package wsj.包头市_000;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 包头市_xw {
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		String[] urlx=new String[7];
		String qye=null;
			urlx[0]="http://wjw.baotou.gov.cn/wsyw/";
			urlx[1]="http://wjw.baotou.gov.cn/wsdt/";
			urlx[2]="http://wjw.baotou.gov.cn/wjtz/";
			
			urlx[3]="http://wjw.baotou.gov.cn/jgzn/cwgh/";
			urlx[4]="http://wjw.baotou.gov.cn/flfg/";
			urlx[5]="http://wjw.baotou.gov.cn/jgzn/zzrs/";
			urlx[6]="http://wjw.baotou.gov.cn/wjek/";
			
			for(int i=1;i<=6;i++){
				
				System.out.println(i);
				Document doc0=GetDocument.connect(urlx[i]);
				String str=doc0.select("#page > table > tbody > tr > td").text();
				int a0=str.lastIndexOf("/");
				int a1=str.lastIndexOf("页");
				String yestr=str.substring(a0+1,a1);
				int ye=Integer.parseInt(yestr);
				String qye1=doc0.select("#page > table > tbody > tr > td > a:nth-child(6)").attr("abs:href");
				
				for(int p=1;p<=ye;p++){
					if(i==0){
						p=3;
					}
			String url=null;
			
			if(p<=5){
				 if(p==1){
				    	url=urlx[i]+"index.html";
				    }else{
				    	url=urlx[i]+"index_"+p+".html";
				    }
				
			}else{
				//http://wjw.baotou.gov.cn/websys/catalog/15800/pc/index_11.html
				
			     
			    	 if(qye1.contains("6")){
			    	  int a=qye1.indexOf("_");
			    	  qye1=qye1.substring(0, a+1);
				      url=qye1+p+".html";
			    	 }
			      
			}
	
			try{
				System.err.println(url);
			
			Document doc = GetDocument.connect(url);
			
			String lei=null;
			try{
			lei=doc.select("body > div.webbg > div:nth-child(2) > div.central > div.menu-title a").last().text();
			}catch(Exception e){
				e.printStackTrace();
			}
			Elements lis = doc.select("body > div.webbg > div:nth-child(2) > div.central > div.list-li2  ul li");
			for (Element li : lis) {
				
			
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				String time=li.select("span").text();
				Document doc2=GetDocument.connect(titlehref);
			//	String lei=doc2.select("body > div.webbg > div:nth-child(2) > div.central > div.menu-title > span > a").last().text();
				String txt=doc2.select("body > div.webbg > div:nth-child(2) > div.central > div.list-li2 > div.contents-bottom2").text();
                String yuma=doc2.select("body > div.webbg > div:nth-child(2) > div.central").html();
                String sci=null;
				sci=doc2.select("div.central > div.list-li2 > div.contents-bottom2 > p").first().text();
				if(!sci.contains("包卫计委办发")){
					sci=null;
				}
				String download_url=GetDocument.connect(titlehref).select("div.list-li2 > div.contents-bottom2  a").attr("abs:href");
				
				 if(time.contains("2016")||time.contains("2017")){
					    Object[] parpm = {lei,title,titlehref,time,sci,txt,yuma,download_url};
						String sql  = "insert into "+tableName + "(lei,title,titlehref,time,sci,txt,yuma,download_url) values(?,?,?,?,?,?,?,?)";
						SQLHelper.insertBySQL(sql,parpm);
						System.out.println(title+"  "+titlehref);
				   }
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
}}
	}
}

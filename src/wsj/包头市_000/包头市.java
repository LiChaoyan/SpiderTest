package wsj.包头市_000;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 包头市 {
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		String  url1 = "http://wjw.baotou.gov.cn/zfxxgk/index_";
		String url2="http://wjw.baotou.gov.cn/websys/catalog/15801/pc/index_";           
		for(int i=2;i<=11;i++){//10,11,
			try{
				
			    String url=null;
			   
				if(i<=5){
					 if(i==1){
					    	url="http://wjw.baotou.gov.cn/zfxxgk/index.html";
					    }else{
					    	url=url1+i+".html";
					    }
					
				}else{
					url=url2+i+".html";
				}
		
			Document doc = GetDocument.connect(url);
			
			System.out.println(url);
			
			Elements lis = doc.select("div.central > div.right-list > div.list-li  li");
			for (Element li : lis) {
				
			    
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				String time=li.select("span").text();
				
				Document doc2 = GetDocument.connect(titlehref);
				String sci=null;
				sci=doc2.select("div.central > div.list-li2 > div.contents-bottom2 > p").first().text();
				if(!sci.contains("包卫计委办发")){
					sci=null;
				}
				String lei=doc2.select("body > div.webbg > div:nth-child(2) > div.central > div.menu-title > span > a").last().text();
				String txt=doc2.select("body > div.webbg > div:nth-child(2) > div.central > div.list-li2 > div.contents-bottom2").text();
                String yuma=doc2.select("body > div.webbg > div:nth-child(2) > div.central").html();
				String download_url=doc2.select("div.contents-bottom2  a").attr("abs:href");
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
		
		}
	}
	
}

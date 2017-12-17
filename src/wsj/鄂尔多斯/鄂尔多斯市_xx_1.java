package wsj.鄂尔多斯;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 鄂尔多斯市_xx_1 {
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		
			String urlx="http://www.ordoswsjs.gov.cn/xxgk_79208/yjsgk/index_";
		//信息公开/预算公开
		for(int i=1;i<=6;i++){//10,11,
			try{
				String url=null;
				if(i==0){
					url="http://www.ordoswsjs.gov.cn/xxgk_79208/yjsgk/";
				}else{
					url=urlx+i+".html";
				}
			System.out.println(url+i+".html");
			Document doc = GetDocument.connect(url);
			
			String lei="信息公开";
			String xlei="预决算公开";
			
			Elements lis = doc.select("body > div > table:nth-child(6) > tbody > tr > td.k1 > table:nth-child(3) > tbody > tr");
			for (Element li : lis) {
				
			
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				String time=li.select("td.fs-13").text();
				
				Document doc2=null;
				doc2=GetDocument.connect(titlehref);
				String source=null;
				source=doc2.select("span.STYLE2").text();
				if(!source.isEmpty()){
					int a0=source.indexOf("发布时间");
					if(a0>2){
					source=source.substring(0,a0);
					}
				}
				
				String yuma=doc2.toString();
				
				Object[] parpm = {lei,xlei,title,titlehref,time,yuma,source};
				String sql  = "insert into "+tableName + "(lei,xlei,title,titlehref,time,yuma,source) values(?,?,?,?,?,?,?)";
				SQLHelper.insertBySQL(sql,parpm);
				System.out.println(title+"  "+titlehref);
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		}
	}
}


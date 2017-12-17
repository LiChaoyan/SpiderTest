package wsj.呼伦贝尔市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 呼伦贝尔市_yw {
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		String  url = "http://www.hlbewsjsw.gov.cn/gzdt/";
		for(int i=153;i<=359;i++){//153
			try{
			//Document doc = GetDocument.changeIp(url+i+"/","gzip, deflate");
				Document doc = GetDocument.connect(url+i+"/");
			System.out.println(url+i+"/");
			System.out.println(doc.toString().length()+"***********************************************************");
			Elements lis = doc.select("div.holder> div.subright> ul.newslist> li");
			for (Element li : lis) {
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				String time=li.select("span").text();
				System.err.println("@@ "+titlehref+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				
				//Document doc2 = GetDocument.changeIp(titlehref,"gzip, deflate");
				Document doc2 = GetDocument.connect(titlehref);
				//System.err.println(doc2.toString());
				String yuma=doc2.toString();
				Elements li2s= doc2.select("#sdcms_content  a");
				String download_urls =new String() ;
				for(Element li2:li2s){
					download_urls=download_urls+li2.select("a").attr("abs:href")+";";
				}
			 
					String lei="卫计要闻";
					Object[] parpm = {lei,title,titlehref,time,yuma,download_urls};
					String sql  = "insert into "+tableName + "(lei,title,titlehref,time,yuma,download_urls) values(?,?,?,?,?,?)";
					SQLHelper.insertBySQL(sql,parpm);
					System.out.println(title+"  "+titlehref);
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	}
}

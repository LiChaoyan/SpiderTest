package wsj.呼伦贝尔市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 呼伦贝尔市 {
	
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		//首页http://www.hlbewsjsw.gov.cn/
		String  url = "http://www.hlbewsjsw.gov.cn/zfxxgk/";
		for(int i=6;i<=9;i++){//9
			try{
			Document doc = GetDocument.connect(url+i+"/");
			
			System.out.println(url+i+"/");
			
			Elements lis = doc.select("div.subright > ul.newslist > li");
			for (Element li : lis) {
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				String time=li.select("span").text();
				
				Document doc2 = GetDocument.connect(titlehref);
				String yuma=doc2.html();
				Elements li2s= doc2.select("#sdcms_content  a");
				String download_urls = "";
				for(Element li2:li2s){
					download_urls=download_urls+li2.select("a").attr("abs:href")+";";
				}
			 
					String lei="政府信息公开";
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

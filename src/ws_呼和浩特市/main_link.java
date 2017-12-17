package ws_呼和浩特市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class main_link {
	private static String tableName = ReadConfig.tablenamelink; // 读取配置文件中的表名
	public static void main(String[] args) {
		
		String url="http://www.hhhtwjw.gov.cn/";
		try{
		Document doc=GetDocument.connect(url);
		
		Elements lis=doc.select("#nav > ul.c > li");
		for(Element li:lis){
			String pageurl0=li.select("span.v > a").attr("abs:href");
			System.out.println(pageurl0);
			try{
			Document doc2=GetDocument.connect(pageurl0);
			Elements li2s=doc2.select("#mm1 > ul > li");
			for(Element li2:li2s){
				String pageurl=li2.select("a").attr("abs:href");
				System.out.println(pageurl);
				
	                String pagetitle=li2.select("a").text();
					Object[] parpm = {pageurl,pagetitle};
					String sql  = "insert into "+tableName + "(pageurl,pagetitle) values(?,?)";
					SQLHelper.insertBySQL(sql,parpm);		}//2f
			}catch(Exception e){
				e.printStackTrace();
			}
	}//f
		}catch(Exception e){
			e.printStackTrace();
		}
   }
}

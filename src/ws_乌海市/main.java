package ws_乌海市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class main {

	private static String tableName = ReadConfig.tablenamelink;
	public static void main(String[] args) {
		   String url="http://wsj.wuhai.gov.cn/index.aspx?lanmuid=67&sublanmuid=620";
		         
			try{
				//信息公开/预算公开其他
			
			Document doc = GetDocument.connect(url);
			
			Elements lis=doc.select("#Menu >ul >li> a");
		    for(int i=1;i<lis.size();i++){
		    	
		    	Element li=lis.get(i);
		    	String pageurl0=li.select("a").attr("abs:href");
		    	String pagetitle0=li.select("a").text();
		    	Document doc1=GetDocument.connect(pageurl0);
		    	Elements li1s=doc1.select(".nav_menu> ul> li a");
		    	for(Element li1:li1s){
		    		String pageurl=li1.attr("abs:href");
		    		String pagetitle=li1.text();
		    	Document doc2=GetDocument.connect(pageurl);
		    	 int ye=0;
		    	try{
		    	String yestr=doc2.select("div.sublanmu_content_article> div.sublanmu_page > a.lastpage").attr("abs:href");
		    	System.out.println(yestr);
		    	int a=yestr.lastIndexOf("=");
		    	yestr=yestr.substring(a+1);
			    ye=Integer.parseInt(yestr);
			    
			    System.out.println(ye);
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
				//http://wjw.bynr.gov.cn/html/dzzt/jsxxxdzz/index.html
			    Object[] parpm = {pagetitle,pageurl,ye};
				String sql  = "insert into "+tableName + "(pagetitle,pageurl,ye) values(?,?,?)";
			    SQLHelper.insertBySQL(sql,parpm);
			    
			    
			}
		    }
				
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

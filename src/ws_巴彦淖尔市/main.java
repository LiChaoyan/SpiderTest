package ws_巴彦淖尔市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class main {

	private static String tableName = ReadConfig.tablenamelink;
	public static void main(String[] args) {
		    
		   
			   
		         String url="http://wjw.bynr.gov.cn/html/dzzt/";
		         
			try{
				//信息公开/预算公开其他
			
			Document doc = GetDocument.connect(url);
			
			Elements lis=doc.select("#menuTree > ul  li.child");
		    for(int i=1;i<lis.size();i++){
		    	Element li=lis.get(i);
		    	String pageurl=li.select("a").attr("abs:href");
		    	String pagetitle=li.select("a").text();
		    	Document doc2=GetDocument.connect(pageurl);
		    	 int ye=0;
		    	try{
		    	String yestr=doc2.select("#pages > a:nth-child(1)").text();
		    	System.out.println(yestr);
		    	
		    	yestr=yestr.substring(0,yestr.length()-1);
			    int num=Integer.parseInt(yestr);
			     ye=num/20+1;
			    System.out.println(ye);
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
				//http://wjw.bynr.gov.cn/html/dzzt/jsxxxdzz/index.html
			    Object[] parpm = {pagetitle,pageurl,ye};
				String sql  = "insert into "+tableName + "(pagetitle,pageurl,ye) values(?,?,?)";
			    SQLHelper.insertBySQL(sql,parpm);
			    
			    
			}
				
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

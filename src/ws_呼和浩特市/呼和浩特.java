package ws_呼和浩特市;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.helper.HttpConnection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 呼和浩特 {

	private static ArrayList<HashMap<String, Object>> rows; // 用来封装从数据库取到的信息

	private static String tableNamelink = ReadConfig.tablenamelink; // 读取配置文件中的表名
	private static String tableName = ReadConfig.tablename; // 读取配置文件中的表名

	private static synchronized HashMap<String, Object> getRow() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pageurl from " + tableNamelink + " where mark<"
					+ ReadConfig.mark1 
					+ " limit 16"; // 每次取出1000个没有补充详细的链接
			rows = SQLHelper.selectBySQL(str);
		
			if (rows.size() <= 0) {
				System.out.println("==========未取到链接=======");
				System.exit(0);
			}
		}
		HashMap<String, Object> row = rows.get(0);
		rows.remove(0);
		return row;
	}

	public static void main(String[] args) {
		for (int i = 0; i < ReadConfig.thread; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (;;) {
						Fetch();
					}
				}
			}).start();
		}
	}

	public static void Fetch() {
		HashMap<String, Object> row = getRow();
		String pageurl = row.get("pageurl").toString();  //注意参数和数据库字段一样
		int id = Integer.parseInt(row.get("id").toString());
		
		try {
			Document doc3=GetDocument.connect(pageurl);
		int ye=33;
		for(int p=0;p<=ye;p++){
			String purl=null;
			if(p==0){
				purl=pageurl;
				
			}else{//http://www.hhhtwjw.gov.cn/zwgk/tzgg/index_2.html
				purl=pageurl+"index_"+p+".html";
			}
			doc3=GetDocument.connect(purl);
			Elements li3s=doc3.select("#mm2 > ul > li");
			for(Element li3:li3s){
				String titlehref=li3.select("a").attr("abs:href");
				System.out.println("titlehref"+titlehref);
				String title=li3.select("a").text();
				try{
				Document doc4=GetDocument.connect(titlehref);
				String lei=doc4.select("#main > div > div.c_t > span > a").last().text();
				String sec=doc4.select("#main > div > div.title_f").text();
				String time=null,scoure=null,author=null;
				try{
					int a00=sec.indexOf("发布日期");
					int a0=sec.indexOf("来源");
					int a1=sec.indexOf("作者");
					
					 time=sec.substring(a00,a0-4);
					 System.out.println(time);	
					}catch(Exception e){
						e.printStackTrace();
					}
				String yuma=doc4.select("div.c_c").html();
				String txt=doc4.select(".content").text();
				
				Object[] parpm = {lei,titlehref,title,time,scoure,txt,yuma};
				String sql  = "insert into "+tableName + "(lei,titlehref,title,time,scoure,txt,yuma) values(?,?,?,?,?,?,?)";
			    SQLHelper.insertBySQL(sql,parpm);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}//3f
					
			
			
		}//p
			
	
	
	} catch (Exception e) {
			System.out.println(pageurl+ "   出现异常");
			String str2 = "Update " + tableNamelink + " set mark=mark+1 where id="
					+ id; // 出现异常将标记+1
			SQLHelper.updateBySQL(str2);
			e.printStackTrace();
		}
	}


}

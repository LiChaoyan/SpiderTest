package ws_兰察布市;

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

public class 兰察布市 {

	private static ArrayList<HashMap<String, Object>> rows; // 用来封装从数据库取到的信息

	private static String tableNamelink = ReadConfig.tablenamelink; // 读取配置文件中的表名
	private static String tableName = ReadConfig.tablename; // 读取配置文件中的表名

	private static synchronized HashMap<String, Object> getRow() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pageurl,ye from " + tableNamelink + " where mark<"
					+ ReadConfig.mark1 
					+ " limit 100"; // 每次取出1000个没有补充详细的链接
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
		String yurl = row.get("pageurl").toString();  //注意参数和数据库字段一样
		int id = Integer.parseInt(row.get("id").toString());
		int ye = Integer.parseInt(row.get("ye").toString());
		
		int num=yurl.lastIndexOf("=");
		String url=yurl.substring(0,num+1);
		
		for(int p=1;p<=ye;p++){
			String purl=null;
			if(p==1&&ye==1){
				purl=yurl;
			}else{
		purl=url+p;
		}
		System.out.println(purl);
		Document doc=GetDocument.connect(purl);
		Elements lis=doc.select("body > table > tbody > tr");
		for(Element li:lis){
		   String titlehref=li.select("a").attr("abs:href");
		   String title=li.select("a").text();
		   String time=li.select(".addtime").text();
		   System.out.println(titlehref);
		try {
			Document docur=GetDocument.connect(titlehref);
			
			    String sci=docur.select("#secondtitle").text();
			    String txt=docur.select("#content").text();
				String yuma=docur.select("#hlcms_4").html();
				int leishu=docur.select("#hlcms_6 > div > span").size();
				String lei=docur.select("#hlcms_6 > div > span").get(leishu-2).text();
				System.out.println(lei);
				String sec=docur.select("#otherinfo").text();
				String[] secx=sec.split(" ");
				String scoure=null;
				try{
					
					int a0=secx[1].indexOf("作者：");
					int a1=secx[1].indexOf("浏览次数");
					 scoure=secx[1].substring(a0,a1);
					}catch(Exception e){
						e.printStackTrace();
					}
			
			Object[] parpm = {lei,titlehref,title,sci,time,scoure,txt,yuma};
			String sql  = "insert into "+tableName + "(lei,titlehref,title,sci,time,scoure,txt,yuma) values(?,?,?,?,?,?,?,?)";
		    SQLHelper.insertBySQL(sql,parpm);
			} catch (Exception e) {
			System.out.println(titlehref+ "   出现异常");
			
			String str = "Update " + tableNamelink + " set mark=mark+1 where id="
					+ id; // 出现异常将标记+1
			SQLHelper.updateBySQL(str);
			e.printStackTrace();
		}
	}
		}
}
}

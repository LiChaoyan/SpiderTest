package ws_巴彦淖尔市;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class FetchInform {
	private static ArrayList<HashMap<String, Object>> rows; // 用来封装从数据库取到的信息

	private static String tableNamelink = ReadConfig.tablenamelink; // 读取配置文件中的表名
	private static String tableName = ReadConfig.tablename; // 读取配置文件中的表名

	private static synchronized HashMap<String, Object> getRow() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pagetitle,pageurl,ye from " + tableNamelink + " where id=11 or id=13 and  mark< "+ ReadConfig.mark1
					//+ ReadConfig.orderBy  
					//mark默认为1，如果make1为5，则允许出错4次，一般出错为没有获取到源码或者选择器选到为空
					+ " limit 1000"; // 每次取出1000个没有补充详细的链接
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
		String pagetitle = row.get("pagetitle").toString();  
		int id = Integer.parseInt(row.get("id").toString());
		int ye = Integer.parseInt(row.get("ye").toString());
		try {
			for(int p=1;p<=ye;p++){
				String url=null;
				if(ye==0||p==1){
					url=pageurl;
				}else{
					url=pageurl+p+".html";
				}
				try{
				Document doc=GetDocument.connect(url);
				Elements lis=doc.select("#news_list > ul > li");
				for(Element li: lis){
					String titlehref=li.select("a").attr("abs:href");
					String title=li.select("a").text();
					String time=li.select("span").text();
				try{
				Document doc2 = GetDocument.connect(titlehref);
				
				if(doc2.toString().equals("")){
					System.out.println("dic2为空！");
				}else{
				String scoure=null;
				try{
				scoure=doc2.select("#news_detail > div.date").text();
				int a=scoure.indexOf("来源");
				scoure=scoure.substring(a);
				}catch(Exception e){
					e.printStackTrace();
				}
				String txt=doc2.select("#news_detail > div.newsbody").text();
				String yuma=doc2.select("#rightcontent").html();
				String lei=pagetitle;
				if(time.contains("2016")||time.contains("2017")){
				Object[] parpm = {lei,title,titlehref,time,yuma,txt,scoure};
				String sql  = "insert into "+tableName + "(lei,title,titlehref,time,yuma,txt,scoure) values(?,?,?,?,?,?,?)";
				SQLHelper.insertBySQL(sql,parpm);
				System.out.println(title+"  "+titlehref);
				}
				}
				}catch (Exception e) {
					System.out.println(pageurl+ "   出现异常");
					String str = "Update " + tableNamelink + " set mark=mark+1 where id="
							+ id; // 出现异常将标记+1
					SQLHelper.updateBySQL(str);
					e.printStackTrace();
				}
				}
			}catch (Exception e) {
				System.out.println(pageurl+ "   出现异常");
				String str = "Update " + tableNamelink + " set mark=mark+1 where id="
						+ id; // 出现异常将标记+1
				SQLHelper.updateBySQL(str);
				e.printStackTrace();
				}
			}
			} catch (Exception e) {
			System.out.println(pageurl+ "   出现异常");
			String str = "Update " + tableNamelink + " set mark=mark+1 where id="
					+ id; // 出现异常将标记+1
			SQLHelper.updateBySQL(str);
			e.printStackTrace();
		}
	}
}

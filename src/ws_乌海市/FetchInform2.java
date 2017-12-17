package ws_乌海市;

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

public class FetchInform2 {
	private static ArrayList<HashMap<String, Object>> rows; // 用来封装从数据库取到的信息

	private static String tableNamelink = ReadConfig.tablenamelink; // 读取配置文件中的表名
	private static String tableName = ReadConfig.tablename; // 读取配置文件中的表名

	private static synchronized HashMap<String, Object> getRow() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pagetitle,pageurl,ye from " + tableNamelink + " where ye=0 and mark< "+ ReadConfig.mark1
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
		
		Document doc=GetDocument.connect(pageurl);
		Elements lis=doc.select("div.sublanmu_box > div.sublanmu_content > ul.article > li");
		if(lis.toString().length()<10){
			lis=doc.select("div.sublanmu_box > div.sublanmu_content > div.job > table > tbody > tr.title");
		}
		for(Element li: lis){
			String sci=null;
			sci=li.select("td:nth-child(1)").text();
			String titlehref=li.select("a").attr("abs:href");
			String title=li.select("a").text();
			String time=li.select(".date").text();
			if(time.isEmpty()||time==null){
				time=li.select("td:nth-child(3)").text();
			}
			System.out.println(title+"  "+titlehref);
				try{
				Document doc2 = GetDocument.connect(titlehref);
				
				//String title=doc2.select("#sublanmu_content > div > ul > li.title > h1").text();
				if(doc2.toString().equals("")){
					System.out.println("dic2为空！");
				}else{
				String source=null;
				try{
					source=doc2.select("#sublanmu_content > div.articleinfor > ul > li.info").text();
				    int a0=source.indexOf("日期");
					int a=source.indexOf("作者");
				int b=source.indexOf("【");
				//time=source.substring(0,a0);
				source=source.substring(a,b);
				}catch(Exception e){
					e.printStackTrace();
				}
				String txt=doc2.select("#Content").text();
				String yuma=doc2.select("div.main_box_inner > div.main_box_inner_right").html();
				String lei=pagetitle;
			
				if(time.contains("2016")||time.contains("2017")){
				Object[] parpm = {lei,title,titlehref,time,sci,yuma,txt,source};
				String sql  = "insert into "+tableName + "(lei,title,titlehref,time,sci,yuma,txt,source) values(?,?,?,?,?,?,?,?)";
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
	}
}

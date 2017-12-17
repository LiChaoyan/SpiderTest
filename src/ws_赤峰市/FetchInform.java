package ws_赤峰市;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;

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

	private static String tableName = ReadConfig.tablename; // 读取配置文件中的表名

	private static synchronized HashMap<String, Object> getRow() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,yuma from " + tableName + " where mark< "
					+ ReadConfig.mark1
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
		String yuma = row.get("yuma").toString();  //注意参数和数据库字段一样
		int id = Integer.parseInt(row.get("id").toString());
		try {
			
				
				Document doc=Jsoup.parse(yuma);
				yuma=doc.select("#page_content").html();
					Object[] params = {yuma};
					String str = "Update " + tableName
							+ " set yuma=?,mark="
							+ReadConfig.mark2+" where id=" + id; // 补充完成将mark标记为mark2的值
					SQLHelper.updateBySQL(str,params);
					
					System.out.println(id);
				
		} catch (Exception e) {
			
			String str = "Update " + tableName + " set mark=mark+1 where id="
					+ id; // 出现异常将标记+1
			SQLHelper.updateBySQL(str);
			e.printStackTrace();
		}
	}
}

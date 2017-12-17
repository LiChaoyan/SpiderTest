package wsj.通辽市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 通辽市 {

	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		String  url = "http://www.tlswjw.gov.cn/infogk/150500/201409210934311611/";
		           
		for(int i=1;i<=16;i++){
			try{
			Document doc = GetDocument.connect(url+i+".shtml");
			
			
			
			Elements lis = doc.select("div.p_top_10 > div.min_h_450.p_top_20 > div > div.gtdw2");
			for (Element li : lis) {
				
			
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				
				
				Document doc2 = GetDocument.connect(titlehref);

				String sci= doc2.select("div.p_top_10.f_size_14 > table > tbody > tr:nth-child(1) > td:nth-child(2)").text();
				String fbjg=doc2.select("div.p_top_10.f_size_14 > table > tbody > tr:nth-child(2) > td:nth-child(2)").text();
				String xlei=doc2.select("div.p_top_10.f_size_14 > table > tbody > tr:nth-child(1) > td:nth-child(4)").text();
				String time= doc2.select("div.p_top_10.f_size_14 > table > tbody > tr:nth-child(2) > td:nth-child(4)").text();
			    String yuma=doc2.toString();
				String lei="信息公开目录";
				
				
					
					 
					
					Object[] parpm = {lei,title,titlehref,time,sci,fbjg,xlei,yuma};
					String sql  = "insert into "+tableName + "(lei,title,titlehref,time,sci,fbjg,xlei,yuma) values(?,?,?,?,?,?,?,?)";
					SQLHelper.insertBySQL(sql,parpm);
					System.out.println(title+"  "+titlehref);
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		}
	}

}

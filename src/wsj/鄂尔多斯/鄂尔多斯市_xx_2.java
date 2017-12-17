package wsj.鄂尔多斯;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 鄂尔多斯市_xx_2 {
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		
			String[] urlx={"http://www.ordoswsjs.gov.cn/xxgk_79208/zfxxgkzd/",
			"http://www.ordoswsjs.gov.cn/xxgk_79208/zfxxgkzn/",
		   "http://www.ordoswsjs.gov.cn/xxgk_79208/zfxxgkml/",
		   "http://www.ordoswsjs.gov.cn/xxgk_79208/zfxxgknb/",
		   "http://www.ordoswsjs.gov.cn/xxgk_79208/zfxxysqgk/"};
		   for(int i=0;i<urlx.length-1;i++){
			   
		         String url=urlx[i];
			try{
				//信息公开/预算公开其他
			
			Document doc = GetDocument.connect(url);
			String lei="信息公开";
			String xlei=doc.select("table.jz.xxk  span.home-tname").text();
			
			
			Elements lis = doc.select("body > div > table:nth-child(6) > tbody > tr > td.k1 > table:nth-child(3) > tbody > tr");
			for (Element li : lis) {
				
			
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				String time=li.select("td.fs-13").text();
				
				Document doc2=null;
				doc2=GetDocument.connect(titlehref);
				String source=null;
				source=doc2.select("span.STYLE2").text();
				if(!source.isEmpty()){
					int a0=source.indexOf("发布时间");
					if(a0>2){
					source=source.substring(0,a0);
					}
				}
				
				String yuma=doc2.toString();
				Object[] parpm = {lei,xlei,title,titlehref,time,yuma,source};
				String sql  = "insert into "+tableName + "(lei,xlei,title,titlehref,time,yuma,source) values(?,?,?,?,?,?,?)";
				SQLHelper.insertBySQL(sql,parpm);
				System.out.println(title+"  "+titlehref);
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		   }
		}
	}



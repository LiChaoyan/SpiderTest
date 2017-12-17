package wsj.鄂尔多斯;

import org.apache.http.conn.scheme.Scheme;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 鄂尔多斯市_gg {
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		

		int[] yema={34,3,16,34,3,12,5,1,1};
		String[]  urlx = {
				"http://www.ordoswsjs.gov.cn/zxdt/",//最新动态
				"http://www.ordoswsjs.gov.cn/xwfb/",//新闻发布
				"http://www.ordoswsjs.gov.cn/tzgg_79203/",//通知公告
				"http://www.ordoswsjs.gov.cn/qqdt_79204/",//旗区动态
				"http://www.ordoswsjs.gov.cn/zcfg/",//政策法规
				"http://www.ordoswsjs.gov.cn/xgbd_79205/",//媒体报道
				"http://www.ordoswsjs.gov.cn/zdzt/lajc/",//重点专题一
				"http://www.ordoswsjs.gov.cn/zdzt/ordosmy/",//重点专题二
				"http://www.ordoswsjs.gov.cn/spdb/"//领导访谈
				};
		for(int ix=5;ix<=8;ix++){//11
			
		
		for(int p=0;p<yema[ix];p++){
			
		String url=null;
		if(p==0){
			url=urlx[ix];
		}else{
			url=urlx[ix]+"index_"+p+".html";
		}
			
		try{
			Document doc = GetDocument.connect(url);
		
			System.out.println(url);
			String lei="通知公告";
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
}


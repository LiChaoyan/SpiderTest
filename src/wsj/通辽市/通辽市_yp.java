package wsj.通辽市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 通辽市_yp {

	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
	
		int[] yema={1,24};
		String[]  urlx = {
				"http://www.tlswjw.gov.cn/listimg/150500/201409131418169268.shtml",//视频
				"http://www.tlswjw.gov.cn/listimg/150500/201409131418560905/",//图片
				};
		for(int ix=0;ix<2;ix++){//11
			
		
		for(int p=1;p<=yema[ix];p++){
			
		String url=null;
		if(p==1&&yema[ix]==1){
			url=urlx[ix];
		}else{
			url=urlx[ix]+p+".shtml";
		}
			try{
			Document doc = GetDocument.connect(url);
			System.err.println(url); 
			String xlei=doc.select("#c_name").text();
			Elements lis = doc.select("div.f_right.page_right  div.min_h_450 > div.f_left");
			
			for (Element li : lis) {
				
			
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
			
				
				
				Document doc2 = GetDocument.connect(titlehref);
				
				String time=doc2.select("#info_time").text();
                String author=doc2.select("#info_author").text();
                String fbjg=doc2.select("#info_Source").text();
				String yuma=doc2.toString();
				String lei="新闻中心";
				
				
					
					 
					
					Object[] parpm = {lei,title,titlehref,time,author,fbjg,xlei,yuma};
					String sql  = "insert into "+tableName + "(lei,title,titlehref,time,author,fbjg,xlei,yuma) values(?,?,?,?,?,?,?,?)";
					SQLHelper.insertBySQL(sql,parpm);
					//System.out.println(title+"  "+titlehref);
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		}
		}
		
	}

}

package ws_赤峰市;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.ReadConfig;
import utils.SQLHelper;

public class 赤峰市 {
	private static String tableName = ReadConfig.tablename;
	public static void main(String[] args) {
		    
		    int[] urly={45,49,50,52,53,54,55,56,57};
		    String urlx="http://www.nmcfws.gov.cn/index.php?g=Home&m=List&a=index&cid=";
			
		    for(int i=0;i<urly.length;i++){
			   
		         String url=urlx+urly[i];
		         
			try{
				//信息公开/预算公开其他
			
			Document doc = GetDocument.connect(url);
			
			String lei=doc.select("body > div.container > div.content > div.content-bt > div.content-bt-zi").text();
		
			String xlei=null;
			
			int a0=lei.indexOf(">>");
			int b0=lei.lastIndexOf(">>");
			if(a0==b0){
				lei=lei.substring(a0+3);
			}else{
				xlei=lei.substring(b0+3);
				lei=lei.substring(a0+3,b0);
				
			}
			String yestring=doc.select("div.content-bg > div > div.quotes").text();
			int y0=yestring.lastIndexOf("共");
			int y1=yestring.lastIndexOf("页");
			String yestr=yestring.substring(y0+1,y1);
			int ye=Integer.parseInt(yestr);
			System.out.println(ye);
			for(int p=1;p<=ye;p++){
				//http://www.nmcfws.gov.cn/index.php?m=List&a=index&cid=52&p=3
				String purl=url+"&p="+p;
				System.out.println(purl);
				try{
			Document docp=GetDocument.connect(purl);
			
			Elements lis = docp.select("body > div.container > div.content > div.content-bg> ul> li");
			for (Element li : lis) {
			
				String titlehref = li.select("a").attr("abs:href");
				String title=li.select("a").text();
				String time=li.select("span.time").text();
				
				Document doc2=null;
				doc2=GetDocument.connect(titlehref);
				String source=null;
				source=doc2.select("body > div.nr > div.nr-bg > div:nth-child(2) > span:nth-child(2)").text();
			
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
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		   }
		}
	}



package wsj.包头市_000;

import org.jsoup.nodes.Document;

import utils.GetDocument;

public class test {

	public static void main(String[] args) {
		Document doc=GetDocument.connect("http://wjw.baotou.gov.cn/websys/catalog/15809/pc/index_6.html");
		String str=doc.select("#page > table > tbody > tr > td").text();
		
		int a=str.lastIndexOf("/");
		int a1=str.lastIndexOf("页");
		String yestr=str.substring(a+1,a1);
		
		int ye=Integer.parseInt(yestr);
		
		

	}

}

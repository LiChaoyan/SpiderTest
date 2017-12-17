package ws_乌海市;

import javax.print.Doc;

import org.jsoup.nodes.Document;

import utils.GetDocument;



public class test {

	public static void main(String[] args) {
		Document doc=GetDocument.connect("http://wsj.wuhai.gov.cn/index.aspx?lanmuid=68&sublanmuid=625");
		String yestr=doc .select("div.sublanmu_content_article> div.sublanmu_page > a.lastpage").attr("abs:href");
    	System.out.println(yestr);
    	int a=yestr.lastIndexOf("=");
    	yestr=yestr.substring(a+1);
	    int ye=Integer.parseInt(yestr);
	    
	    System.out.println(ye);
	}

}

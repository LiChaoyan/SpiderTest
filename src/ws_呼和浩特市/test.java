package ws_呼和浩特市;

public class test {

	public static void main(String[] args) {
		String sec="〖发布日期:2017-12-08〗 〖来源:〗〖作者:〗 〖";
		String time=null,scoure=null,author=null;
	
			int a00=sec.indexOf("发布日期");
			int a0=sec.indexOf("来源");
			int a1=sec.indexOf("作者");
			System.out.println(a00+" "+a0+" "+a1);
			 time=sec.substring(a00,a0-4);
			 scoure=sec.substring(a0,a1-4);
			 author=sec.substring(a1);
			System.out.println(time);
			System.out.println(scoure);
			

	}

}

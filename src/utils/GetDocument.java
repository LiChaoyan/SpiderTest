package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import TOU_https.SSLClient;
import utils.ProxyHost.IP;

public class GetDocument {
	public static Document changeIp(String url,String Encoading){
		Document document1 = null;
		int ii = 1;
		while (ii <= 10) { // 请求超时多次请求
			try {
				document1 = changeIp1(url, Encoading);
				return document1;
			} catch (Exception e1) {
				ii++;
			}
	}
		return null;
	}
	
	public static Document changeIp1(String url,String Encoading) {
		StringBuilder sBuilder = null;
		InputStream inputStream = null;
		IP ip = null;
		Document document = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			
			//https
			//HttpClient httpClient = new SSLClient();  
			if (ReadConfig.isProxy) {
				HttpHost proxy = null;
				if (ReadConfig.isuseGoAgent) {
					proxy = new HttpHost(ReadConfig.proxyIp,
							ReadConfig.proxyPort);
				} else {
					ip = ProxyHost.getIp(true);
					proxy = new HttpHost(ip.ip, ip.port);
				}
				
				httpClient.getParams().setParameter(
						ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT,6* 6* 1000);// 鏉╃偞甯撮弮鍫曟？1閸掑棝鎸?
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT,6* 6* 1000);// 閺佺増宓佹导鐘虹翻閺冨爼妫?鐏忓繑妞?
			httpClient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
			HttpGet httpGet = new HttpGet(url.trim());
			httpGet.setHeader("User-Agent", UserAgent.getUserAgent());
			httpGet.setHeader("Cookie",ReadConfig.cookie);
			httpGet.setHeader("Host","www.hlbewsjsw.gov.cn");
			httpGet.setHeader("Referer","http://www.hlbewsjsw.gov.cn/gzdt/3/");
			httpGet.setHeader("Connection","keep-alive");
			httpGet.setHeader("Upgrade-Insecure-Requests","1");
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			httpGet.setHeader("Accept-Encodinggzip","gzip, deflate");
			httpGet.setHeader("Cache-Control","max-age=0");
			httpGet.setHeader("Cookie","__cfduid=d6d0235102c28083a90d1d3e8076095b41512356486; UM_distinctid=1601f792c33d0-0e32e3f0f3bcc2-5b4d261d-100200-1601f792c34f6; yjs_id=TW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV09XNjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS82Mi4wLjMyMDIuMTggU2FmYXJpLzUzNy4zNnx3d3cuaGxiZXdzanN3Lmdvdi5jbnwxNTEyMzU2NTIzNTgyfGh0dHA6Ly93d3cuc28uY29tL2xpbms/bT1hU2JiWEdHVjFUSWF0S1hYNkh4NWZXJTJGcjlDZVhSc2gwZmN2OXFpa09wQXlzYXFsejZtRUZPQTU0JTJGcGk2ZGIzd2t3WkN2JTJGcEtjRFlWTlNnU09Vc016U1hIRktFMmR0JTJGdkNZbUNnMmUwUlk3b2ttZzJITDB2U3BBOUloMHZ6bUdhYmJzRHFzSWNkR003SWNWYVJ0eHl3WnRyemRXSFBLemJCdWswN056OVpKR1VIV2pzdHBJS2JHU25BVkdNJTNE; yunsuo_session_verify=96754253cf1d8098f4d6a0e4c226cd76; ctrl_time=1; ASPSESSIONIDCCDSCQST=PIOHCCCDDLEMCLLBIIJHKJHI; CNZZDATA1254174941=860052239-1512352804-http%253A%252F%252Fwww.so.com%252F%7C1512459236; hlbewjw=content%5F14339=38%3A0&content%5F8735=170%3A0");
			
			
            
			
         HttpResponse execute = httpClient.execute(httpGet);
			System.err.println(execute.getStatusLine());
			if (execute.getStatusLine().getStatusCode() != 200) {
				if (ip != null) {
					int code = execute.getStatusLine().getStatusCode();
					System.out.println(ip.ip + ":" + ip.port + "---" + code
							+ "--");
					ProxyHost.removeIp(ip);
				}
				return null;
			}
			BufferedReader bReader = null;
			sBuilder = new StringBuilder();
			HttpEntity entity = execute.getEntity();
			inputStream = entity.getContent();
			if(Encoading.toUpperCase().contains("UTF-8")){
				bReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
				for (String line = bReader.readLine(); line != null; line = bReader
						.readLine()) {
					sBuilder.append(line).append("\r\n");
				}
			}else{
				bReader = new BufferedReader(new InputStreamReader(inputStream,
						"gbk"));
				for (String line = new String(bReader.readLine().getBytes("gbk"),"utf-8"); line != null; line = bReader.readLine()) {
					sBuilder.append(line).append("\r\n");
				}
			}
			if (sBuilder != null) {
				document = Jsoup.parse(sBuilder.toString());
			}
		} catch (Exception ex) {
			if (ip != null) {
				ProxyHost.removeIp(ip);
			}
			ex.printStackTrace();

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return document;
	}
	
	public static Document connect(String url){
		Document document1 = null;
		int ii = 1;
		while (ii <= 1) { // 请求超时多次请求
			try {
				int count=17512;
				document1 = Jsoup
						.connect(url)
						.header("Host","www.ordoswsjs.gov.cn")
						//.header("Cookie","yunsuo_session_verify=18602c94ebf86bb59b5812cabe9f70e9")
						.header("Connection","keep-alive")
						.header("Upgrade-Insecure-Requests","1")
						.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
						.header("Accept-Encodinggzip","gzip, deflate")
						.header("Cache-Control","max-age=0")
						//.header("If-Modified-Since","Wed, 06 Dec 2017 16:08:36 GMT")
                       //  .header("If-None-Match","1508177-91ec-55fae29fa1282")
						.userAgent(
								"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.18 Safari/537.36")
						.ignoreContentType(true).timeout(30000)
						.get();
				      //  System.out.println(count);
				return document1;
			} catch (IOException e1) {
				ii++;
			}
	}
		return null;
	}
}

package qc.net.httpclient4;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

/**
 * @author dragon
 * 
 */
public class HttpClientTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() {
		// 核心应用类
		HttpClient httpClient = new DefaultHttpClient();

		// HTTP请求
		String url = "http://rongjih.blog.163.com/blog/static/335744612010112041023772/";
		//String url = "https://www.ggssl.com";
		HttpUriRequest request = new HttpGet(url);

		// 打印请求信息
		System.out.println("requestLine: " + request.getRequestLine());
		try {
			// 发送请求，返回响应
			HttpResponse response = httpClient.execute(request);

			// 打印响应信息
			System.out.println("StatusLine: " + response.getStatusLine());
			System.out.println("ProtocolVersion: "
					+ response.getProtocolVersion());
			System.out.println("Locale: " + response.getLocale());
			System.out.println("Params: " + response.getParams());
			System.out.println("AllHeaders: "
					+ StringUtils.arrayToDelimitedString(
							response.getAllHeaders(), "\r\n"));
//			System.out.println("Entity: "
//					+ EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (ClientProtocolException e) {
			// 协议错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络异常
			e.printStackTrace();
		}
	}
}

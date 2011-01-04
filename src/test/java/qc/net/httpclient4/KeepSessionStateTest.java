package qc.net.httpclient4;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dragon
 * 
 */
public class KeepSessionStateTest {
	private static final Log logger = LogFactory
			.getLog(KeepSessionStateTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() throws Exception {
		// 核心应用类
		HttpClient httpClient = new DefaultHttpClient();

		// HTTP请求
		String[] urls = {
				"http://localhost:8082/login.do?name=dragon&password=password",
				"http://localhost:8082/debug.do" };
		
		//只要是使用了相同的httpClient，session就是相同的
		doRequest(httpClient, urls[0]);
		doRequest(httpClient, urls[1]);
	}

	private void doRequest(HttpClient httpClient, String url)
			throws IOException, ClientProtocolException {
		//httpClient.getConnectionManager().
		HttpUriRequest request = new HttpGet(url);

		// 打印请求信息
		logger.warn("requestLine: " + request.getRequestLine());
		logger.warn("before request " + request.getURI() + "：");
		for (HeaderIterator itor = request.headerIterator(); itor.hasNext();) {
			logger.warn("  " + itor.next());
		}

		// 发送请求，返回响应
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			logger.warn("after request：");
			for (HeaderIterator itor = request.headerIterator(); itor.hasNext();) {
				logger.warn("  " + itor.next());
			}

			// do something
			logger.warn("response：" + response.getStatusLine().getStatusCode());
			for (HeaderIterator itor = response.headerIterator(); itor
					.hasNext();) {
				logger.warn("  " + itor.next());
			}
			//logger.warn("内容：" + EntityUtils.toString(entity));

			// 保证连接能释放会管理器
			entity.consumeContent();
		}
	}
}

package qc.net.httpclient4;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import qc.apps.clickrate.Config;

/**
 * 多线程连接尝试
 * 
 * @author dragon
 * 
 */
public class MutiThreadRequestTest {
	private static final Log logger = LogFactory
			.getLog(MutiThreadRequestTest.class);

	@Before
	public void setUp() throws Exception {
		Config.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws NoSuchAlgorithmException {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		// 注册http协议模式
		Scheme http = new Scheme("http", PlainSocketFactory.getSocketFactory(),
				80);
		schemeRegistry.register(http);

		// 注册https协议模式
//		SSLSocketFactory sf = new SSLSocketFactory(
//				SSLContext.getInstance("TLS"));
//		sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
//		Scheme https = new Scheme("https", sf, 443);
//		schemeRegistry.register(https);

		// 创建适于多线程连接的连接管理器
		HttpParams params = new BasicHttpParams();
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);

		//
		HttpClient httpClient = new DefaultHttpClient(cm, params);

		// HTTP请求
		String[] urls = {
				"http://localhost:8082/login.do?name=dragon&password=password",
				"http://localhost:8082/debug.do" };

		// URI
		GetThread[] threads = new GetThread[urls.length];
		for (int i = 0; i < threads.length; i++) {
			HttpGet httpget = new HttpGet(urls[i]);
			threads[i] = new GetThread(httpClient, httpget);
		}

		// 开始执行线程
		for (int j = 0; j < threads.length; j++) {
			threads[j].start();
		}

		// 合并线程：等待已开始的线程终止
		for (int j = 0; j < threads.length; j++) {
			try {
				threads[j].join();
			} catch (InterruptedException e) {
				// 如果线程被其他线程中断就会抛出该异常
				logger.error("线程" + j + "被中断", e);
			}
		}
	}

	static class GetThread extends Thread {
		private final HttpClient httpClient;
		private final HttpContext context;
		private final HttpGet httpget;

		public GetThread(HttpClient httpClient, HttpGet httpget) {
			this.httpClient = httpClient;
			this.context = new BasicHttpContext();
			this.httpget = httpget;
		}

		@Override
		public void run() {
			try {
				HttpResponse response = this.httpClient.execute(this.httpget,
						this.context);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// do something
					logger.warn("url：" + this.httpget.getURI());
					logger.warn(response.getStatusLine().getStatusCode() + "：");
					for (HeaderIterator itor = response.headerIterator(); itor
							.hasNext();) {
						logger.warn("  " + itor.next());
					}
					logger.warn("内容：" + EntityUtils.toString(entity));

					// 保证连接能释放会管理器
					entity.consumeContent();
				}
			} catch (Exception ex) {
				this.httpget.abort();
			}
		}
	}
}

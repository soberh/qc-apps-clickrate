package qc.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.xml.DOMConfigurator;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionUtilsTest {
	@BeforeClass
	public static void beforeClass() throws Exception {
		DOMConfigurator.configure(ConnectionUtilsTest.class.getResource("/log4j.xml"));
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sameSession() throws Exception {
		//String url = NetUtils.getTestUrl();
		String url = "http://127.0.0.1:8082/debug.do";
		Map<String, String> cookies = new HashMap<String, String>();
		Document doc = ConnectionUtils.get(url, cookies);
		Assert.assertTrue(cookies.containsKey("JSESSIONID"));
		String sessionId = cookies.get("JSESSIONID");
		Assert.assertNotNull(sessionId);

		// 使用相同session再请求一次
		doc = ConnectionUtils.get(url, cookies);
		Assert.assertTrue(cookies.containsKey("JSESSIONID"));
		Assert.assertEquals(sessionId, cookies.get("JSESSIONID"));
		//System.out.println("==cookies==");
		//System.out.println(debug(cookies));

		boolean success = ConnectionUtils.validate(doc, "body>ul>li:eq(0)",
				sessionId);
		Assert.assertTrue(success);
	}

	@Test
	public void loginToSuccess() throws Exception {
		String loginUrl = "http://127.0.0.1:8082/login.do";
		String toUrl = "http://127.0.0.1:8082/debug.do";
		Map<String, String> loginParams = new HashMap<String, String>();
		loginParams.put("name", "dragon");
		loginParams.put("password", "password");
		boolean success = ConnectionUtils.loginTo(loginUrl, loginParams, toUrl);
		Assert.assertEquals(true, success);
	}

	@Test
	public void loginToFailed() throws Exception {
		String loginUrl = "http://127.0.0.1:8082/login.do";
		String toUrl = "http://127.0.0.1:8082/debug.do";
		Map<String, String> loginParams = new HashMap<String, String>();
		loginParams.put("name", "dragon");
		loginParams.put("password", "xxx");//错误的密码
		boolean success = ConnectionUtils.loginTo(loginUrl, loginParams, toUrl);
		Assert.assertEquals(false, success);
	}

	protected String debug(Map<String, String> map) {
		if (map == null)
			return null;
		Iterator<String> itor = map.keySet().iterator();
		StringBuffer debug = new StringBuffer();
		String key;
		while (itor.hasNext()) {
			key = itor.next();
			debug.append(key + ": " + map.get(key) + "\r\n");
		}
		return debug.toString();
	}
}

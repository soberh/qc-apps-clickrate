package qc.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import qc.net.task.RepeatConnectTask;

public class ConnectionUtils {
	private static final Log logger = LogFactory.getLog(ConnectionUtils.class);

	public static Document get(String url) throws IOException {
		return connect(url, "get", null, NetUtils.getDefaultUserAgent(),
				NetUtils.getDefaultHeaders(), null);
	}

	public static Document get(String url, Map<String, String> cookies)
			throws IOException {
		return connect(url, "get", null, NetUtils.getDefaultUserAgent(),
				NetUtils.getDefaultHeaders(), cookies);
	}

	public static Document get(String url, Map<String, String> params,
			String userAgent, Map<String, String> headers,
			Map<String, String> cookies) throws IOException {
		return connect(url, "get", params, userAgent, headers, cookies);
	}

	public static Document post(String url, Map<String, String> params)
			throws IOException {
		return connect(url, "post", params, NetUtils.getDefaultUserAgent(),
				NetUtils.getDefaultHeaders(), null);
	}

	public static Document post(String url, Map<String, String> params,
			Map<String, String> cookies) throws IOException {
		return connect(url, "post", params, NetUtils.getDefaultUserAgent(),
				NetUtils.getDefaultHeaders(), cookies);
	}

	public static Document post(String url, Map<String, String> params,
			String userAgent, Map<String, String> headers,
			Map<String, String> cookies) throws IOException {
		return connect(url, "post", params, userAgent, headers, cookies);
	}

	public static Document connect(String url, String method,
			Map<String, String> params, String userAgent,
			Map<String, String> headers, Map<String, String> cookies)
			throws IOException {
		Connection con = Jsoup.connect(url);

		// add userAgent
		if (userAgent != null)
			con.userAgent(userAgent);

		// add headers
		if (headers != null) {
			Iterator<String> itor = headers.keySet().iterator();
			String name;
			while (itor.hasNext()) {
				name = itor.next();
				con.header(name, headers.get(name));
			}
		}

		// add data
		if (params != null)
			con.data(params);

		// set method
		if ("post".equalsIgnoreCase(method)) {
			con.method(Method.POST);
		} else {
			con.method(Method.GET);
		}

		// add cookie
		if (cookies != null) {
			Iterator<Entry<String, String>> itor = cookies.entrySet()
					.iterator();
			Entry<String, String> entry;
			while (itor.hasNext()) {
				entry = itor.next();
				con.cookie(entry.getKey(), entry.getValue());
			}
		}

		// connect
		Response response = con.execute();
		Document doc = response.parse();
		if (cookies != null) {
			cookies.putAll(response.cookies());
		}
		if (logger.isDebugEnabled()) {
			logger.debug(doc.html());
		}
		return doc;
	}

	/**
	 * 使用选择器验证获取文档的正确性
	 * 
	 * @param doc
	 * @param selector
	 * @param containsValue
	 *            应该包含的字符串
	 * @return
	 */
	public static boolean validate(Document doc, String selector,
			String containsValue) {
		if (doc == null || selector == null)
			return false;

		String html = doc.select(selector).html();
		if (html != null) {
			if (logger.isDebugEnabled())
				logger.debug("selector's html=" + html);
			return html.contains(containsValue);
		} else {
			return false;
		}
	}

	public static boolean loginTo(String loginUrl,
			Map<String, String> loginParams, String toUrl) throws Exception {
		// 登录
		Map<String, String> cookies = new HashMap<String, String>();
		post(loginUrl, loginParams, cookies);
		String sessionId1 = cookies.get("JSESSIONID");
		if (logger.isDebugEnabled()) {
			logger.debug("sessionId1=" + sessionId1);
		}

		// 使用同一session跳到toUrl
		get(toUrl, cookies);
		String sessionId2 = cookies.get("JSESSIONID");
		boolean success = sessionId1 != null && sessionId1.equals(sessionId2);
		if (logger.isDebugEnabled()) {
			logger.debug("sessionId2=" + sessionId2);
			logger.debug("success=" + success);
		}

		return success;
	}

	/**
	 * 重复连接指定的url
	 * 
	 * @param num
	 *            连接次数
	 * @param interval
	 *            每次连接间的时间间隔(毫秒)
	 * @param url
	 *            要连接的网站
	 */
	public static void repeatGet(int num, int interval, String url) {
		Timer timer = new Timer();
		RepeatConnectTask task = new RepeatConnectTask(url, num);
		timer.schedule(task, 0, interval);
	}

	public static void repeatGet(int num, int interval, String url,
			Map<String, String> cookies) {
		Timer timer = new Timer();
		RepeatConnectTask task = new RepeatConnectTask(url, num);
		task.setCookies(cookies);
		timer.schedule(task, 0, interval);
	}
}

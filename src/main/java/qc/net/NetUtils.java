package qc.net;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dragon
 * 
 */
public class NetUtils {
	private static final Map<String, String> userAgents;
	static {
		userAgents = new HashMap<String, String>();
		userAgents
				.put("chrome8",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.224 Safari/534.10");
		userAgents.put("ie6",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
	}

	public static String getTestUrl() {
		return "http://127.0.0.1:8081/qcdebug/debug.do";
	}

	public static String getDefaultUserAgent() {
		return userAgents.get("ie6");
	}

	public static String getUserAgent(String key) {
		return userAgents.get(key);
	}

	public static Map<String, String> getDefaultHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers
				.put(
						"Accept",
						"application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		headers.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		headers.put("Accept-Encoding", "gzip,deflate,sdch");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		// headers("Host", "rongjih.blog.163.com");
		// headers.put("Referer","http://rongjih.blog.163.com/blog/static/335744612010112041023772/");

		return headers;
	}

}

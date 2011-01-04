package qc.net.service.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import qc.net.NetUtils;

/**
 * 从web获取可用代理
 * 
 * @author dragon
 * 
 */
public class ProxyConfigFinder {
	private static final Log logger = LogFactory.getLog(ProxyConfigFinder.class);
	
	/**
	 * @param url 获取代理列表的url，返回一个html文档
	 * @param selector html选择器
	 * @param builder ProxyConfigParser
	 * @return
	 */
	public static List<ProxyConfig> find(String url, String selector,
			ProxyConfigParser builder) {
		Connection con = Jsoup.connect(url);
		con.method(Method.GET);

		// add userAgent
		con.userAgent(NetUtils.getUserAgent("firefox3"));

		// add headers
		Map<String, String> headers = NetUtils.getDefaultHeaders();
		Iterator<String> itor = headers.keySet().iterator();
		String name;
		while (itor.hasNext()) {
			name = itor.next();
			con.header(name, headers.get(name));
		}

		Document doc;
		List<ProxyConfig> list = new ArrayList<ProxyConfig>();
		ProxyConfig cfg;
		try {
			doc = con.get();
		} catch (IOException e1) {
			logger.error(e1.getMessage(),e1);
			return list;
		}
		Elements es = doc.select(selector);
		for (Element e : es) {
			cfg = builder.parse(e);
			if (cfg != null)
				list.add(cfg);
		}

		return list;
	}
}

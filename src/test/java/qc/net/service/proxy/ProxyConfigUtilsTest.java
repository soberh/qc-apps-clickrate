package qc.net.service.proxy;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import qc.apps.clickrate.Config;
import qc.net.service.proxy.impl.ProxyConfigParser4ProxyCN;

public class ProxyConfigUtilsTest {
	@Before
	public void setUp() throws Exception {
		Config.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findProxyFromProxyCN() throws Exception {
		String url = "http://www.proxycn.com/html_proxy/30fastproxy-1.html";
		String selector = "tr[bgcolor=#fbfbfb]";
		List<ProxyConfig> list = ProxyConfigFinder.find(url, selector,
				new ProxyConfigParser4ProxyCN());
		Assert.assertEquals(50, list.size());
		// System.out.println(StringUtils
		// .collectionToDelimitedString(list, "\r\n"));
	}

	@Test
	public void findFromConfigFile() throws Exception {
		String url = Config.getValue("app.proxy_finder.url");
		String parser = Config.getValue("app.proxy_finder.parser");
		String selector = Config.getValue("app.proxy_finder.selector");

		Assert.assertEquals(
				"http://www.proxycn.com/html_proxy/30fastproxy-1.html", url);
		Assert.assertEquals("tr[bgcolor=#fbfbfb]", selector);
		Assert.assertEquals(
				"qc.net.service.proxy.impl.ProxyConfigParser4ProxyCN", parser);

		ProxyConfigParser proxyParser = (ProxyConfigParser) Class.forName(
				parser).newInstance();
		List<ProxyConfig> list = ProxyConfigFinder.find(url, selector,
				proxyParser);
		Assert.assertEquals(50, list.size());
	}
}

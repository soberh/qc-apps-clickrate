package qc.net.service.proxy.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import qc.apps.clickrate.Config;
import qc.net.service.proxy.ProxyConfig;
import qc.net.service.proxy.ProxyConfigFinder;
import qc.net.service.proxy.ProxyConfigParser;
import qc.net.service.proxy.ProxyConfigService;

public class ProxyConfigServiceImplTest {
	ProxyConfigService proxyConfigService;
	List<ProxyConfig> proxies;
	@Before
	public void before() throws Exception {
		Config.init();

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
		proxies = ProxyConfigFinder.find(url, selector,
				proxyParser);
		Assert.assertEquals(50, proxies.size());
		
		proxyConfigService = new ProxyConfigServiceImpl();
	}

	@Test
	public void save() throws Exception {
		proxyConfigService.save(proxies);
	}
}

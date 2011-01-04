package qc.net.httpclient4;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import qc.apps.clickrate.Config;
import qc.net.service.NetService;
import qc.net.service.NetService.Method;
import qc.net.service.impl.httpclient4.NetServiceImpl;
import qc.net.service.proxy.ProxyConfig;
import qc.net.service.proxy.ProxyConfigService;
import qc.net.service.proxy.impl.ProxyConfigServiceImpl;

/**
 * @author dragon
 * 
 */
public class NetServiceImplTest {
	private static final Log logger = LogFactory
			.getLog(NetServiceImplTest.class);
	ProxyConfigService proxyConfigService;
	NetService netService;

	@Before
	public void setUp() throws Exception {
		Config.init();
		proxyConfigService = new ProxyConfigServiceImpl();
		netService = new NetServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void connectSpeedTest() {
		String url = "http://rongjih.blog.163.com/blog/static/335744612010112041023772/";
		List<ProxyConfig> cfgs = proxyConfigService.find();
		int i = 0;
		for (ProxyConfig cfg : cfgs) {
			logger.info(i + ")" + netService.proxySpeedTest(cfg, url, Method.GET, 2000));
			i++;
			//if (i > 3)
			//	break;
		}
	}
}

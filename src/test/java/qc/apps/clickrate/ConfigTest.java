package qc.apps.clickrate;

import junit.framework.Assert;

import org.junit.Test;

public class ConfigTest {
	@Test
	public void loadProperties() {
		Config.initAppConfig();
		Assert.assertEquals("qc.net.service.proxy.impl.ProxyConfigParser4ProxyCN",
				Config.getValue("app.proxy_finder.parser"));
	}
}

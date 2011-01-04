package qc.apps.clickrate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import qc.net.service.impl.httpclient4.NetServiceImpl;
import qc.net.service.proxy.impl.ProxyConfigServiceImpl;

public class GUIMain {
	private static final Log logger = LogFactory.getLog(GUIMain.class);

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Config.init();
			ClickRate1 window = new ClickRate1();
			window.setNetService(new NetServiceImpl());
			window.setProxyConfigService(new ProxyConfigServiceImpl());
			window.open();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}

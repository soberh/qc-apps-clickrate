package qc.apps.clickrate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import qc.net.service.NetService;
import qc.net.service.impl.jsoup.NetServiceImpl;

public class GUIMain {
	private static final Log logger = LogFactory.getLog(GUIMain.class);

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//Config.initLoger();
		try {
			ClickRate1 window = new ClickRate1();
			NetService netService = new NetServiceImpl();
			window.setNetService(netService);
			window.open();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

}

package qc.apps.clickrate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.swt.widgets.Text;

import qc.log.impl.log4j.EclipseSWTTextAppender;
import qc.net.service.proxy.ProxyConfigService;
import qc.net.service.proxy.impl.ProxyConfigServiceImpl;

public class Config {
	private static final Log logger = LogFactory.getLog(Config.class);
	private static boolean init = false;
	public static String CONFIG_PATH = Config.getPath() + File.separator
			+ "support" + File.separator;

	public static void init() {
		initLoger();
		initAppConfig();
	}

	public static void initLoger(Text comp) {
		if (!init)
			initLoger();

		logger.fatal("设置日志的桌面显示: " + (comp == null ? "null" : comp.getClass()));
		EclipseSWTTextAppender guiAppender = (EclipseSWTTextAppender) Logger
				.getRootLogger().getAppender("gui");
		if (guiAppender != null)
			guiAppender.setControl(comp);
		logger.fatal("准备完毕！");
	}

	public static void initLoger() {
		// 配置log4j
		// DOMConfigurator.configure(Config.class.getResource("/log4j.xml"));
		String log4j = CONFIG_PATH + "log4j.xml";
		DOMConfigurator.configure(log4j);
		init = true;
		logger.fatal("加载日志配置文件: " + log4j);
	}

	// 获取程序当前的运行目录的绝对路径
	public static String getPath() {
		try {
			return new File("").getCanonicalPath();
		} catch (IOException e) {
			return System.getProperty("user.dir");
		}
	}

	public static void initAppConfig() {
		try {
			FileInputStream in = new FileInputStream(CONFIG_PATH + "app.xml");
			appProperties.loadFromXML(in);
			in.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static Properties appProperties = new Properties();

	public static Properties getAppProperties() {
		return appProperties;
	}

	/**
	 * 获取所配置的资源值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		return appProperties.getProperty(key);
	}
}

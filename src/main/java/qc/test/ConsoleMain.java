package qc.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import qc.apps.clickrate.Config;

public class ConsoleMain {
	private static final Log logger = LogFactory.getLog(ConsoleMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Config.init();

			// args[0] -- 要访问的url
			// args[1] -- 循环次数
			// args[2] -- 循环间隔，默认为秒，可以通过附加单位s、m、h区分秒、分钟、小时
			if (args == null || args.length == 0)
				throw new RuntimeException("必须指定参数：" + "\r\n    [0]-要访问的url"
						+ "\r\n    [1]-[可选配置]循环次数，默认为1"
						+ "\r\n    [2]-[可选配置]循环间隔，默认为5秒，可以通过附加单位s、m、h区分秒、分钟、小时");
			String[] _urls = args[0].split(";");
			int num, interval;
			if (args.length > 1)
				num = Integer.parseInt(args[1]);
			else
				num = 1;

			if (args.length > 2)
				interval = getInterval(args[2]);
			else
				interval = 5000;

			if (_urls.length == 1) {
				logger.info("start repeat...");
				ConnectionUtils.repeatGet(num, interval, _urls[0]);
			} else if (_urls.length == 2) {// 先登录后获取的方式
				String[] url1 = _urls[0].split(",");// 第一个url格式：http://ip:port/path,key1=value1&key2=value2&...

				Map<String, String> params;
				if (url1.length == 2) {
					params = new HashMap<String, String>();
					String[] kvs = url1[1].split("&");
					String[] kv;
					for (String _kv : kvs) {
						kv = _kv.split("=");
						params.put(kv[0], kv[1]);
					}
				} else {
					params = null;
				}

				Map<String, String> cookies = new HashMap<String, String>();
				logger.info("post:" + url1[0]);
				logger.info("    params:" + params);
				ConnectionUtils.post(url1[0], params, cookies);
				logger.info("    cookies:" + cookies);
				logger.info("start repeat...");
				ConnectionUtils.repeatGet(num, interval, _urls[1], cookies);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	// 解析带单位的值为毫秒值
	private static int getInterval(String interval) {
		if (interval == null || interval.length() == 0)
			return 5000;// 默认5秒

		if (interval.endsWith("m")) {// 分钟
			return getNumber(interval) * 60 * 1000;
		} else if (interval.endsWith("h")) {// 小时
			return getNumber(interval) * 60 * 60 * 1000;
		} else if (interval.endsWith("ms")) {// 毫秒
			return Integer.parseInt(interval
					.substring(0, interval.length() - 2));
		} else if (interval.endsWith("s")) {// 秒
			return getNumber(interval) * 1000;
		} else {// 秒
			return Integer.parseInt(interval) * 1000;
		}
	}

	private static int getNumber(String interval) {
		return Integer.parseInt(interval.substring(0, interval.length() - 1));
	}
}

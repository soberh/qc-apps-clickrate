package qc.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import qc.net.task.GetOuterNetIPTask;

public class OuterNetIPMain {
	private static final Log logger = LogFactory.getLog(OuterNetIPMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// 配置log4j
			DOMConfigurator.configure(OuterNetIPMain.class
					.getResource("/log4j.xml"));

			// args[0] -- 循环次数
			// args[1] -- 循环间隔，默认为秒，可以通过附加单位s、m、h区分秒、分钟、小时
			if (args == null || args.length == 0)
				throw new RuntimeException("必须指定参数："
						+ "\r\n    [0]-[可选配置]循环次数，默认为1"
						+ "\r\n    [1]-[可选配置]循环间隔，默认为5秒，可以通过附加单位s、m、h区分秒、分钟、小时");
			int num, interval;
			boolean useSameSession;
			if (args.length > 0)
				num = Integer.parseInt(args[0]);
			else
				num = 1;

			if (args.length > 1)
				interval = getInterval(args[1]);
			else
				interval = 5000;

			if (args.length > 2)
				useSameSession = Boolean.parseBoolean(args[2]);
			else
				useSameSession = true;

			String defaultUrl = "http://www.ip138.com/ip2city.asp";
			String defaultSelector = "center";
			Timer timer = new Timer();
			GetOuterNetIPTask task = new GetOuterNetIPTask(defaultUrl, num);
			Map<String, String> cookies = new HashMap<String, String>();
			if(useSameSession)task.setCookies(cookies);
			task.setSelector(defaultSelector);
			timer.schedule(task, 0, interval);
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

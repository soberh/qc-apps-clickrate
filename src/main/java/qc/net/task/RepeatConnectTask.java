package qc.net.task;

import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import qc.net.ConnectionUtils;

public class RepeatConnectTask extends TimerTask {
	private static final Log logger = LogFactory.getLog(RepeatConnectTask.class);
	private long currentCount = 0;
	private long maxCount = 1;
	private String url;
	private String method;
	private Map<String, String> params;
	private Map<String, String> cookies;

	public RepeatConnectTask(String url, long maxCount) {
		if (url == null || url.length() == 0)
			throw new RuntimeException("url cannot be null or empty!");
		this.url = url;
		this.maxCount = Math.max(1, maxCount);
	}

	public void setPostParams(Map<String, String> params) {
		this.method = "post";
		this.params = params;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	@Override
	public void run() {
		try {
			currentCount++;
			if (logger.isInfoEnabled())
				logger
						.info("currentCount=" + currentCount + ";url="
								+ this.url);

			if ("post".equalsIgnoreCase(method)) {
				ConnectionUtils.post(url, params, cookies);
			} else {
				ConnectionUtils.get(url, cookies);
			}

			if (maxCount <= currentCount) {
				this.cancel();
				if (logger.isInfoEnabled())
					logger.info("timer stopped!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}

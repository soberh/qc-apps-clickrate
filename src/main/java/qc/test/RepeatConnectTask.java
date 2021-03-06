package qc.test;

import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;


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

	protected String getUrl(String url, long count) {
		return url;
	}

	@Override
	public void run() {
		try {
			currentCount++;
			String url = getUrl(this.url, currentCount);
			if (logger.isInfoEnabled())
				logger
						.info("currentCount=" + currentCount + ";url="
								+ url);
			Document doc;
			if ("post".equalsIgnoreCase(method)) {
				doc = ConnectionUtils.post(url, params, cookies);
			} else {
				doc = ConnectionUtils.get(url, cookies);
			}
			
			afterRun(doc);

			if (maxCount <= currentCount) {
				this.cancel();
				if (logger.isInfoEnabled())
					logger.info("timer stopped!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected void afterRun(Document doc) {
		// do nothing
	}
}

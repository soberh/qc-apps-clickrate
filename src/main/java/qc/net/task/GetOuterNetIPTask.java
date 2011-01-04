package qc.net.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;

public class GetOuterNetIPTask extends RepeatConnectTask {
	private static final Log logger = LogFactory
			.getLog(GetOuterNetIPTask.class);
	private String selector;
	private Map<String, String> ips = new HashMap<String, String>();
	private String html;

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public GetOuterNetIPTask(String url, long maxCount) {
		super(url, maxCount);
	}

	@Override
	protected String getUrl(String url, long count) {
		return super.getUrl(url, count) + "?c=" + count;
	}

	@Override
	public boolean cancel() {
		boolean ok = super.cancel();
		logger.warn("all[" + ips.size() + "] " + this.ips.keySet());
		System.exit(0);
		return ok;
	}

	@Override
	protected void afterRun(Document doc) {
		if (this.selector != null) {
			this.html = format(doc.select(this.selector).html());
			if(this.html != null) {
				if(!ips.containsKey(this.html)) {
					ips.put(this.html, this.html);
					logger.warn("[" + ips.size() + "] " + this.html);
				}else{
					logger.info("repeat: " + this.html);
				}
			}else{
				logger.error("error");
			}
		}
	}

	private String format(String html) {
		if (html == null)
			return null;
		return html.trim().replace("您的IP地址是：[", "").replace("]", "")
				.replaceAll("\r|\n", "");
	}
}

package qc.net.service.impl.jsoup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import qc.net.NetUtils;
import qc.net.service.Callback;
import qc.net.service.NetService;
import qc.net.service.ResponseValidator;

public class NetServiceImpl implements NetService {
	private static final Log logger = LogFactory.getLog(NetServiceImpl.class);
	Map<String, String> _headers = new HashMap<String, String>();
	private ResponseValidator validator;

	public void setValidator(ResponseValidator validator) {
		this.validator = validator;
	}

	public boolean connect(Method method, String url,
			Map<String, String> headers, Map<String, String> sid) {
		Connection con = Jsoup.connect(url);

		// add userAgent
		con.userAgent(NetUtils.getDefaultUserAgent());

		// add headers
		Map<String, String> _headers = new HashMap<String, String>();
		if (headers != null) {
			_headers.putAll(headers);
		}
		Iterator<String> itor = _headers.keySet().iterator();
		String name;
		while (itor.hasNext()) {
			name = itor.next();
			con.header(name, _headers.get(name));
		}

		// set method
		if (method == NetService.Method.POST) {
			con.method(Connection.Method.POST);
		} else {
			con.method(Connection.Method.GET);
		}

		// add cookie
		if (sid != null) {
			Iterator<Entry<String, String>> itor1 = sid.entrySet().iterator();
			Entry<String, String> entry;
			while (itor1.hasNext()) {
				entry = itor1.next();
				con.cookie(entry.getKey(), entry.getValue());
			}
		}

		try {
			// connect
			Response response = con.execute();
			if (sid != null)
				sid.putAll(response.cookies());

			if (logger.isDebugEnabled()) {
				logger.debug(response.body());
				// Document doc = response.parse();
				// logger.debug(doc.html());
			}

			// validate
			if (response.statusCode() != HttpURLConnection.HTTP_OK) {
				if (logger.isInfoEnabled())
					logger.info("statusCode=" + response.statusCode());
				return false;
			} else {
				if (this.validator != null) {
					return this.validator.validate(response);
				} else {
					return true;
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	// 记录已启动的定时器
	Map<String, Timer> timers = new HashMap<String, Timer>();

	public void repeat(final String id, final int num, int interval,
			final Method method, final String url,
			final Map<String, String> headers, final Map<String, String> sid,
			final Callback callback) {
		if (logger.isDebugEnabled()) {
			logger.warn("id=" + id);
			logger.warn("num=" + num);
			logger.warn("interval=" + interval);
			logger.warn("method=" + method);
			logger.warn("url=" + url);
		}
		Timer timer = new Timer();
		timers.put(id, timer);
		timer.schedule(new TimerTask() {
			int cur = 0;

			@Override
			public void run() {
				try {
					cur++;
					if (logger.isDebugEnabled())
						logger.debug("开始第" + cur + "次连接: " + url);
					boolean success = NetServiceImpl.this.connect(method, url,
							headers, sid);

					String msg = "第" + cur + "次连接"
							+ ((num <= cur) ? "(最后一次)" : "") + "："
							+ (success ? "成功" : "失败");
					logger.warn(msg);
					if (num <= cur) {
						if (logger.isWarnEnabled())
							logger.warn("定时器循环完毕!");
						this.cancel();
						stopRepeat(id);
						if (callback != null){
							callback.call(-1);
						}
					}else{
						if (callback != null){
							callback.call(cur);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

		}, 0, interval);
	}

	public void stopRepeat(String id) {
		Timer timer = timers.get(id);
		if (timer == null)
			logger.warn("定时器没有启动!");

		timer.cancel();
		timers.remove(id);
		logger.warn("定时器已被取消!");
	}

	public void stopAllRepeat() {
		//释放定时器的线程资源
		for(Entry<String,Timer> entry : timers.entrySet()){
			entry.getValue().cancel();
		}
	}
}

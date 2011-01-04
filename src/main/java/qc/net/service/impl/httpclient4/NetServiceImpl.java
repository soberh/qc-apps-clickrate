package qc.net.service.impl.httpclient4;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import qc.net.NetUtils;
import qc.net.service.Callback;
import qc.net.service.NetService;
import qc.net.service.ResponseValidator;
import qc.net.service.proxy.ProxyConfig;

public class NetServiceImpl implements NetService {
	private static final Log logger = LogFactory.getLog(NetServiceImpl.class);
	Map<String, String> _headers = new HashMap<String, String>();
	private ResponseValidator validator;

	public void setValidator(ResponseValidator validator) {
		this.validator = validator;
	}

	public boolean connect(Method method, String url,
			Map<String, String> headers, Map<String, String> sid) {
		return this.connect(method, url, headers, sid, null);
	}

	public boolean connect(Method method, String url,
			Map<String, String> headers, Map<String, String> sid,
			ProxyConfig proxyConfig) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpHost proxy;
		if (proxyConfig != null) {
			// proxy = new HttpHost("203.165.28.106", 8080);
			proxy = new HttpHost(proxyConfig.getIp(), proxyConfig.getPort());
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		HttpUriRequest request;

		// set method
		if (method == NetService.Method.POST) {
			request = new HttpGet(url);
		} else {
			request = new HttpPost(url);
		}

		// add headers
		Iterator<String> itor = headers.keySet().iterator();
		String name;
		while (itor.hasNext()) {
			name = itor.next();
			request.addHeader(name, headers.get(name));
		}

		// add userAgent
		request.addHeader("User-Agent", NetUtils.getUserAgent("ie6"));

		// TODO add cookie
		// CookieStore CookieStore = httpClient.getCookieStore();
		// if (sid != null) {
		// Iterator<Entry<String, String>> itor1 = sid.entrySet().iterator();
		// Entry<String, String> entry;
		// while (itor1.hasNext()) {
		// entry = itor1.next();
		// con.cookie(entry.getKey(), entry.getValue());
		// }
		// }

		try {
			// connect
			HttpResponse response = httpClient.execute(request);
			// if (sid != null)
			// sid.putAll(response.cookies());

			if (logger.isDebugEnabled()) {
				logger.debug(EntityUtils.toString(response.getEntity()));// ,"UTF-8"));
			}

			// validate
			if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
				if (logger.isInfoEnabled())
					logger.info("statusCode="
							+ response.getStatusLine().getStatusCode());
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

	public int proxySpeedTest(ProxyConfig proxyConfig, String url,
			Method method, int timeout) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpHost proxy;
		if (proxyConfig != null && !"127.0.0.1".equals(proxyConfig.getIp())) {
			proxy = new HttpHost(proxyConfig.getIp(), proxyConfig.getPort());
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		// 设置超时
		if (timeout > 0)
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, new Integer(timeout));
		
		HttpUriRequest request;

		// set method
		if (method == NetService.Method.POST) {
			request = new HttpGet(url);
		} else {
			request = new HttpPost(url);
		}

		// add headers
		Map<String, String> headers = NetUtils.getDefaultHeaders();
		Iterator<String> itor = headers.keySet().iterator();
		String name;
		while (itor.hasNext()) {
			name = itor.next();
			request.addHeader(name, headers.get(name));
		}

		// add userAgent
		request.addHeader("User-Agent", NetUtils.getUserAgent("ie6"));

		int ms;
		try {
			long start = new Date().getTime();
			HttpResponse response = httpClient.execute(request);

			if (logger.isDebugEnabled()) {
				logger.debug(EntityUtils.toString(response.getEntity()));// ,"UTF-8"));
			}

			// validate
			if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
				if (logger.isInfoEnabled())
					logger.info("statusCode="
							+ response.getStatusLine().getStatusCode());
				ms = -2;
			} else {
				long end = new Date().getTime();
				ms = (int) (end - start);
			}
		} catch (ClientProtocolException e) {
			logger.warn(e.getMessage());
			ms = -1;
		} catch (ParseException e) {
			logger.warn(e.getMessage());
			ms = -1;
		} catch (IOException e) {
			logger.warn(e.getMessage());
			ms = -1;
		}

		return ms;
	}

	// 记录已启动的定时器
	Map<String, Timer> timers = new HashMap<String, Timer>();

	public void repeat(final String id, final int num, int interval,
			final Method method, final String url,
			final Map<String, String> headers, final Map<String, String> sid,
			final Callback callback) {
		this.repeat(id, num, interval, method, url, headers, sid, callback,
				null);
	}

	public void repeat(final String id, final int num, int interval,
			final Method method, final String url,
			final Map<String, String> headers, final Map<String, String> sid,
			final Callback callback, final List<ProxyConfig> proxyConfigs) {
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
			boolean useProxy = (proxyConfigs != null && !proxyConfigs.isEmpty());
			int proxyCount = useProxy ? proxyConfigs.size() : 0;
			ProxyConfig proxyConfig;

			@Override
			public void run() {
				try {
					cur++;
					if (logger.isDebugEnabled())
						logger.debug("开始第" + cur + "次连接: " + url);
					boolean success;
					if (useProxy) {
						proxyConfig = proxyConfigs.get((cur - 1) % proxyCount);
						success = NetServiceImpl.this.connect(method, url,
								headers, sid, proxyConfig);
					} else {
						success = NetServiceImpl.this.connect(method, url,
								headers, sid);
					}

					String msg = "第" + cur + "次连接"
							+ ((num <= cur) ? "(最后一次)" : "") + "："
							+ (success ? "成功" : "失败");
					if (useProxy) {
						msg += "(使用代理" + proxyConfig.getIp() + ":"
								+ proxyConfig.getPort() + ")";
					}
					logger.warn(msg);
					if (num <= cur) {
						if (logger.isWarnEnabled())
							logger.warn("定时器循环完毕!");
						this.cancel();
						stopRepeat(id);
						if (callback != null) {
							callback.call(-1);
						}
					} else {
						if (callback != null) {
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
		// 释放定时器的线程资源
		for (Entry<String, Timer> entry : timers.entrySet()) {
			entry.getValue().cancel();
		}
	}
}

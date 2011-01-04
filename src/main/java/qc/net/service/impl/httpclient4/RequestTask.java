/**
 * 
 */
package qc.net.service.impl.httpclient4;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import qc.net.NetUtils;
import qc.net.service.NetService;
import qc.net.service.NetService.Method;
import qc.net.service.proxy.ProxyConfig;

/**
 * @author dragon
 * 
 */
public class RequestTask implements Callable<ThreadResult> {
	private static final Log logger = LogFactory.getLog(RequestTask.class);
	private String id;
	private String url;
	private int timeout;
	private Method method;
	private ProxyConfig proxyConfig;
	private Map<String, Object> params;

	public RequestTask(String id, String url, Method method,
			Map<String, Object> params, ProxyConfig proxyConfig) {
		this.id = id;
		this.url = url;
		this.method = method;
		this.params = params;
		this.proxyConfig = proxyConfig;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public ThreadResult call() throws Exception {
		String msg = id + " 连接";
		String p = (proxyConfig != null ? "(" + proxyConfig.label() + ")" : "");

		long start = new Date().getTime();
		// 初始化
		HttpClient httpClient = new DefaultHttpClient();

		// 设置请求参数
		if (params != null) {
			for (Entry<String, Object> entry : params.entrySet()) {
				httpClient.getParams().setParameter(entry.getKey(),
						entry.getValue());
			}
		}

		// 设置的代理
		if (proxyConfig != null) {
			HttpHost proxy = new HttpHost(proxyConfig.getIp(),
					proxyConfig.getPort());
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		if (timeout > 0) {
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT,
					new Integer(timeout));
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, new Integer(timeout));
		}

		// 设置请求的方法
		HttpUriRequest request;
		if (method == NetService.Method.POST) {
			request = new HttpPost(url);
		} else {
			request = new HttpGet(url);
		}

		// add userAgent
		request.addHeader("User-Agent", NetUtils.getUserAgent("ie6"));

		HttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (HttpHostConnectException e) {
			int ms = (int) (new Date().getTime() - start);
			msg += "失败" + p + ",耗时" + ms + "毫秒,code=-1";
			return new ThreadResult(false, -1, ms, p);
		}
		HttpEntity entity = response.getEntity();

		if (logger.isInfoEnabled()) {
			if (entity != null) {
				logger.info(id + " url：" + request.getURI());
				logger.info(id + " " + response.getStatusLine().getStatusCode()
						+ "：");
				for (HeaderIterator itor = response.headerIterator(); itor
						.hasNext();) {
					logger.info(id + "   " + itor.next());
				}

				// 保证连接能释放回管理器
				entity.consumeContent();
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(id + " 内容：" + EntityUtils.toString(entity));
		}

		int ms = (int) (new Date().getTime() - start);
		boolean success;
		if (response.getStatusLine().getStatusCode() == 200) {
			msg += "成功" + p + ",耗时" + ms + "毫秒";
			success = true;
		} else {
			msg += "失败" + p + ",耗时" + ms + "毫秒,code="
					+ response.getStatusLine().getStatusCode();
			success = false;
		}
		logger.warn(msg);

		return new ThreadResult(success, response.getStatusLine()
				.getStatusCode(), ms, p);
	}

}

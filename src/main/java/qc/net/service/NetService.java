package qc.net.service;

import java.util.List;
import java.util.Map;

import qc.net.service.proxy.ProxyConfig;

/**
 * 网络连接接口
 * 
 * @author dragon
 * 
 */
public interface NetService {
	/**
	 * 连接方法
	 * 
	 * @author dragon
	 * 
	 */
	public enum Method {
		GET, POST
	}

	/**
	 * 连接指定的网址
	 * 
	 * @param url
	 *            要连接的网址
	 * @param headers
	 *            附加的头信息
	 * @param sid
	 *            该次连接返回的session标识数据，如cookies，可以用于下次连接使用从而保持session
	 *            为空则代表无状态连接，每次连接都是不同的session
	 * @return 连接成功返回true(响应代码为200)，否则返回false
	 */
	boolean connect(Method method, String url, Map<String, String> headers,
			Map<String, String> sid);

	boolean connect(Method method, String url, Map<String, String> headers,
			Map<String, String> sid, ProxyConfig proxyConfig);

	/**
	 * 重复连接指定的网址
	 * 
	 * @param id
	 *            用于取消该次重复连接的唯一标识id
	 * @param num
	 *            连接次数
	 * @param interval
	 *            连接的时间间隔，单位毫秒
	 * @param method
	 * @param url
	 * @param headers
	 * @param sid
	 */
	void repeat(String id, int num, int interval, Method method, String url,
			Map<String, String> headers, Map<String, String> sid,
			Callback<Integer> callback);

	void repeat(String id, int num, int interval, Method method, String url,
			Map<String, String> headers, Map<String, String> sid,
			Callback<Integer> callback, List<ProxyConfig> proxyConfigs);

	/**
	 * 使用多线程并发访问
	 * 
	 * @param id
	 * @param num
	 *            连接次数
	 * @param method
	 *            请求的方法：get|post
	 * @param url
	 *            要访问的网址
	 * @param params
	 *            请求附加的参数
	 * @param proxyConfigs
	 *            可用代理
	 * @param callback
	 */
	void multithreaded(String id, int num, Method method, String url,
			Map<String, Object> params, List<ProxyConfig> proxyConfigs,
			Callback<int[]> callback);
	
	void stopMulti();

	/**
	 * 终止指定的重复连接
	 * 
	 * @param id
	 */
	void stopRepeat(String id);

	void stopAllRepeat();

	/**
	 * 代理连接速度测试
	 * 
	 * @param proxy
	 * @param url
	 * @param method
	 * @param timeout
	 *            超时控制
	 * @return 连接耗时
	 */
	int proxySpeedTest(ProxyConfig proxy, String url, Method method, int timeout);
}

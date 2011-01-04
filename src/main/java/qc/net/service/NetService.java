package qc.net.service;

import java.util.Map;

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
			Callback callback);

	/**
	 * 终止指定的重复连接
	 * 
	 * @param id
	 */
	void stopRepeat(String id);

	void stopAllRepeat();
}

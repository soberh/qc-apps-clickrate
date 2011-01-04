package qc.net.service.proxy;

import java.util.List;

/**
 * 代理服务
 * 
 * @author dragon
 * 
 */
public interface ProxyConfigService {
	/**
	 * 获取所有配置的代理
	 * @return
	 */
	List<ProxyConfig> find();

	/**
	 * 获取所配置的代理
	 * @return
	 */
	ProxyConfig load(String ip);

	/**
	 * 保存代理配置
	 * @param proxies
	 */
	void save(List<ProxyConfig> proxies);
}

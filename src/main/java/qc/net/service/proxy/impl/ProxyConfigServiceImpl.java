package qc.net.service.proxy.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import qc.apps.clickrate.Config;
import qc.net.service.proxy.ProxyConfig;
import qc.net.service.proxy.ProxyConfigService;

import com.google.gson.Gson;

public class ProxyConfigServiceImpl implements ProxyConfigService {
	private static final Log logger = LogFactory
			.getLog(ProxyConfigServiceImpl.class);

	private Properties proxies = new Properties();
	private Map<String, ProxyConfig> all= new HashMap<String, ProxyConfig>();
	private boolean hadInit = false;
	private Gson gson = new Gson();
	private String file = Config.CONFIG_PATH + "proxies.xml";

	protected void init() {
		try {
			File _file = new File(file);
			if (_file.exists()) {
				logger.debug("从'" + file + "'加载代理配置");
				FileInputStream in = new FileInputStream(_file);
				proxies.loadFromXML(in);
				in.close();

				ProxyConfig cfg;
				String key, json;
				for (Entry<Object, Object> entry : proxies.entrySet()) {
					key = (String) entry.getKey();
					json = (String) entry.getValue();
					if (key == null || key.trim().length() == 0)
						throw new IllegalArgumentException(
								"key counot be null or empty.");
					if (json == null || json.trim().length() == 0)
						throw new IllegalArgumentException(
								"json counot be null or empty.");
					cfg = gson.fromJson(json, ProxyConfig.class);
					all.put(key, cfg);
				}
				logger.warn("成功加载" + all.size() + "个代理配置");
				logger.debug("    size=" + all.size() + "|" + proxies.size());
			} else {
				logger.warn("没有配置代理: " + file);
			}
			hadInit = true;
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public ProxyConfig load(String ip) {
		if (!hadInit)
			init();

		return all.get(ip);
	}

	public void save(List<ProxyConfig> proxies) {
		//if (!hadInit) init();
		this.proxies.clear();
		this.all.clear();
		for (ProxyConfig cfg : proxies) {
			all.put(cfg.getIp(), cfg);

			this.proxies.setProperty(cfg.getIp(), gson.toJson(cfg));
			this.all.put(cfg.getIp(), cfg);
		}

		// 保存到文件
		try {
			FileOutputStream os = new FileOutputStream(file);
			logger.warn("保存" + all.size() + "个代理配置到: " + file);
			this.proxies.storeToXML(os, all.size() + "", "UTF-8");
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public List<ProxyConfig> find() {
		if (!hadInit)
			init();

		List<ProxyConfig> cfgs = new ArrayList<ProxyConfig>();
		for (Entry<String, ProxyConfig> e : all.entrySet()) {
			cfgs.add(e.getValue());
		}

		return cfgs;
	}
}

package qc.net.service.proxy;

import java.io.Serializable;

public class ProxyConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ip;
	private int port = -1;
	private String type;
	private String desc;
	private int ms;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getMs() {
		return ms;
	}

	public void setMs(int ms) {
		this.ms = ms;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("{");
		s.append("ip:" + (ip != null ? ip : "null"));
		s.append(",port:" + port);
		s.append(",type:" + (type != null ? type : "null"));
		s.append(",ms:" + ms);
		s.append(",desc:" + (desc != null ? desc : "null"));
		s.append("}");
		return s.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ProxyConfig))
			return false;

		if (this.ip == null)
			return false;

		ProxyConfig cfg = (ProxyConfig) obj;
		return this.ip.equals(cfg.getIp()) && this.port == cfg.getPort();
	}
}

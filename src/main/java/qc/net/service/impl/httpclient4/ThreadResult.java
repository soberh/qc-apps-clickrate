package qc.net.service.impl.httpclient4;

public class ThreadResult {
	private int code;
	private int ms;
	private String proxy;
	private boolean success;

	public ThreadResult(boolean success, int code, int ms, String proxy) {
		this.success = success;
		this.code = code;
		this.ms = ms;
		this.proxy = proxy;
	}

	public int getCode() {
		return code;
	}

	public int getMs() {
		return ms;
	}

	public String getProxy() {
		return proxy;
	}

	public boolean isSuccess() {
		return success;
	}
}

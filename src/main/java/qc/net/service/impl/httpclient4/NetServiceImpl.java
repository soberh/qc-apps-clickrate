package qc.net.service.impl.httpclient4;

import java.util.Map;

import qc.net.service.Callback;
import qc.net.service.NetService;

public class NetServiceImpl implements NetService {
	public boolean connect(Method method, String url,
			Map<String, String> headers, Map<String, String> sid) {
		return false;
	}

	public void repeat(String id, int num, int interval, Method method,
			String url, Map<String, String> headers, Map<String, String> sid,Callback callback) {

	}

	public void stopRepeat(String id) {

	}

	public void stopAllRepeat() {
		
	}
}

package qc.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class URLConnectionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		URL url = new URL(NetUtils.getTestUrl());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
		conn.setRequestMethod("GET"); 
		//URLConnection conn = url.openConnection();
		
		//add userAgent
		conn.setRequestProperty(
				"User-Agent",
				NetUtils.getUserAgent("chrome8"));
		
		//add headers
		Map<String, String> headers = NetUtils.getDefaultHeaders();
		Iterator<String> itor = headers.keySet().iterator();
		String name;
		while(itor.hasNext()){
			name = itor.next();
			conn.setRequestProperty(name, headers.get(name));
		}
		InputStream in = conn.getInputStream();
		BufferedInputStream bisIn = new BufferedInputStream(in);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				bisIn, "utf-8"));
		StringBuilder html = new StringBuilder();
		if (reader.ready()) {
			String line;
			do {
				line = reader.readLine();
				if (line != null) {
					html.append(line + "\r\n");
				}
			} while (line != null);
		}
		in.close();
		conn.disconnect();

		System.out.println(html);;
	}
}

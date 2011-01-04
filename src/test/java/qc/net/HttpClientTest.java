package qc.net;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * http://fireinwind.javaeye.com/blog/707260
 * @author dragon
 *
 */
public class HttpClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(NetUtils.getTestUrl());
		//getMethod.setFollowRedirects(true);
		
		//add userAgent
		getMethod.addRequestHeader("User-Agent", NetUtils.getUserAgent("chrome8"));
		
		//add headers
		Map<String, String> headers = NetUtils.getDefaultHeaders();
		Iterator<String> itor = headers.keySet().iterator();
		String name;
		while(itor.hasNext()){
			name = itor.next();
			getMethod.addRequestHeader(name, headers.get(name));
		}

		int statusCode = httpClient.executeMethod(getMethod);
		System.out.println("statusCode=" + statusCode);
		System.out.println(getMethod.getResponseBodyAsString());
//
//		if (statusCode == HttpStatus.SC_OK) {
//			InputStream in = getMethod.getResponseBodyAsStream();
//			InputSource is = new InputSource(in);
//			DOMParser domParser = new DOMParser();
//			domParser.parse(is);
//			Document document = domParser.getDocument();
//
//			System.out.println(getMethod.getURI());
//
//		}

	}
}

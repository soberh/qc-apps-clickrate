package qc.net;

import java.util.Iterator;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsoupTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		String url = NetUtils.getTestUrl();
		Connection con = Jsoup.connect(url);
		con.method(Method.GET);
		
		//add userAgent
		con.userAgent(NetUtils.getUserAgent("chrome8"));
		
		//add headers
		Map<String, String> headers = NetUtils.getDefaultHeaders();
		Iterator<String> itor = headers.keySet().iterator();
		String name;
		while(itor.hasNext()){
			name = itor.next();
			con.header(name, headers.get(name));
		}
		
		Document doc = con.get();
		System.out.println(doc.html());
	}
}

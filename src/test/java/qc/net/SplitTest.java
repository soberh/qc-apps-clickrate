package qc.net;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SplitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		String url = "key1=value1&key2=value2";
		Map<String, String> params;
		params = new HashMap<String, String>();
		String[] kvs = url.split("&");
		String[] kv;
		for (String _kv : kvs) {
			kv = _kv.split("=");
			params.put(kv[0], kv[1]);
		}
		Assert.assertEquals("value1", params.get("key1"));
		Assert.assertEquals("value2", params.get("key2"));
	}

	@Test
	public void test2() throws Exception {
		String url = "key1\nvalue\r\nkey2\rvalue2";
		String[] kvs = url.split(";|\r\n|\r|\n");
		Assert.assertEquals(4, kvs.length);
	}
}

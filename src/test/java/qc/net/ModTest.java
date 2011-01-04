package qc.net;

import junit.framework.Assert;

import org.junit.Test;

public class ModTest {
	@Test
	public void test1() throws Exception {
		Assert.assertEquals(0, 0 % 3);
		Assert.assertEquals(1, 1 % 3);
		Assert.assertEquals(2, 2 % 3);
		Assert.assertEquals(0, 3 % 3);
		Assert.assertEquals(1, 4 % 3);
		Assert.assertEquals(2, 5 % 3);
		Assert.assertEquals(0, 6 % 3);
		Assert.assertEquals(1, 7 % 3);
		Assert.assertEquals(2, 8 % 3);
		Assert.assertEquals(0, 9 % 3);
		Assert.assertEquals(1, 10 % 3);
	}
}

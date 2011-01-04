package qc.net;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;
import org.springframework.util.StringUtils;

import qc.util.EncodingUtils;

public class NetUtilsTest {
	@Test
	public void testGetLocalName() throws UnknownHostException {
		System.out.println("local hostName=" + NetUtils.getLocalHostName());
	}

	@Test
	public void testGetLocalIP() throws UnknownHostException {
		System.out.println("local hostIP=" + NetUtils.getLocalHostIP());
	}

	@Test
	public void testGetAllLocalIP() throws UnknownHostException {
		System.out.println("all local ip:\r\n    "
				+ StringUtils.arrayToDelimitedString(
						NetUtils.getAllLocalAddress(), "\r\n    "));
	}

	@Test
	public void testGetAllNetworkInterface() throws UnknownHostException,
			UnsupportedEncodingException {
		List<NetworkInterface> nis = NetUtils.getAllNetworkInterface();
		StringBuffer t = new StringBuffer("all local networkInterface:\r\n");
		int i = 0;
		for (NetworkInterface ni : nis) {
			t.append("  " + (++i) + " name: "
					+ (ni.getName() == null ? "null" : ni.getName()));
			if (ni.getDisplayName() != null) {
				t.append(" (" + ni.getDisplayName() + ")");
			}
			t.append("; addresses:");
			for (Enumeration<InetAddress> e = ni.getInetAddresses(); e
					.hasMoreElements();) {
				t.append("\n    " + e.nextElement());
			}
			t.append("\n");
		}

		System.out.println(t);
	}

	@Test
	public void testGetOuterNetIP() {
		System.out.println("OuterNetIP: " + NetUtils.getOuterNetIP());
	}
}

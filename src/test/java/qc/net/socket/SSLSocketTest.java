package qc.net.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

import javax.net.ssl.SSLSocketFactory;

import org.junit.Test;

/**
 * 
 * @author dragon
 * 
 */
public class SSLSocketTest {
	public static final String SERVER_NAME = "www.ggssl.com";
	public static final int SERVER_PORT = 443;
	public static final String ENCODING = "utf-8";// "ISO-8859-1";
	public static final String AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.224 Safari/534.10";
	//public static final String Agent = "SSL-TEST";

	@Test
	public void test() throws Exception {
		//InetAddress localAaddress = InetAddress.getByName("10.20.0.194");
		//InetAddress localAaddress = InetAddress.getByName("192.168.4.23");
		InetAddress localAaddress = InetAddress.getByName("113.111.200.104");
		System.out.println(new Date());
		Socket socket = SSLSocketFactory.getDefault().createSocket(
				SERVER_NAME, SERVER_PORT,localAaddress,0);
		System.out.println(new Date());
		if(false) return;
		//socket.bind(new InetSocketAddress("192.168.4.37", 0));
		try {
			Writer out = new OutputStreamWriter(socket.getOutputStream(),
					ENCODING);
			out.write("GET / HTTP/1.1\r\n");
			out.write("Host: " + SERVER_NAME + ":" + SERVER_PORT
					+ "\r\n");
			out.write("Agent: " + AGENT + "\r\n");
			out.write("\r\n");
			out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), ENCODING));
			System.out.println(new Date());
			System.out.println("====");
			int i=0;
			System.out.println((i++) + "getLocalPort:" + socket.getLocalPort());
			System.out.println((i++) + "getPort:" + socket.getPort());
			System.out.println((i++) + "getChannel:" + socket.getChannel());
			System.out.println((i++) + "getInetAddress:" + socket.getInetAddress());
			System.out.println((i++) + "getLocalAddress:" + socket.getLocalAddress());
			System.out.println((i++) + "getLocalSocketAddress:" + socket.getLocalSocketAddress());
			System.out.println((i++) + "getRemoteSocketAddress:" + socket.getRemoteSocketAddress());
			System.out.println((i++) + "getReuseAddress:" + socket.getReuseAddress());
			System.out.println((i++) + "getReceiveBufferSize:" + socket.getReceiveBufferSize());
			System.out.println(new Date());
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println(new Date());
			System.out.println("====");
			i=0;
			System.out.println((i++) + "getLocalPort:" + socket.getLocalPort());
			System.out.println((i++) + "getPort:" + socket.getPort());
			System.out.println((i++) + "getChannel:" + socket.getChannel());
			System.out.println((i++) + "getInetAddress:" + socket.getInetAddress());
			System.out.println((i++) + "getLocalAddress:" + socket.getLocalAddress());
			System.out.println((i++) + "getLocalSocketAddress:" + socket.getLocalSocketAddress());
			System.out.println((i++) + "getRemoteSocketAddress:" + socket.getRemoteSocketAddress());
			System.out.println(new Date());
		} finally {
			socket.close();
		}
	}
}

package qc.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author dragon
 * 
 */
public class SocketTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws Exception {
		String html = getUrl("192.168.4.15", "8081", "/qcdebug/debug.do");
		System.out.println("--html--");
		System.out.println(html);
	}

	public static String getUrl(String serverIP, String serverPort, String urlPath) {
		int nPort = new Integer(serverPort).intValue();
		Socket s = null;
		try {
			s = new Socket(serverIP, nPort);
			
			//s = new Socket();//(strIP, nPort);
			//s.bind(new InetSocketAddress("192.168.4.37", 0));
			//s.connect(new InetSocketAddress(strIP, nPort));

			if (s.isConnected()) {
				log("connected");
			}
			OutputStream output = s.getOutputStream();
			StringBuilder sb = new StringBuilder();
			sb.append("GET " + urlPath + " HTTP/1.1\r\n");
			sb.append("Host: " + serverIP + "\r\n");
			sb.append("Connection: Close\r\n");
			sb.append("\r\n");

			output.write(sb.toString().getBytes("utf-8"));
			log("Send:" + String.format("%n") + sb.toString());
			output.flush();

			InputStream in = s.getInputStream();
			BufferedInputStream bisIn = new BufferedInputStream(in);

			log(bisIn.available());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					bisIn, "utf-8"));

			StringBuilder html = new StringBuilder();
			if (reader.ready()) {
				String line;
				do {
					line = reader.readLine();
					if (line != null) {
						// System.out.println(line);
						html.append(line + "\r\n");
					}
				} while (line != null);
			}

			in.close();
			s.close();
			return html.toString();

		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				s.close();
			} catch (IOException ex2) {
				ex2.printStackTrace();
			}
		}
	}

	private static void log(String s) {
		System.out.println("log: " + s);
	}

	private static void log(Integer s) {
		System.out.println("log: " + s);
	}
}

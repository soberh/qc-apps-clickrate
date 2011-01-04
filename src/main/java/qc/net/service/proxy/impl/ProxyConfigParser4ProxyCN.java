/**
 * 
 */
package qc.net.service.proxy.impl;

import org.jsoup.nodes.Element;

import qc.net.service.proxy.ProxyConfigParser;
import qc.net.service.proxy.ProxyConfig;

/**
 * 解析从http://www.proxycn.com/html_proxy/http-1.html获取的的代理列列表
 * 
 * @author dragon
 * 
 */
public class ProxyConfigParser4ProxyCN implements ProxyConfigParser {
	public ProxyConfig parse(Element element) {
		/*
		 * element的dom结构为： <tr align="center" bgcolor="#fbfbfb"
		 * ondblclick="clip('119.1.174.28:3128');alert('已拷贝到剪贴板!')"
		 * onmouseover="this.style.backgroundColor='#E1E8E8';return true;"
		 * onmouseout="this.style.backgroundColor='#fbfbfb';"> <td
		 * class="list">30</td> <td class="list"> <script language="JavaScript">
		 * <!-- document.write("119."); document.write("1.");
		 * document.write("174."); document.write("28"); //--> </script></td>
		 * <td class="list">3128</td> <td class="list">HTTP</td> <td
		 * class="list">贵州省黔东南州 电信</td> <td class="list">12-31 17:13</td> <td
		 * class="list">1.001</td> <td class="list"><a
		 * href="whois.php?whois=119.1.174.28" target="_blank">whois</a></td>
		 * </tr>
		 */

		ProxyConfig cfg = new ProxyConfig();
		cfg.setPort(Integer.parseInt(element.select("td:eq(2)").html().trim()));
		cfg.setType(element.select("td:eq(3)").html().trim());
		cfg.setDesc(element.select("td:eq(4)").html().trim());
		cfg.setIp(element.select("td:eq(7)>a").attr("href").split("=")[1]);
		cfg.setMs(new Float(Float.parseFloat(element.select("td:eq(6)").html()
				.trim()) * 1000).intValue());
		return cfg;
	}
}

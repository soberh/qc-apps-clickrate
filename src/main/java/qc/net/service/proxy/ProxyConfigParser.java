package qc.net.service.proxy;

import org.jsoup.nodes.Element;

public interface ProxyConfigParser {
	ProxyConfig parse(Element element);
}

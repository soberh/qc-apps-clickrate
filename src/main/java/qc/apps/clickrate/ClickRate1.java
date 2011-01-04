package qc.apps.clickrate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import qc.net.NetUtils;
import qc.net.service.Callback;
import qc.net.service.NetService;
import qc.net.service.NetService.Method;
import qc.net.service.proxy.ProxyConfig;
import qc.net.service.proxy.ProxyConfigFinder;
import qc.net.service.proxy.ProxyConfigParser;
import qc.net.service.proxy.ProxyConfigService;

import com.swtdesigner.SWTResourceManager;

public class ClickRate1 {
	private int mport = 1;
	private int mip = 1;
	private int mspeed = 1;

	private final class ProxyIpComparator implements Comparator<ProxyConfig> {
		public int compare(ProxyConfig cfg1, ProxyConfig cfg2) {
			return mip * cfg1.getIp().compareTo(cfg2.getIp());
		}
	}

	private final class ProxyPortComparator implements Comparator<ProxyConfig> {
		public int compare(ProxyConfig cfg1, ProxyConfig cfg2) {
			if (cfg1.getPort() > cfg2.getPort()) {
				return mport * 1;
			} else if (cfg1.getPort() < cfg2.getPort()) {
				return mport * -1;
			} else {
				return 0;
			}
		}
	}

	private final class ProxySpeedComparator implements Comparator<ProxyConfig> {
		public int compare(ProxyConfig cfg1, ProxyConfig cfg2) {
			if (cfg1.getMs() > cfg2.getMs()) {
				return mspeed * 1;
			} else if (cfg1.getMs() < cfg2.getMs()) {
				return mspeed * -1;
			} else {
				return 0;
			}
		}
	}

	private static final Log logger = LogFactory.getLog(ClickRate1.class);
	private NetService netService;
	private ProxyConfigService proxyConfigService;
	private static final String TID = "TID1";
	private List<ProxyConfig> proxyConfigs;

	public void setNetService(NetService netService) {
		this.netService = netService;
	}

	public void setProxyConfigService(ProxyConfigService proxyConfigService) {
		this.proxyConfigService = proxyConfigService;
	}

	protected Shell shlClickRate;
	private Text txtUrl;
	private Text textLog;
	private Spinner spinnerRepeatNum;
	private Spinner spinnerInterval;
	private Spinner spinnerTimeout;
	private Button chkMarkUrl;
	private Button chkUseProxy;
	private Combo comboUnit;
	private Button btnStart;
	private Button btnStop;
	private Button btnTest;
	private int cur = 0;
	private Table table;

	public Text getLogPanel() {
		return this.textLog;
	}

	protected String getUrl() {
		String url = this.txtUrl.getText().trim();
		if (this.chkMarkUrl.getSelection()) {
			url += (url.indexOf("?") == -1 ? "?" : "&") + "rc=" + cur;
		}
		return url;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlClickRate.open();
		shlClickRate.layout();

		Config.initLoger(this.getLogPanel());
		this.initProxyConfig();
		while (!shlClickRate.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	// 初始化预定义代理的配置
	private void initProxyConfig() {
		proxyConfigs = proxyConfigService.find();
		orderProxies(null);
	}

	private void showProxies() {
		showProxies(true);
	}

	private void showProxies(boolean clean) {
		if (clean) {
			table.removeAll();
		}
		TableItem tableItem;
		int i = 1;
		for (ProxyConfig cfg : proxyConfigs) {
			tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] { (i++) + "", cfg.getIp(),
					cfg.getPort() + "", cfg.getType(), cfg.getMs() + "",
					cfg.getDesc() });
		}
	}

	/**
	 * 重新获取新的代理列表 包括从外网获取、更新界面
	 */
	private void autoLoadProxies() {
		String[] urls = Config.getValue("app.proxy_finder.url").split(
				";|\r\n|\r|\n");
		logger.warn("搜索新的代理...");
		String parser = Config.getValue("app.proxy_finder.parser");
		String selector = Config.getValue("app.proxy_finder.selector");
		ProxyConfigParser proxyParser = null;
		try {
			proxyParser = (ProxyConfigParser) Class.forName(parser)
					.newInstance();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return;
		}
		Map<String, ProxyConfig> map = new HashMap<String, ProxyConfig>();
		List<ProxyConfig> list;
		for (String url : urls) {
			if (url != null && url.trim().length() > 0) {
				logger.info("  url=" + url);
				list = ProxyConfigFinder.find(url, selector, proxyParser);
				for (ProxyConfig cfg : list) {
					if (!map.containsKey(cfg.getIp())) {
						map.put(cfg.getIp(), cfg);
					}
				}
			}
		}
		int n = 0;
		if (map.isEmpty()) {
			logger.warn("搜索不到新的代理");
		} else {
			// this.proxyConfigs.clear();
			for (Entry<String, ProxyConfig> entry : map.entrySet()) {
				if (!this.proxyConfigs.contains(entry.getValue())) {
					this.proxyConfigs.add(entry.getValue());
					n++;
				}
			}
			logger.warn("搜索到" + n + "个新代理(" + map.size() + ")"
					+ (n > 0 ? ",建议重新进行速度测试" : ""));
			this.showProxies();
		}
	}

	// 代理连接速度测试，并删除超时或错误的代理
	private void speedTest() {
		if (this.proxyConfigs == null || this.proxyConfigs.isEmpty()) {
			logger.fatal("当前还没有配置任何可用代理，无法处理");
		}
		int timeout = this.spinnerTimeout.getSelection();
		String url = getUrl();
		int speed;
		List<ProxyConfig> mustToRemove = new ArrayList<ProxyConfig>();

		// 测试速度
		int n = 0;
		for (ProxyConfig cfg : this.proxyConfigs) {
			logger.warn("开始测试(" + (++n) + "/" + this.proxyConfigs.size() + ")："
					+ cfg.getIp() + ":" + cfg.getPort());
			speed = this.netService.proxySpeedTest(cfg, url, Method.GET,
					timeout);
			cfg.setMs(speed);
			if (speed == -1)
				logger.warn("  连接超时");
			else if (speed == -2)
				logger.warn("  连接错误");
			else
				logger.warn("  耗时" + speed + "毫秒");
			if (speed < 0) {
				mustToRemove.add(cfg);
			}
		}

		// 删除慢速代理
		this.proxyConfigs.removeAll(mustToRemove);

		// 按速度排序、更新界面
		orderProxies(null);
	}

	private void deleteSlowProxy() {
		int timeout = this.spinnerTimeout.getSelection();
		List<ProxyConfig> mustToRemove = new ArrayList<ProxyConfig>();
		for (ProxyConfig cfg : this.proxyConfigs) {
			if (cfg.getMs() > timeout) {
				mustToRemove.add(cfg);
			}
		}

		// 删除慢速代理
		if (mustToRemove.isEmpty()) {
			logger.warn("当前没有慢速代理可删除");
		} else {
			this.proxyConfigs.removeAll(mustToRemove);

			// 按速度排序、更新界面
			orderProxies(null);

			logger.warn("已删除" + mustToRemove.size() + "个慢速代理");
		}
	}

	private void orderProxies(String type) {
		this.orderProxies(this.proxyConfigs, type);

		// 更新界面
		showProxies();
	}

	private void orderProxies(List<ProxyConfig> cfgs, String type) {
		if ("ip".equalsIgnoreCase(type)) {
			Collections.sort(cfgs, new ProxyIpComparator());
		} else if ("port".equalsIgnoreCase(type)) {
			Collections.sort(cfgs, new ProxyPortComparator());
		} else {
			Collections.sort(cfgs, new ProxySpeedComparator());
		}
	}

	// 保存当前的代理配置
	private void saveProxiesConfig() {
		if (this.proxyConfigs == null || this.proxyConfigs.isEmpty()) {
			logger.fatal("当前没有配置任何可用代理，无法保存");
		}
		proxyConfigService.save(proxyConfigs);
		logger.fatal("保存完毕");
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlClickRate = new Shell();
		shlClickRate.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				// 必须调用这个，否则定时器线程javaw.exe依然在任务管理器中不释放
				ClickRate1.this.netService.stopAllRepeat();
			}
		});
		shlClickRate.setToolTipText("盗亦有道");
		shlClickRate.setImage(SWTResourceManager.getImage(ClickRate1.class,
				"/icon.png"));
		shlClickRate.setSize(626, 530);
		shlClickRate.setText("Click Rate 4.0a1");

		textLog = new Text(shlClickRate, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		textLog.setText("");
		textLog.setBounds(10, 259, 598, 227);

		TabFolder tabFolder = new TabFolder(shlClickRate, SWT.NONE);
		tabFolder.setBounds(10, 10, 598, 243);

		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("主页");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite);

		btnStart = new Button(composite, SWT.NONE);
		btnStart.setBounds(283, 177, 95, 31);
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				logger.fatal("开始");
				String url = getUrl();
				// Map<String, String> headers = NetUtils.getDefaultHeaders();
				int num = ClickRate1.this.spinnerRepeatNum.getSelection();
				ClickRate1.this.netService.multithreaded(TID, num,
						NetService.Method.GET, url, null, proxyConfigs,
						new Callback<int[]>() {
							public void call(int[] result) {
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										btnStart.setEnabled(true);
										btnStop.setEnabled(false);
									}
								});
							}
						});
			}
			// public void widgetSelected(SelectionEvent e) {
			// cur = 0;
			// btnStart.setEnabled(false);
			// btnStop.setEnabled(true);
			// logger.fatal("开始");
			// String url = getUrl();
			// Map<String, String> headers = NetUtils.getDefaultHeaders();
			// headers.put("User-Agent", NetUtils.getDefaultUserAgent());
			// int num = ClickRate1.this.spinnerRepeatNum.getSelection();
			// int interval = NetUtils.getInterval(
			// ClickRate1.this.spinnerInterval.getSelection(),
			// ClickRate1.this.comboUnit.getText());
			//
			// ClickRate1.this.netService.repeat(
			// TID,
			// num,
			// interval,
			// NetService.Method.GET,
			// url,
			// headers,
			// null,
			// new Callback() {
			// public void call(int c) {
			// if (c == -1) {
			// Display.getDefault().syncExec(
			// new Runnable() {
			// public void run() {
			// btnStart.setEnabled(true);
			// btnStop.setEnabled(false);
			// }
			// });
			// } else {
			// cur = c;
			// }
			// }
			// },
			// (ClickRate1.this.chkUseProxy.getSelection() ?
			// ClickRate1.this.proxyConfigs
			// : null));
			// }

		});
		btnStart.setText("开始");

		btnStop = new Button(composite, SWT.NONE);
		btnStop.setBounds(384, 177, 95, 31);
		btnStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.fatal("停止");
				//ClickRate1.this.netService.stopRepeat(TID);
				ClickRate1.this.netService.stopMulti();
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
			}
		});
		btnStop.setText("停止");
		btnStop.setEnabled(false);

		btnTest = new Button(composite, SWT.NONE);
		btnTest.setBounds(485, 177, 95, 31);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String url = getUrl();
				logger.warn("开始测试一下：" + url);
				Map<String, String> headers = NetUtils.getDefaultHeaders();
				headers.put("User-Agent", NetUtils.getDefaultUserAgent());
				Map<String, String> sid = new HashMap<String, String>();
				ClickRate1.this.netService.connect(NetService.Method.GET, url,
						headers, sid);
				logger.warn("测试完毕,cookie=" + sid);
			}
		});
		btnTest.setText("测试一下");

		Group group = new Group(composite, SWT.NONE);
		group.setBounds(10, 112, 570, 52);
		group.setText("配置");

		Label label = new Label(group, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setBounds(10, 21, 60, 12);
		label.setText("重复次数：");

		spinnerRepeatNum = new Spinner(group, SWT.BORDER);
		spinnerRepeatNum.setPageIncrement(1);
		spinnerRepeatNum.setMaximum(10000);
		spinnerRepeatNum.setMinimum(1);
		spinnerRepeatNum.setSelection(2);
		spinnerRepeatNum.setBounds(76, 17, 80, 20);

		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("重复间隔：");
		label_1.setAlignment(SWT.RIGHT);
		label_1.setBounds(177, 21, 60, 12);

		spinnerInterval = new Spinner(group, SWT.BORDER);
		spinnerInterval.setPageIncrement(1);
		spinnerInterval.setMaximum(60);
		spinnerInterval.setMinimum(1);
		spinnerInterval.setSelection(5);
		spinnerInterval.setBounds(243, 17, 80, 20);

		chkMarkUrl = new Button(group, SWT.CHECK);
		chkMarkUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		chkMarkUrl.setBounds(480, 19, 80, 16);
		chkMarkUrl.setText("附加计数值");

		comboUnit = new Combo(group, SWT.READ_ONLY);
		comboUnit.setItems(new String[] { "毫秒", "秒", "分钟", "小时" });
		comboUnit.setBounds(322, 17, 55, 21);
		comboUnit.setText("秒");
		comboUnit.select(1);

		chkUseProxy = new Button(group, SWT.CHECK);
		chkUseProxy.setBounds(405, 19, 69, 16);
		chkUseProxy.setText("使用代理");
		chkUseProxy.setSelection(true);

		Group group_1 = new Group(composite, SWT.NONE);
		group_1.setText("网址：");
		group_1.setBounds(10, 10, 570, 92);

		txtUrl = new Text(group_1, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.MULTI);
		txtUrl.setBounds(10, 18, 550, 64);
		txtUrl.setText("http://opendragon2010.blog.163.com/blog/static/1775342832010113114815909/");

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("代理");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);

		spinnerTimeout = new Spinner(composite_1, SWT.BORDER);
		spinnerTimeout.setIncrement(100);
		spinnerTimeout.setPageIncrement(100);
		spinnerTimeout.setMaximum(600000);
		spinnerTimeout.setMinimum(100);
		spinnerTimeout.setSelection(2000);
		spinnerTimeout.setBounds(83, 182, 59, 21);

		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		table.setBounds(10, 10, 570, 161);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setMoveable(true);
		tableColumn.setWidth(40);
		tableColumn.setText("序号");

		TableColumn tblclmnIp = new TableColumn(table, SWT.NONE);
		tblclmnIp.setMoveable(true);
		tblclmnIp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClickRate1.this.mip = ClickRate1.this.mip * -1;
				ClickRate1.this.orderProxies("ip");
			}
		});
		tblclmnIp.setWidth(123);
		tblclmnIp.setText("IP地址");

		TableColumn tblclmnPort = new TableColumn(table, SWT.NONE);
		tblclmnPort.setMoveable(true);
		tblclmnPort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClickRate1.this.mport = ClickRate1.this.mport * -1;
				ClickRate1.this.orderProxies("port");
			}
		});
		tblclmnPort.setWidth(43);
		tblclmnPort.setText("端口");

		TableColumn tableColumn_3 = new TableColumn(table, SWT.NONE);
		tableColumn_3.setMoveable(true);
		tableColumn_3.setWidth(43);
		tableColumn_3.setText("类型");

		TableColumn tblclmnSpeed = new TableColumn(table, SWT.NONE);
		tblclmnSpeed.setMoveable(true);
		tblclmnSpeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClickRate1.this.mspeed = ClickRate1.this.mspeed * -1;
				ClickRate1.this.orderProxies("speed");
			}
		});
		tblclmnSpeed.setWidth(59);
		tblclmnSpeed.setText("速度");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setMoveable(true);
		tableColumn_1.setWidth(228);
		tableColumn_1.setText("描述");

		Button btnSpeedTest = new Button(composite_1, SWT.NONE);
		btnSpeedTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClickRate1.this.speedTest();
			}
		});
		btnSpeedTest.setText("速度测试");
		btnSpeedTest.setBounds(399, 177, 59, 31);

		Button btnFindProxy = new Button(composite_1, SWT.NONE);
		btnFindProxy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClickRate1.this.autoLoadProxies();
			}
		});
		btnFindProxy.setText("搜索新代理");
		btnFindProxy.setBounds(464, 177, 69, 31);

		Label label_2 = new Label(composite_1, SWT.NONE);
		label_2.setBounds(10, 186, 79, 12);
		label_2.setText(" *将连接超过");

		Label label_3 = new Label(composite_1, SWT.NONE);
		label_3.setBounds(144, 186, 117, 12);
		label_3.setText("毫秒的代理标为慢速");

		Button btnSaveProxyConfig = new Button(composite_1, SWT.NONE);
		btnSaveProxyConfig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClickRate1.this.saveProxiesConfig();
			}
		});
		btnSaveProxyConfig.setBounds(539, 177, 41, 31);
		btnSaveProxyConfig.setText("保存");

		Button btnDelProxy = new Button(composite_1, SWT.NONE);
		btnDelProxy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] ti = ClickRate1.this.table.getSelection();
				List<ProxyConfig> mustToRemove = new ArrayList<ProxyConfig>();
				for (TableItem item : ti) {
					for (ProxyConfig cfg : ClickRate1.this.proxyConfigs) {
						if (cfg.getIp().equalsIgnoreCase(item.getText(1)))
							mustToRemove.add(cfg);
					}
				}
				ClickRate1.this.proxyConfigs.removeAll(mustToRemove);

				ClickRate1.this.table.remove(ClickRate1.this.table
						.getSelectionIndices());
			}
		});
		btnDelProxy.setBounds(267, 177, 41, 31);
		btnDelProxy.setText("X");

		Button btnDelSlowProxy = new Button(composite_1, SWT.NONE);
		btnDelSlowProxy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClickRate1.this.deleteSlowProxy();
			}
		});
		btnDelSlowProxy.setBounds(314, 177, 79, 31);
		btnDelSlowProxy.setText("删除慢速代理");
		// textLog.setBackgroundImage(SWTResourceManager.getImage(ClickRate1.class,
		// "/dao.png"));
	}
}

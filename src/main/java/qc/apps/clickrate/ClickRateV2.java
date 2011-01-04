package qc.apps.clickrate;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import qc.net.NetUtils;
import qc.net.service.Callback;
import qc.net.service.NetService;

import com.swtdesigner.SWTResourceManager;

public class ClickRateV2 {
	private static final Log logger = LogFactory.getLog(ClickRateV2.class);
	private NetService netService;
	private static final String TID = "TID1";

	public void setNetService(NetService netService) {
		this.netService = netService; 
	}

	protected Shell shlClickRate;
	private Text txtUrl;
	private Text textLog;
	private Spinner spinnerRepeatNum;
	private Spinner spinnerInterval;
	private Button chkMarkUrl;
	private Combo comboUnit;
	private Button btnStart;
	private Button btnStop;
	private Button btnTest;
	private int cur = 0;

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
	 * @wbp.nonvisual location=38,222
	 */
	private final JTextField textField = new JTextField();

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlClickRate.open();
		shlClickRate.layout();

		Config.initLoger(this.getLogPanel());
		while (!shlClickRate.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		textField.setColumns(10);
		shlClickRate = new Shell();
		shlClickRate.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				// 必须调用这个，否则定时器线程javaw.exe依然在任务管理器中不释放
				ClickRateV2.this.netService.stopAllRepeat();
			}
		});
		shlClickRate.setToolTipText("盗亦有道");
		shlClickRate.setImage(SWTResourceManager.getImage(ClickRateV2.class,
				"/icon.png"));
		shlClickRate.setSize(626, 530);
		shlClickRate.setText("Click Rate 1 v2.0");

		btnTest = new Button(shlClickRate, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String url = getUrl();
				logger.warn("开始测试一下：" + url);
				Map<String, String> headers = NetUtils.getDefaultHeaders();
				headers.put("User-Agent", NetUtils.getDefaultUserAgent());
				Map<String, String> sid = new HashMap<String, String>();
				ClickRateV2.this.netService.connect(NetService.Method.GET, url,
						headers, sid);
				logger.warn("测试完毕,cookie=" + sid);
			}
		});
		btnTest.setBounds(502, 184, 95, 31);
		btnTest.setText("测试一下");

		Group grpOk = new Group(shlClickRate, SWT.NONE);
		grpOk.setText("网址");
		grpOk.setBounds(10, 10, 598, 106);

		txtUrl = new Text(grpOk, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.MULTI);
		txtUrl.setText("http://rongjih.blog.163.com/blog/static/335744612010112041023772/");
		txtUrl.setBounds(10, 20, 578, 76);

		Group group = new Group(shlClickRate, SWT.NONE);
		group.setText("配置");
		group.setBounds(10, 122, 598, 52);

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
		label_1.setBounds(175, 21, 60, 12);

		spinnerInterval = new Spinner(group, SWT.BORDER);
		spinnerInterval.setPageIncrement(1);
		spinnerInterval.setMaximum(60);
		spinnerInterval.setMinimum(1);
		spinnerInterval.setSelection(5);
		spinnerInterval.setBounds(241, 17, 80, 20);

		chkMarkUrl = new Button(group, SWT.CHECK);
		chkMarkUrl.setBounds(464, 19, 124, 16);
		chkMarkUrl.setText("附加计数值到网址");

		comboUnit = new Combo(group, SWT.READ_ONLY);
		comboUnit.setItems(new String[] { "毫秒", "秒", "分钟", "小时" });
		comboUnit.setBounds(320, 17, 55, 21);
		comboUnit.setText("秒");
		comboUnit.select(1);

		btnStop = new Button(shlClickRate, SWT.NONE);
		btnStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.fatal("停止");
				ClickRateV2.this.netService.stopRepeat(TID);
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
			}
		});
		btnStop.setText("停止");
		btnStop.setBounds(401, 184, 95, 31);
		btnStop.setEnabled(false);

		btnStart = new Button(shlClickRate, SWT.NONE);
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cur = 0;
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				logger.fatal("开始");
				String url = getUrl();
				Map<String, String> headers = NetUtils.getDefaultHeaders();
				headers.put("User-Agent", NetUtils.getDefaultUserAgent());
				int num = ClickRateV2.this.spinnerRepeatNum.getSelection();
				int interval = NetUtils.getInterval(
						ClickRateV2.this.spinnerInterval.getSelection(),
						ClickRateV2.this.comboUnit.getText());
				ClickRateV2.this.netService.repeat(TID, num, interval,
						NetService.Method.GET, url, headers, null,
						new Callback() {
							public void call(int c) {
								if (c == -1) {
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													btnStart.setEnabled(true);
													btnStop.setEnabled(false);
												}
											});
								} else {
									cur = c;
								}
							}
						});
			}

		});
		btnStart.setText("开始");
		btnStart.setBounds(300, 184, 95, 31);

		textLog = new Text(shlClickRate, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		textLog.setText("");
		textLog.setBounds(10, 228, 598, 258);
		// textLog.setBackgroundImage(SWTResourceManager.getImage(ClickRate1.class,
		// "/dao.png"));
	}
}

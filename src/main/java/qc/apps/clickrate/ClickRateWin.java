package qc.apps.clickrate;

import java.awt.Button;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import qc.net.ConnectionUtils;

public class ClickRateWin extends Frame {
	private static final Log logger = LogFactory.getLog(ClickRateWin.class);

	private static final long serialVersionUID = 1L;
	private Label label = null;
	private TextField txtUrl = null;
	private Button btnStart = null;
	private Button btnStop = null;
	private Label label1 = null;
	private Label label2 = null;
	private TextField txtNum = null;
	private TextField txtInterval = null;
	private TextArea textAreaLog = null;
	private Label label3 = null;

	/**
	 * This method initializes txtUrl
	 * 
	 * @return java.awt.TextField
	 */
	private TextField getTxtUrl() {
		if (txtUrl == null) {
			txtUrl = new TextField();
			txtUrl.setBounds(new Rectangle(86, 37, 480, 21));
			txtUrl.setComponentOrientation(ComponentOrientation.UNKNOWN);
			txtUrl.setText("http://rongjih.blog.163.com/blog/static/335744612010112041023772/");
		}
		return txtUrl;
	}

	/**
	 * This method initializes btnStart
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtnStart() {
		if (btnStart == null) {
			btnStart = new Button();
			btnStart.setBounds(new Rectangle(181, 73, 123, 51));
			btnStart.setLabel("开始循环");
			btnStart.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					String url = ClickRateWin.this.getTxtUrl().getText().trim();
					int num = Integer.parseInt(ClickRateWin.this.getTxtNum()
							.getText().trim());
					int interval = Integer.parseInt(ClickRateWin.this
							.getTxtInterval().getText().trim());
					logger.info("start repeat...");
					ConnectionUtils.repeatGet(num, interval, url);
				}
			});
		}
		return btnStart;
	}

	/**
	 * This method initializes btnStop
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtnStop() {
		if (btnStop == null) {
			btnStop = new Button();
			btnStop.setBounds(new Rectangle(329, 74, 128, 51));
			btnStop.setLabel("终止循环");
			btnStop.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					textAreaLog.append("终止循环\r\n");
				}
			});
		}
		return btnStop;
	}

	/**
	 * This method initializes txtNum
	 * 
	 * @return java.awt.TextField
	 */
	private TextField getTxtNum() {
		if (txtNum == null) {
			txtNum = new TextField();
			txtNum.setBounds(new Rectangle(86, 73, 70, 22));
			txtNum.setText("2");
		}
		return txtNum;
	}

	/**
	 * This method initializes txtInterval
	 * 
	 * @return java.awt.TextField
	 */
	private TextField getTxtInterval() {
		if (txtInterval == null) {
			txtInterval = new TextField();
			txtInterval.setBounds(new Rectangle(86, 106, 70, 22));
			txtInterval.setText("5000");
		}
		return txtInterval;
	}

	/**
	 * This method initializes textAreaLog
	 * 
	 * @return java.awt.TextArea
	 */
	private TextArea getTextAreaLog() {
		if (textAreaLog == null) {
			textAreaLog = new TextArea();
			textAreaLog.setBounds(new Rectangle(7, 162, 560, 243));
		}
		return textAreaLog;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClickRateWin awt = new ClickRateWin();
		awt.setVisible(true);

		// 配置log4j
		DOMConfigurator.configure(ClickRateWin.class.getResource("/log4j.xml"));
	}

	/**
	 * This is the default constructor
	 */
	public ClickRateWin() {
		super();
		initialize();

		// 将控制台的输出改为输出到textAreaLog控件，方便log4j的日志查看
		//PrintStream out = new PrintStream(new GuiOutputStream(this.textAreaLog));
		//System.setOut(out);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		label3 = new Label();
		label3.setBounds(new Rectangle(12, 139, 71, 21));
		label3.setAlignment(Label.RIGHT);
		label3.setText("输出信息：");
		label2 = new Label();
		label2.setBounds(new Rectangle(11, 106, 72, 21));
		label2.setAlignment(Label.RIGHT);
		label2.setText("时间间隔：");
		label1 = new Label();
		label1.setBounds(new Rectangle(10, 74, 73, 20));
		label1.setAlignment(Label.RIGHT);
		label1.setText("重复次数：");
		label = new Label();
		label.setBounds(new Rectangle(9, 37, 74, 20));
		label.setAlignment(Label.RIGHT);
		label.setFont(new Font("宋体", Font.PLAIN, 12));
		label.setText("网        址：");
		this.setLayout(null);
		this.setSize(575, 413);
		this.setFont(new Font("宋体", Font.PLAIN, 12));
		this.setTitle("ClickRate");

		this.add(label, null);
		this.add(getTxtUrl(), null);
		this.add(getBtnStart(), null);
		this.add(getBtnStop(), null);
		this.add(label1, null);
		this.add(label2, null);
		this.add(getTxtNum(), null);
		this.add(getTxtInterval(), null);
		this.add(getTextAreaLog(), null);
		this.add(label3, null);

		this.setResizable(false);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		this.setLocationByPlatform(true);
	}

} // @jve:decl-index=0:visual-constraint="10,10"

package qc.apps.clickrate;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLayeredPane;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.TextArea;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSplitPane;
import javax.swing.JProgressBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public class SwingMain {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 526, 497);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 321, 39);
		frame.getContentPane().add(toolBar);
		
		JButton button = new JButton("开始");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		toolBar.add(button);
		
		JButton button_1 = new JButton("结束");
		toolBar.add(button_1);
		
		JButton button_2 = new JButton("测试一下");
		toolBar.add(button_2);
		
		JToggleButton toggleButton = new JToggleButton("New toggle button");
		toolBar.add(toggleButton);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(80, 171, 63, 20);
		frame.getContentPane().add(spinner);
		
		JCheckBox checkBox = new JCheckBox("New check box");
		checkBox.setBounds(161, 169, 103, 23);
		frame.getContentPane().add(checkBox);
		
		JLabel label = new JLabel("New label");
		label.setBounds(20, 173, 54, 15);
		frame.getContentPane().add(label);
		
		JTextArea txtrAa = new JTextArea();
		txtrAa.setText("aa");
		txtrAa.setRows(4);
		txtrAa.setLineWrap(true);
		txtrAa.setBounds(10, 45, 352, 83);
		txtrAa.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(txtrAa);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 215, 311, 144);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel label_1 = new JLabel("New label");
		label_1.setBounds(10, 10, 54, 15);
		panel_1.add(label_1);
		
		textField = new JTextField();
		textField.setBounds(70, 8, 71, 20);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
		panel_2.setLayout(null);
		
		textField_1 = new JTextField();
		textField_1.setBounds(58, 27, 71, 20);
		panel_2.add(textField_1);
		textField_1.setColumns(10);
		
		JSlider slider = new JSlider();
		slider.setBounds(64, 383, 200, 25);
		frame.getContentPane().add(slider);
		
		JButton button_3 = new JButton("New button");
		button_3.setBounds(397, 423, 111, 30);
		frame.getContentPane().add(button_3);
	}
}

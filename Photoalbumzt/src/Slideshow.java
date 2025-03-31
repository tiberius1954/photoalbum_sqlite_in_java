import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import Classes.Album;
import Classes.Grahelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Slideshow extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	Grahelper gh = new Grahelper();
	private ArrayList<String> pictures = new ArrayList<>();
	private String[][] arr;
	int act_aid;
	Timer tm;
	int x = 0;

	Slideshow() {
		initcomponents();
		hh.iconhere(this);
		dd.albumscombofill(cmbalbums);
	}

	private void initcomponents() {
		UIManager.put("ComboBox.selectionBackground", hh.piros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));
		UIManager.put("ComboBox.disabledForeground", Color.magenta);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});
		setSize(1300, 660);
		setLayout(null);
		setLocationRelativeTo(null);
		getContentPane().setBackground(hh.skek);
		mPanel = new JPanel(null);
		mPanel.setBounds(0, 1, 1284, 610);
		mPanel.setBackground(hh.skek);
		add(mPanel);
		lbheader = hh.fflabel("S L I D E S H O W");
		lbheader.setBounds(630, 20, 250, 40);
		mPanel.add(lbheader);

		lPanel = new JPanel(null);
		lPanel.setBounds(6, 60, 365, 540);
		lPanel.setBackground(hh.skek);
		lPanel.setBorder(hh.lineo);
		mPanel.add(lPanel);

		rPanel = new JPanel();
		rPanel.setBounds(380, 60, 898, 540);
		rPanel.setBackground(hh.skek);
		rPanel.setBorder(hh.lineo);
		rPanel.setLayout(new BorderLayout());

		lbimage = new JLabel();
		lbimage.setPreferredSize(new Dimension(890, 510));
		lbimage.setBorder(hh.linesz);
		lbimage.setHorizontalAlignment(JLabel.CENTER);
		lbimage.setVerticalAlignment(JLabel.CENTER);
		rPanel.add(lbimage, BorderLayout.CENTER);
		mPanel.add(rPanel);

		lbalbum = hh.clabel("Name of the Album");
		lbalbum.setBounds(100, 20, 150, 30);
		lbalbum.setHorizontalAlignment(JLabel.CENTER);
		lPanel.add(lbalbum);

		cmbalbums = gr.grcombo();
		cmbalbums.setFocusable(true);
		cmbalbums.setName("albums");
		cmbalbums.setBounds(40, 60, 280, 28);
		lPanel.add(cmbalbums);
		cmbalbums.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Album alb = (Album) cmbalbums.getSelectedItem();
					String path = alb.getPath();
					String name = alb.getName();
					act_aid = alb.getAid();
					if (act_aid != 0) {
						start();
					}
				}
			}
		});

		lbpicture = hh.clabel("Name of the picture");
		lbpicture.setBounds(100, 100, 150, 30);
		lbpicture.setHorizontalAlignment(JLabel.CENTER);
		lPanel.add(lbpicture);

		txpname = hh.cTextField(30);
		txpname.setBounds(40, 140, 280, 30);
		txpname.setEditable(false);
		lPanel.add(txpname);

		lbnote = hh.clabel("Note to the picture");
		lbnote.setBounds(100, 190, 150, 30);
		lbnote.setHorizontalAlignment(JLabel.CENTER);
		lPanel.add(lbnote);

		txanote = hh.cTextarea();
		txanote.setBounds(40, 230, 280, 80);
		txanote.setEditable(false);
		lPanel.add(txanote);

		btnstop = gr.sbcs("Stop");
		btnstop.setBounds(120, 360, 120, 35);
		btnstop.setBackground(hh.lpiros);
		lPanel.add(btnstop);
		btnstop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				tm.stop();
			}
		});

		tm = new Timer(300, new ActionListener() {
			int ix = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				Instant start = Instant.now();
				SetImageSize(x);
				Instant end = Instant.now();
				Duration timeEl = Duration.between(start, end);
				int sec = (int) timeEl.toMillis();
				try {
					if (3000 - sec < 0) {
						ix = sec - 3000;
					} else {
						ix = 3000 - sec;
					}
					Thread.sleep(ix);
				} catch (InterruptedException e1) {// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				x += 1;
				if (x >= arr.length)
					x = 0;
			}
		});
		setVisible(true);
	}

	private void start() {
		int t = collect();
		if (t == 0) {
			return;
		}

		SetImageSize(0);
		tm.start();
	}

	private int collect() {
		int num = 0;
		String Sql = "select count()  as num from pictures where aid=" + act_aid;
		rs = dh.GetData(Sql);
		try {
			if (rs.next()) {
				num = rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}

		if (num == 0) {
			return num;
		}
		arr = new String[num][5];
		Sql = "select  pid, aid, name, note, path, date  from pictures where aid=" + act_aid;

		try {
			int ii = 0;
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String name = rs.getString("name");
				String note = rs.getString("note");
				String path = rs.getString("path");
				arr[ii][0] = name;
				arr[ii][1] = note;
				arr[ii][2] = path;
				ii++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		return num;
	}

	public void SetImageSize(int i) {
		String path = arr[i][2];
		String name = arr[i][0];
		String note = arr[i][1];
		txpname.setText(name);
		txanote.setText(note);
		File file = new File(path);
		BufferedImage image;
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(lbimage.getWidth(), lbimage.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon newImc = new ImageIcon(newImg);
		lbimage.setIcon(newImc);
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Slideshow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JLabel lbheader, lbimage, lbalbum, lbnote, lbname, lbpicture;
	JTextField txpname;
	JTextArea txanote;
	JPanel mPanel, lPanel, rPanel;
	JComboBox cmbalbums;
	JButton btnstop;
}

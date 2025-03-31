import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import Classes.Hhelper;
import Classes.Grlib;
import Classes.Album;
import Classes.Grahelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Pictureview extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	Grahelper gh = new Grahelper();
	private ArrayList<String> pictures = new ArrayList<>();
	int Index = 0;
	String actpath = "";
	int act_aid = 0;

	Pictureview() {
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
		lbheader = hh.fflabel("V I E W  P I C T U R E S");
		lbheader.setBounds(580, 20, 250, 40);
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

		lbalbum = hh.clabel("Choose an album");
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
					act_aid = alb.getAid();
					String name = alb.getName();
					start(path);
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
		lbnote.setBounds(100, 180, 150, 30);
		lbnote.setHorizontalAlignment(JLabel.CENTER);
		lPanel.add(lbnote);

		txanote = hh.cTextarea();
		txanote.setBounds(40, 220, 280, 80);
		txanote.setEditable(false);
		lPanel.add(txanote);

		btnprev = gr.sbcs("Previous");
		btnprev.setBounds(65, 330, 120, 35);
		btnprev.setBackground(Color.green);
		lPanel.add(btnprev);
		btnprev.addActionListener(e -> previous());

		btnnext = gr.sbcs("Next");
		btnnext.setBounds(185, 330, 120, 35);
		btnnext.setBackground(Color.green);
		lPanel.add(btnnext);
		btnnext.addActionListener(e -> next());

//		btndelete = gr.sbcs("Delete");
//		btndelete.setBounds(110, 380, 150, 35);
//		btndelete.setBackground(hh.piros);
//		lPanel.add(btndelete);
//		btndelete.addActionListener(e -> photodelete());

		btnclear = gr.sbcs("Clear");
		btnclear.setBounds(110, 430, 150, 35);
		btnclear.setBackground(hh.vkek);
		lPanel.add(btnclear);
		btnclear.addActionListener(e -> photoclear());
		setVisible(true);
	}

	private void start(String path) {
		Index = 0;
		if (!hh.zempty(path)) {
			String basePath = new File("").getAbsolutePath();
			String dir = basePath + "\\photoes\\" + path;
			actpath = dir;
			hh.readimages(dir, pictures);
			if (pictures.size() == 0) {
				return;
			}
			Showpict(pictures.get(Index));
			Index++;
		}
	}

	private void next() {
		int n = pictures.size();
		if (!pictures.isEmpty()) {
			Showpict(pictures.get(Index));
			Index += 1;
			if (Index >= n) {
				Index = 0;
			}
		}
	}

	private void previous() {
		int n = pictures.size();
		if (!pictures.isEmpty()) {
			Showpict(pictures.get(Index));
			if (Index > 0) {
				Index--;
			} else {
				Index = n - 1;
			}
		}
	}

	void Showpict(String filename) {
		File pctFile = new File(actpath + "\\" + filename);
		BufferedImage img;
		try {
			img = ImageIO.read(pctFile);
			int width = img.getWidth();
			int height = img.getHeight();
			if (width < 890 && height < 510) {
				showImage(img);
			} else {
				BufferedImage newimg = gh.resizeImage(img, 890, 510);
				showImage(newimg);
			}
		} catch (IOException e) {
			System.out.println("Picture do not load !");
			e.printStackTrace();
		}
		showpictdada(filename);
	}

	private void showpictdada(String filename) {
		String sql = "select name, note from pictures where aid=" + act_aid + " and path LIKE '%" + filename + "%'";
		try {
			rs = dh.GetData(sql);

			if (rs.next()) {
				String name = rs.getString("name");
				txpname.setText(name);
				String note = rs.getString("note");
				txanote.setText(note);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
	}

//	void photodelete() {
//
//	}

	private void photoclear() {
		lbimage.setIcon(null);
	}

	private void showImage(BufferedImage img) {
		lbimage.setIcon(new ImageIcon(img));
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Pictureview();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JLabel lbheader, lbimage, lbalbum, lbpicture, lbnote, lbpname;
	JPanel mPanel, lPanel, rPanel;
	JComboBox cmbalbums;
	JTextArea txanote;
	JTextField txpname;
	JButton btnnext, btnprev, btnclear, btndelete;

}

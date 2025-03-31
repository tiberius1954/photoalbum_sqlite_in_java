import javax.imageio.ImageIO;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import Classes.Hhelper;
import Classes.Grlib;
import Classes.Album;
import Classes.Grahelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Picture4view extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	Grahelper gh = new Grahelper();
	private ArrayList<String> pictures = new ArrayList<>();
	String actpath = "";

	int totalPages;
	int currentPage;
	final int PAGE_SIZE = 4;

	Picture4view() {
		initcomponents();
		hh.iconhere(this);
		dd.albumscombofill(cmbalbums);
	}

	void initcomponents() {
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
		setSize(1250, 700);
		setLayout(null);
		setLocationRelativeTo(null);
		getContentPane().setBackground(hh.skek);

		mPanel = new JPanel(null);
		mPanel.setBounds(0, 1, 1220, 640);
		mPanel.setBackground(hh.skek);
		add(mPanel);
		lbheader = hh.fflabel("V I E W  4  P I C T U R E S");
		lbheader.setBounds(35, 15, 280, 40);
		mPanel.add(lbheader);

		lbalbum = hh.clabel("Choose an album");
		lbalbum.setBounds(310, 20, 160, 28);
		lbalbum.setFont(new Font("Arial", 1, 18));
		lbalbum.setHorizontalAlignment(JLabel.CENTER);
		mPanel.add(lbalbum);

		cmbalbums = gr.grcombo();
		cmbalbums.setFocusable(true);
		cmbalbums.setName("albums");
		cmbalbums.setBounds(480, 20, 280, 35);
		mPanel.add(cmbalbums);
		cmbalbums.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Album alb = (Album) cmbalbums.getSelectedItem();
					String path = alb.getPath();
					String name = alb.getName();
					start(path);
				}
			}
		});

		p1Panel = new JPanel();
		p1Panel.setBounds(30, 80, 500, 260);
		p1Panel.setBackground(hh.skek);
		p1Panel.setBorder(hh.lineo);
		mPanel.add(p1Panel);

		p2Panel = new JPanel();
		p2Panel.setBounds(30, 370, 500, 260);
		p2Panel.setBackground(hh.skek);
		p2Panel.setBorder(hh.lineo);
		mPanel.add(p2Panel);

		p3Panel = new JPanel();
		p3Panel.setBounds(700, 80, 500, 260);
		p3Panel.setBackground(hh.skek);
		p3Panel.setBorder(hh.lineo);
		mPanel.add(p3Panel);

		p4Panel = new JPanel();
		p4Panel.setBounds(700, 370, 500, 260);
		p4Panel.setBackground(hh.skek);
		p4Panel.setBorder(hh.lineo);
		mPanel.add(p4Panel);

		btnnext = gr.sbcs("Next");
		btnnext.setBounds(550, 310, 140, 35);
		btnnext.setBackground(Color.green);
		mPanel.add(btnnext);
		btnnext.addActionListener(e -> next());

		btnprev = gr.sbcs("Previous");
		btnprev.setBounds(550, 370, 140, 35);
		btnprev.setBackground(Color.green);
		mPanel.add(btnprev);
		btnprev.addActionListener(e -> previous());

		lbp1pict = plabel();
		p1Panel.add(lbp1pict, BorderLayout.CENTER);
		lbp2pict = plabel();
		p2Panel.add(lbp2pict, BorderLayout.CENTER);
		lbp3pict = plabel();
		p3Panel.add(lbp3pict, BorderLayout.CENTER);
		lbp4pict = plabel();
		p4Panel.add(lbp4pict, BorderLayout.CENTER);
		p4Panel.add(lbp4pict);
		setVisible(true);
	}

	private void start(String path) {
		if (!hh.zempty(path)) {
			String basePath = new File("").getAbsolutePath();
			String dir = basePath + "\\photoes\\" + path;
			actpath = dir;
			hh.readimages(dir, pictures);
			if (pictures.size() == 0) {
				return;
			}
			totalPages = (int) Math.ceil(pictures.size() / PAGE_SIZE);
			currentPage = 0;
			Showpictures();
		}
	}

	private void next() {
		if (currentPage < totalPages) {
			currentPage++;
		} else {
			currentPage = 0;
		}
		Showpictures();
	}

	private void previous() {
		if (currentPage > 0) {
			currentPage--;
		} else {
			currentPage = totalPages;
		}
		Showpictures();
	}

	private void Showpictures() {
		int start;
		int n = pictures.size();
		start = currentPage * 4;
		if (start >= n) {
			return;
		}

		pictremove();
		if (start < n) {
			Showpict(pictures.get(start), lbp1pict);
		}
		if (start + 1 < n) {
			Showpict(pictures.get(start + 1), lbp2pict);
		}
		if (start + 2 < n) {
			Showpict(pictures.get(start + 2), lbp3pict);
		}
		if (start + 3 < n) {
			Showpict(pictures.get(start + 3), lbp4pict);
		}
	}

	private void pictremove() {
		lbp1pict.setIcon(null);
		lbp2pict.setIcon(null);
		lbp3pict.setIcon(null);
		lbp4pict.setIcon(null);
	}

	void Showpict(String filename, JLabel plabel) {
		File pctFile = new File(actpath + "\\" + filename);
		String spath = actpath + "\\" + filename;
		BufferedImage img = null;	
		try {
			img = ImageIO.read(pctFile);
			int width = img.getWidth();
			int height = img.getHeight();
			if (width < 490 && height < 250) {
				showImage(img, plabel);
			} else {
		//		BufferedImage image = gh.resize(img, 490, 250);
				BufferedImage image = gh.resizeImage(img, 490, 250);
				showImage(image, plabel);
			}
		} catch (IOException e) {
			System.out.println("Picture do not load !");
			e.printStackTrace();
		}
	}

	private void showImage(BufferedImage img, JLabel plabel) {
		plabel.setIcon(new ImageIcon(img));
	}

	public JLabel plabel() {
		JLabel llabel = new JLabel();
		llabel.setSize(490, 250);
		llabel.setPreferredSize(new Dimension(490, 250));
		llabel.setHorizontalAlignment(JLabel.CENTER);
		llabel.setVerticalAlignment(JLabel.CENTER);
		return llabel;
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Picture4view();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JPanel mPanel, p1Panel, p2Panel, p3Panel, p4Panel;
	JLabel lbheader, lbalbum, lbp1pict, lbp2pict, lbp3pict, lbp4pict;
	JComboBox cmbalbums;
	JButton btnnext, btnprev;
}

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import Classes.Album;
import Classes.Hhelper;
import Classes.Grlib;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Loadpictures extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	JFrame myframe = this;
	File lastDirectory = new File(System.getProperty("user.home"));
	File SOURCEFILE = null;

	Loadpictures() {
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
		lbheader = hh.fflabel("L O A D  P I C T U R E S");
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

		btnnewalbum = gr.sbcs("New album");
		btnnewalbum.setBounds(110, 100, 130, 35);
		btnnewalbum.setBackground(Color.yellow);
		btnnewalbum.setToolTipText("Add new album if not exit.");
		btnnewalbum.setFocusable(false);
		lPanel.add(btnnewalbum);

		btnnewalbum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Addalbum ob = new Addalbum(cmbalbums, myframe, "Loadpictures");
				ob.setVisible(true);
			}
		});

		lbpicture = hh.clabel("Name of the picture");
		lbpicture.setBounds(100, 140, 150, 30);
		lbpicture.setHorizontalAlignment(JLabel.CENTER);
		lPanel.add(lbpicture);

		txpname = hh.cTextField(30);
		txpname.setBounds(40, 180, 280, 30);
		lPanel.add(txpname);

		lbnote = hh.clabel("Note to the picture");
		lbnote.setBounds(100, 220, 150, 30);
		lbnote.setHorizontalAlignment(JLabel.CENTER);
		lPanel.add(lbnote);

		txanote = hh.cTextarea();
		txanote.setBounds(40, 260, 280, 80);
		lPanel.add(txanote);

		btnchoose = gr.sbcs("Choose photo");
		btnchoose.setBounds(110, 370, 150, 35);
		btnchoose.setBackground(Color.green);
		lPanel.add(btnchoose);
		btnchoose.addActionListener(e -> photochoose());

		btninsert = gr.sbcs("Insert");
		btninsert.setBounds(110, 420, 150, 35);
		btninsert.setBackground(hh.piros);
		lPanel.add(btninsert);
		btninsert.addActionListener(e -> photoinsert());

		btnclear = gr.sbcs("Clear");
		btnclear.setBounds(110, 470, 150, 35);
		btnclear.setBackground(hh.vkek);
		lPanel.add(btnclear);
		btnclear.addActionListener(e -> photoclear());

		setVisible(true);
	}

	private void photoinsert() {
		int aid = 0;
		String albumname = "";
		String path = "";
		String name = txpname.getText();
		String note = txanote.getText();
		String sdate = hh.currentDate();
		if (cmbalbums.getSelectedItem() != null) {
			Album alb = (Album) cmbalbums.getSelectedItem();
			aid = alb.getAid();
			albumname = alb.getName();
			path = alb.getPath();
		}
		if (hh.zempty(name) || hh.zempty(albumname) || hh.zempty(path)) {
			JOptionPane.showMessageDialog(null, "Please fill file name,  album name fields");
			return;
		}
		String filename = SOURCEFILE.getName();
		String basePath = new File("").getAbsolutePath();
		File destFile = new File(basePath + "\\photoes\\" + path + "\\" + filename);
		String relativePath = "./photoes/";
		String destpath = relativePath + path + "/" + filename;
		try {
			hh.copyFile(SOURCEFILE, destFile);
			String sql = "insert into pictures (aid, name,note, path, date) " + "values ('" + aid + "','" + name + "','"
					+ note + "','" + destpath + "','" + sdate + "')";
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
			}

		} catch (IOException e) {
			System.out.print("Copy file Error !");
			e.printStackTrace();
		}
	}

	private void photoclear() {
		lbimage.setIcon(null);
	}

	private void photochoose() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select an Image");
		// fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setCurrentDirectory(lastDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif");
		fileChooser.setFileFilter(filter);

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.exists()) {
				JOptionPane.showConfirmDialog((Component) null, "Photo " + file.getName() + " not available",
						"File not found.", JOptionPane.PLAIN_MESSAGE);
				return;
			}
			try {
				SOURCEFILE = file;
				lastDirectory = file.getParentFile();
				BufferedImage img = ImageIO.read(file);
				int width = img.getWidth();
				int height = img.getHeight();
				if (width < 950 && height < 510) {
					showImage(img);
				} else {
					resizeImage(img, 890, 510);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void showImage(BufferedImage img) {
		lbimage.setIcon(new ImageIcon(img));
	}

	private void resizeImage(BufferedImage img, int newWidth, int newHeight) {
		BufferedImage originalImage = img;
		Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	//	Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(resizedImage, 0, 0, null);
		g.dispose();
		originalImage.flush();
		originalImage=null;
		showImage(newImage);
	}

	public void passtocmb(int number) {
		dd.albumscombofill(cmbalbums);
		hh.setSelectedValue(cmbalbums, number);
		cmbalbums.updateUI();
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Loadpictures();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel mPanel, lPanel, rPanel;
	private JLabel lbpicture, lbheader, lbalbum, lbnote, lbimage;
	JComboBox cmbalbums;
	JButton btnnewalbum, btninsert, btnchoose, btnclear;
	JTextField txpname;
	JTextArea txanote;

}

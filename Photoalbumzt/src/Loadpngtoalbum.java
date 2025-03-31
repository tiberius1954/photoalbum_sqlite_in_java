import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Classes.Album;
import Classes.Grahelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Loadpngtoalbum extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	Grahelper gh = new Grahelper();
	JFrame myframe = this;
	File lastDirectory = new File(System.getProperty("user.home"));
	String Abspath = "";
	String basePath = new File("").getAbsolutePath();	
	File SOURCEFILE = null;
	int nwhich=0;	

	Loadpngtoalbum() {
		init();
		hh.iconhere(this);
		dd.albumscombofill(cmbalbums);
	}

	Loadpngtoalbum(boolean oke) {
		nwhich=1;	
		init();
		dd.albumscombofill(cmbalbums);	
	}

	private void init() {
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
		setSize(1020, 690);
		setLayout(null);
		setLocationRelativeTo(null);
		getContentPane().setBackground(hh.skek);
		mPanel = new JPanel(null);
		mPanel.setBounds(0, 1, 1000, 650);
		mPanel.setBackground(hh.skek);
		add(mPanel);
		lbheader = hh.fflabel("LOAD  PNG IMAGES TO ALBUM");
		lbheader.setBounds(360, 20, 310, 40);
		mPanel.add(lbheader);

		lPanel = new JPanel(null);
		lPanel.setBounds(6, 80, 990, 560);
		lPanel.setBackground(hh.skek);
		lPanel.setBorder(hh.lineo);
		mPanel.add(lPanel);

		lbalbum = hh.clabel("Album");
		lbalbum.setBounds(10, 30, 50, 30);
		lbalbum.setHorizontalAlignment(JLabel.CENTER);
		lPanel.add(lbalbum);

		cmbalbums = gr.grcombo();
		cmbalbums.setFocusable(true);
		cmbalbums.setName("albums");
		cmbalbums.setBounds(70, 30, 280, 28);
		lPanel.add(cmbalbums);

		btnnewalbum = gr.sbcs("New album");
		btnnewalbum.setBounds(355, 28, 165, 35);
		btnnewalbum.setBackground(Color.yellow);
		btnnewalbum.setToolTipText("Add new album if not exit.");
		btnnewalbum.setFocusable(false);
		lPanel.add(btnnewalbum);

		btnnewalbum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Addalbum ob = new Addalbum(cmbalbums, myframe, "Loadpngtoalbum");
				ob.setVisible(true);
			}
		});
		if (nwhich==0) {
		btnchoose = gr.sbcs("Choose photoes");
		btnchoose.setBackground(Color.green);
		btnchoose.setToolTipText("Choose png files from any directory");
		btnchoose.setBounds(525, 28, 165, 35);
		lPanel.add(btnchoose);
		btnchoose.addActionListener(e -> photochoose());
		}	

		btninsert = gr.sbcs("Insert");
		if (nwhich==0) {
		btninsert.setBounds(695, 28, 165, 35);
		}else {
			btninsert.setBounds(525, 28, 165, 35);
		}
		btninsert.setBackground(hh.piros);
		btninsert.setToolTipText("Pgn files load to any album");
		lPanel.add(btninsert);
		btninsert.addActionListener(e -> {
			try {
				photoinsert();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		ptable = hh.ztable();
		ptable.setRowHeight(105);

		// ptable.setBackground(hh.skek);
		ptable.setTableHeader(new JTableHeader(ptable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});

		String[] columnNames = { "Filename", "Path", "Mark", "Image" };
		Object[][] data = { { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
				{ null, null, null, null } };
		model = new DefaultTableModel(data, columnNames) {
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return Boolean.class;
				case 3:
					return Icon.class;
				default:
					return String.class;
				}
			}
		};

		ptable.setModel(model);
		ptable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				// DefaultTableModel m1 = (DefaultTableModel) ptable.getModel();
				try {
					int row = ptable.getSelectedRow();
//					if (row > -1) {
//						String path = m1.getValueAt(row, 4).toString();
//						String picpath = basePath + path;
//						Showpict(picpath);
//					}
				} catch (Exception e) {
					System.out.println("sql error!!!");
				}
			}
		});

		hh.madeheader(ptable);
		ptable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ptable.scrollRectToVisible(ptable.getCellRect(ptable.getRowCount() - 1, 0, true));
			}
		});

		jScrollPane = new JScrollPane(ptable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		hh.setJTableColumnsWidth(ptable, 950, 40, 40, 7, 13);
		jScrollPane.setViewportView(ptable);
		jScrollPane.setBounds(20, 90, 950, 450);
		jScrollPane.getViewport().setBackground(hh.skek);
		jScrollPane.setBorder(hh.lineo);
		lPanel.add(jScrollPane);
		setVisible(true);		
	}

	private void photochoose() {
		Boolean mark = Boolean.FALSE;
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Select an Image");
		fc.setCurrentDirectory(lastDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png");
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(true);
		int retVal = fc.showOpenDialog(this);
		try {
			if (retVal == JFileChooser.APPROVE_OPTION) {
				File[] selectedfiles = fc.getSelectedFiles();
				SOURCEFILE = selectedfiles[0];
				model.setRowCount(0);
				JDialog dialog = hh.makedialog(this);
				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
					@Override
					protected String doInBackground() throws Exception {
						System.out.println("dolgozom");
						for (int i = 0; i < selectedfiles.length; i++) {
							File file = selectedfiles[i];
							String filename = file.getName();
							String path = file.getAbsolutePath();
							if (i == 0) {
								File lastDirectory = file.getParentFile();
							}
							BufferedImage image = ImageIO.read(file);
							BufferedImage img = gh.createThumbnail(image, 100, 100);
						    image.flush();
						    image=null;
							Icon icon = new ImageIcon(img);						
							model.addRow(new Object[] { filename, path, mark, icon });							
						}
						return null;
					}

					@Override
					protected void done() {
						dialog.dispose();
						System.out.println("vége");
					}
				};
				worker.execute();
				dialog.setVisible(true);
				try {
					worker.get();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void photoinsert() throws IOException {
		String sdate = hh.currentDate();
		String dpath = "";
		String basePath = new File("").getAbsolutePath();
		String albumname = "";
		int aid = 0;
		if (cmbalbums.getSelectedItem() != null) {
			Album alb = (Album) cmbalbums.getSelectedItem();
			aid = alb.getAid();
			albumname = alb.getName();
			dpath = alb.getPath();
		}
		String albpath = dpath;
		if (hh.zempty(albumname) || hh.zempty(dpath)) {
			JOptionPane.showMessageDialog(null, "Please choose album.");
			return;
		}
		int rowCount = model.getRowCount();
		for (int row = 0; row < rowCount; row++) {
			Boolean bb = ((Boolean) model.getValueAt(row, 2));
			if (model.getValueAt(row, 0) != null && bb == true) {
				String filename = model.getValueAt(row, 0).toString();
				String path = model.getValueAt(row, 1).toString();
			//	String destpath = basePath + "\\photoes\\" + albpath + "\\" + filename;
				String relativePath = "./photoes/";
				String destpath = relativePath + albpath + "/" + filename;
				File destFile = new File(destpath);
				File sourceFile = new File(path);
				hh.copyFile(sourceFile, destFile);
				String sql = "insert into pictures (aid,path, date) values ('" + aid + "','" + destpath + "','" + sdate
						+ "')";
				int flag = dh.Insupdel(sql);
			}
		}
		model.setRowCount(0);
	}

	public void passtocmb(int number) {
		dd.albumscombofill(cmbalbums);
		hh.setSelectedValue(cmbalbums, number);
		cmbalbums.updateUI();
	}

	public void addtotable(DefaultTableModel pmodel) {
		model.setRowCount(0);
		int rowCount = pmodel.getRowCount();
		for (int row = 0; row < rowCount; row++) {
			Boolean bb = ((Boolean) pmodel.getValueAt(row, 2));
			if (pmodel.getValueAt(row, 0) != null && bb == true) {
				String filename = pmodel.getValueAt(row, 0).toString();	
				int nn = filename.lastIndexOf(".") + 1;
				String ufilename = filename.substring(0,nn);
                  ufilename = ufilename+"png";                  
				String path = basePath + "\\pngdir\\" +ufilename ;	
				ImageIcon icon =  (ImageIcon) pmodel.getValueAt(row, 3);					
				model.addRow(new Object[] { ufilename, path, true, icon });				
			}
		}
	}
	

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Loadpngtoalbum frame = new Loadpngtoalbum();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JPanel mPanel, lPanel;
	JLabel lbheader, lbalbum;
	JComboBox cmbalbums;
	JButton btnnewalbum, btnchoose, btninsert;
	JTable ptable;
	JScrollPane jScrollPane;
	DefaultTableModel model;
	Image image;
}

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
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

import Classes.Grahelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class JPGtoPNG extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	Grahelper gh = new Grahelper();
	JFrame myframe = this;
	File lastDirectory = new File(System.getProperty("user.home"));
	String Abspath ="";
	String basePath = new File("").getAbsolutePath();	

	JPGtoPNG() {
		initcomponents();
		hh.iconhere(this);
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
		setSize(920, 660);
		setLayout(null);
		setLocationRelativeTo(null);
		getContentPane().setBackground(hh.skek);

		mPanel = new JPanel(null);
		mPanel.setBounds(0, 1, 900, 610);
		mPanel.setBackground(hh.skek);
		add(mPanel);
		lbheader = hh.fflabel("CONVERT JPG TO PNG");
		lbheader.setBounds(360, 20, 250, 40);
		mPanel.add(lbheader);
		lPanel = new JPanel(null);
		lPanel.setBounds(6, 80, 890, 530);
		lPanel.setBackground(hh.skek);
		lPanel.setBorder(hh.lineo);
		mPanel.add(lPanel);

		btnchoose = gr.sbcs("Choose photoes");
		btnchoose.setBounds(200, 30, 170, 35);
		btnchoose.setBackground(Color.green);
		btnchoose.setToolTipText("Choose jpg files from any directory");
		lPanel.add(btnchoose);
		btnchoose.addActionListener(e -> photochoose());

		btnconvert = gr.sbcs("Convert photoes");
		btnconvert.setBounds(370, 30, 170, 35);
		btnconvert.setBackground(hh.narancs1);
		btnconvert.setToolTipText("Choosen jpg convert to pngdir directory");
		lPanel.add(btnconvert);
		btnconvert.addActionListener(e -> photoconvert());

		btnloading = gr.sbcs("Loading photoes");
		btnloading.setBounds(540, 30, 170, 35);
		btnloading.setBackground(hh.lpiros);
		btnloading.setToolTipText("Pgn files load to any album");
		lPanel.add(btnloading);
	    btnloading.addActionListener(e -> photoloading());

		ptable = hh.ztable();
		// ptable.setBackground(hh.skek);
		ptable.setTableHeader(new JTableHeader(ptable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});

		String[] columnNames = { "Filename","Path", "Mark","icon" };
//	Object[][] data = {};
		Object[][] data = { { null, null,null, null }, { null, null,null,null }, { null, null,null, null}, { null, null,null, null },
		{ null, null,null, null}, { null, null,null,null }, { null, null,null, null }, { null, null,null, null },
		{ null, null,null, null }, { null, null,null, null }, { null, null,null, null }, { null, null,null, null },
		{ null, null,null, null }, { null, null,null, null }};
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
//				if (row > -1) {
//					String path = m1.getValueAt(row, 4).toString();
//					String picpath = basePath + path;
//					Showpict(picpath);
//				}
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
	   hh.setJTableColumnsWidth(ptable, 850, 47,47, 6,0);
		jScrollPane.setViewportView(ptable);
		jScrollPane.setBounds(20, 90, 850, 380);
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
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg");
		fc.setFileFilter(filter);
		 fc.setMultiSelectionEnabled(true);		 
		 int retVal = fc.showOpenDialog(this);
			try {	
		 if (retVal == JFileChooser.APPROVE_OPTION) {
             File[] selectedfiles = fc.getSelectedFiles();
           model.setRowCount(0);        
             for (int i = 0; i < selectedfiles.length; i++) {
            	 File file = selectedfiles[i];                
        		String filename = file.getName();
        		 String path =file.getAbsolutePath();           		 
            	 if (i==0) {      		
          	              File lastDirectory = file.getParentFile();           	        
            	 }
            	 model.addRow(new Object[] { filename, path, mark, null });              
             }              
         }				
			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	private void photoconvert() {		
		int rowCount = model.getRowCount();	 
		String pngdir = basePath + "\\pngdir\\";
		File fpngdir = new File(pngdir);
		hh.purgedirectory(fpngdir);
			JDialog dialog = hh.makedialog(this);		    
		    SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
		        @Override
		        protected String doInBackground() throws Exception {
		            System.out.println("dolgozom");
		        	for (int row = 0; row < rowCount; row++) {		        	
		        		Boolean bb = ((Boolean) model.getValueAt(row, 2));	
		    			if (model.getValueAt(row, 0) != null && bb==true ) {
		    				String filename = model.getValueAt(row, 0).toString();
		    				String path = model.getValueAt(row, 1).toString();
		    				ImageIcon icon =convertfile(filename, path);	
		    				model.setValueAt(icon, row, 3);
		    				}			    		
		    			}		    
		           return null;
		        }
		        @Override
		        protected void done() {
		            dialog.dispose();
		            hh.ztmessage("Success", "Message");
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
	private  ImageIcon convertfile(String inputFile, String path) {	
		BufferedImage bImage=null;
		BufferedImage img=null;
		ImageIcon icon=null;
		try {			
			bImage = ImageIO.read(new File(path));
			// write the bufferedImage back to outputFile
			String outfile = inputFile.substring(0,inputFile.lastIndexOf("."));
			String outputFile = basePath + "\\pngdir\\" +outfile+".png" ;	
			ImageIO.write(bImage, "png", new File(outputFile));
			bImage.flush();
			BufferedImage image = ImageIO.read(new File(outputFile));
			 img = gh.createThumbnail(image, 100, 100);
			icon = new ImageIcon(img);
			img.flush();
			img=null;
		} catch (IOException e) {		
			e.printStackTrace();
		}	
		return icon;
	}
	private void photoloading() {
		boolean oke = true;
		Loadpngtoalbum lo = new Loadpngtoalbum(oke);
		lo.setVisible(true);	   
	 	lo.addtotable(model);	
		dispose();		
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JPGtoPNG frame =	new JPGtoPNG();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}

   JFrame frame;
	JLabel lbheader;
	JPanel mPanel, lPanel;
	JButton btnchoose, btnconvert, btnloading;
	JTable ptable;
	JScrollPane jScrollPane;
	DefaultTableModel model;
	 Timer tm;

}

//JOptionPane.showMessageDialog(null, panel, "Let's Play!", JOptionPane.QUESTION_MESSAGE, icon);

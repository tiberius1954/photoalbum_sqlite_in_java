
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import Classes.Album;
import Classes.Grahelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Tableview extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	Grahelper gh = new Grahelper();
	String rowid = "";
	int myrow = 0;
	String actpath = "";
	int act_aid;
	String basePath = new File("").getAbsolutePath();

	Tableview() {
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
		setSize(1350, 550);
		setLayout(null);
		setLocationRelativeTo(null);
		getContentPane().setBackground(hh.skek);

		mPanel = new JPanel(null);
		mPanel.setBounds(0, 1, 1333, 508);
		mPanel.setBorder(hh.lineo);
		mPanel.setBackground(hh.skek);
		add(mPanel);

		lbheader = hh.fflabel("TABLE VIEW");
		lbheader.setBounds(30, 40, 350, 30);
		mPanel.add(lbheader);

		lbalbum = hh.clabel("Choose an album");
		lbalbum.setBounds(270, 43, 160, 28);
		lbalbum.setFont(new Font("Arial", 1, 18));
		lbalbum.setHorizontalAlignment(JLabel.CENTER);
		mPanel.add(lbalbum);

		cmbalbums = gr.grcombo();
		cmbalbums.setFocusable(true);
		cmbalbums.setName("albums");
		cmbalbums.setBounds(440, 43, 280, 35);
		mPanel.add(cmbalbums);
		cmbalbums.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					act_aid = 0;
					Album alb = (Album) cmbalbums.getSelectedItem();
					String path = alb.getPath();
					act_aid = alb.getAid();
					String name = alb.getName();
					start();
				}
			}
		});

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
		ptable.setRowHeight(105);

		ptable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = ptable.getSelectedRow();
				if (row >= 0) {
					txname.setText(ptable.getValueAt(row, 2).toString());
					txanote.setText(ptable.getValueAt(row, 3).toString());
					rowid = ptable.getValueAt(row, 0).toString();
					myrow = row;
				}
			}
		});
		ptable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				try {
					int row = ptable.getSelectedRow();
					if (row >= 0) {
						rowid = ptable.getValueAt(row, 0).toString();
						myrow = row;
					}
				} catch (Exception e) {
					System.out.println("Error!!!");
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

		String[] columnNames = { "pid", "aid", "Name", "Note", "Path", "Date", "image" };

		Object[][] data = { { null, null, null, null, null, null, null }, { null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null }, { null, null, null, null, null, null, null } };

		model = new DefaultTableModel(data, columnNames) {
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return String.class;
				case 6:
					return Icon.class;
				default:
					return String.class;
				}
			}
		};
		ptable.setModel(model);

		hh.setJTableColumnsWidth(ptable, 900, 0, 0, 40, 35, 0, 10, 15);
		jScrollPane.setViewportView(ptable);
		jScrollPane.setBounds(15, 110, 900, 320);
		jScrollPane.getViewport().setBackground(hh.skek);
		jScrollPane.setBorder(hh.lineo);
		mPanel.add(jScrollPane);

		btndelete = gr.sbcs("Delete picture");
		btndelete.setBounds(410, 450, 170, 35);
		btndelete.setBackground(hh.narancs1);
		mPanel.add(btndelete);
		btndelete.addActionListener(e -> picturedelete());

		pPanel = new JPanel(null);
		pPanel.setBounds(925, 110, 390, 320);
		pPanel.setBorder(hh.lineo);
		pPanel.setBackground(hh.skek);

		lbname = hh.clabel("Name of the picture");
		lbname.setBounds(50, 10, 150, 30);
		lbname.setHorizontalAlignment(JLabel.CENTER);
		pPanel.add(lbname);

		txname = hh.cTextField(30);
		txname.setBounds(50, 50, 300, 30);
		pPanel.add(txname);

		lbnote = hh.clabel("Note to the picture");
		lbnote.setBounds(50, 100, 150, 30);
		lbnote.setHorizontalAlignment(JLabel.CENTER);
		pPanel.add(lbnote);

		txanote = hh.cTextarea();
		txanote.setBounds(50, 140, 300, 80);
		pPanel.add(txanote);

		btnsave = gr.sbcs("Save");
		btnsave.setBounds(85, 250, 120, 35);
		btnsave.setBackground(hh.lpiros);
		pPanel.add(btnsave);
		btnsave.addActionListener(e -> savebuttrun());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(205, 250, 120, 35);
		btncancel.setBackground(Color.green);
		pPanel.add(btncancel);
		btncancel.addActionListener(e -> clearFields());
		mPanel.add(pPanel);
		setVisible(true);
	}

	void start() {
		if (act_aid != 0) {
			String where = hh.itos(act_aid);
			JDialog dialog = hh.makedialog(this);		    
		    SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
		        @Override
		        protected String doInBackground() throws Exception {
		            System.out.println("dolgozom");
			           ptable_update(ptable, where);
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
	}

	public void ptable_update(JTable dtable, String what) {
		DefaultTableModel model = (DefaultTableModel) dtable.getModel();
		model.setRowCount(0);
		String Sql = "";
		if (what == "") {
			Sql = "select  pid, aid, name, note, path, date from pictures ";
		} else {
			Sql = "select  pid, aid, name, note, path, date  from pictures where aid='" + what + "'";
		}
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String pid = rs.getString("pid");
				String aid = rs.getString("aid");
				String name = rs.getString("name");
				String note = rs.getString("note");
				String path = rs.getString("path");
				String date = rs.getString("date");
				File file = new File(path);
				BufferedImage image;
				try {
					image = ImageIO.read(file);
					int h = image.getHeight();
					int w = image.getWidth();
					BufferedImage img = gh.createThumbnail(image, 100, 100);
					Icon icon = new ImageIcon(img);
					image.flush();
					image=null;
					model.addRow(new Object[] { pid, aid, name, note, path, date, icon });
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				clearFields();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		String[] fej = { "pic", "aid", "Name", "Note", "Path", "Date", "Image" };
		((DefaultTableModel) dtable.getModel()).setColumnIdentifiers(fej);
		hh.setJTableColumnsWidth(ptable, 900, 0, 0, 40, 35, 0, 10, 15);
		dtable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				dtable.scrollRectToVisible(dtable.getCellRect(dtable.getRowCount() - 1, 0, true));
			}
		});
		if (dtable.getRowCount() > 0) {
			int row = dtable.getRowCount() - 1;
			dtable.setRowSelectionInterval(row, row);
			rowid = ptable.getValueAt(row, 0).toString();
			myrow = row;
		}
	}

	private void savebuttrun() {
		DefaultTableModel d1 = (DefaultTableModel) ptable.getModel();
		String sql = "";
		String jel = "";
		String name = txname.getText();
		String note = txanote.getText();
		if (hh.zempty(name)) {
			JOptionPane.showMessageDialog(null, "Please fill name");
			return;
		}
		sql = "update  pictures set name= '" + name + "', note = '" + note + "' where pid = " + rowid;
		try {
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
				table_rowrefresh(name, note);
			}
		} catch (Exception e) {
			System.err.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "sql insert hiba");
		}
		clearFields();
	}

	private void table_rowrefresh(String name, String note) {
		DefaultTableModel d1 = (DefaultTableModel) ptable.getModel();
		d1.setValueAt(name, myrow, 2);
		d1.setValueAt(note, myrow, 3);
	}

	private void clearFields() {
		txname.setText("");
		txanote.setText("");
		txname.requestFocus();
		rowid = "";
		myrow = 0;
	}

	private void picturedelete() {
		DefaultTableModel m1 = (DefaultTableModel) ptable.getModel();
		int row = ptable.getSelectedRow();
		if (row >= 0) {
			String ppid = m1.getValueAt(row, 0).toString();
			String path = m1.getValueAt(row, 4).toString();
			File destFile = new File(path);
			if (destFile.exists()) {
				if (destFile.delete()) {
					String sql = "delete from pictures where pid=";
					dd.data_delete(ptable, sql);
					clearFields();
					System.out.print("Success");

				} else {
					System.out.print("Error !");
				}
			}
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Tableview();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JLabel lbheader, lbalbum, lbname, lbnote;
	JTextField txname;
	JTextArea txanote;
	JPanel mPanel, pPanel;
	JComboBox cmbalbums;
	JTable ptable;
	JScrollPane jScrollPane;
	JButton btndelete;
	DefaultTableModel model;
	JButton btnsave, btncancel;
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Classes.Hhelper;
import Classes.Grlib;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Addalbum extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	JComboBox returncmb;
	String rowid = "";
	int myrow = 0;
	String sfrom = "";
	private JFrame pframe;

	Addalbum() {
		initcomponents();
		hh.iconhere(this);
		dd.altable_update(altable, "");
	}

	public Addalbum(JComboBox mycombo, JFrame parent, String wfrom) {
		returncmb = mycombo;
		sfrom = wfrom;
		pframe = parent;
		initcomponents();
		dd.altable_update(altable, "");
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
		setSize(515, 500);
		setLayout(null);
		setLocationRelativeTo(null);
		getContentPane().setBackground(hh.skek);

		mPanel = new JPanel(null);
		mPanel.setBounds(0, 1, 515, 459);

		mPanel.setBackground(hh.skek);
		add(mPanel);

		lbheader = hh.fflabel("A L B U M S");
		// lbheader.setForeground(hh.feher);
		lbheader.setBounds(220, 20, 350, 30);
		mPanel.add(lbheader);

		lbname = hh.clabel("Album name");
		lbname.setBounds(20, 70, 110, 30);
		mPanel.add(lbname);

		txname = hh.cTextField(40);
		txname.setBounds(140, 70, 250, 30);
		mPanel.add(txname);
		// txname.addKeyListener( hh.MUpper());

		btnsave = gr.sbcs("Save");
		btnsave.setBounds(125, 125, 100, 35);
		btnsave.setBackground(hh.lpiros);
		mPanel.add(btnsave);
		btnsave.addActionListener(e -> savebutt());

		btndelete = gr.sbcs("Delete");
		btndelete.setBounds(230, 125, 100, 35);

		btndelete.setBackground(Color.yellow);
		mPanel.add(btndelete);
		btndelete.addActionListener(e -> data_delete());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(335, 125, 100, 35);

		btncancel.setBackground(Color.green);
		mPanel.add(btncancel);
		btncancel.addActionListener(e -> data_cancel());

		altable = hh.ztable();
		altable.setTableHeader(new JTableHeader(altable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});
		altable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = altable.getSelectedRow();
				if (row >= 0) {
					rowid = altable.getValueAt(row, 0).toString();
					txname.setText(altable.getValueAt(row, 1).toString());
					myrow = row;
				}
			}
		});

		hh.madeheader(altable);
		altable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				altable.scrollRectToVisible(altable.getCellRect(altable.getRowCount() - 1, 0, true));
			}
		});

		jScrollPane1 = new JScrollPane(altable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		altable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "aid", "Name", "Date", "Path" }));
		hh.setJTableColumnsWidth(altable, 440, 0, 70, 30, 0);
		jScrollPane1.setViewportView(altable);
		jScrollPane1.setBounds(30, 180, 440, 200);
		// jScrollPane1.setBorder(hh.borderf);
		mPanel.add(jScrollPane1);

		btnsendto = hh.cbutton("Send to load photoes");
		btnsendto.setBounds(145, 400, 210, 30);
		btnsendto.setBackground(hh.narancs);
		if (sfrom.equals("Loadpictures") || sfrom.equals("Loadpngtoalbum")) {
			mPanel.add(btnsendto);
		}
		btnsendto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				data_send();
			}
		});
		setVisible(true);
	}

	private void data_send() {
		DefaultTableModel d1 = (DefaultTableModel) altable.getModel();
		int row = altable.getSelectedRow();
		int number = 0;
		String cnum = "";
		if (row > -1) {
			cnum = d1.getValueAt(row, 0).toString();
			if (!hh.zempty(cnum)) {
				number = Integer.parseInt(cnum);
			}
			if (sfrom == "Loadpictures") {
				((Loadpictures) pframe).passtocmb(number);
			} else {
				((Loadpngtoalbum) pframe).passtocmb(number);
			}
			pframe.setVisible(true);
			dispose();
		}
	}

	private int data_delete() {
		String sql = " delete from albums where aid= ";
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) altable.getModel();
		int sIndex = altable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String did = d1.getValueAt(sIndex, 0).toString();
		if (did.equals("")) {
			return flag;
		}
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			String vsql = sql + did;
			flag = dh.Insupdel(vsql);
			if (flag > 0) {
				d1.removeRow(sIndex);
				String basePath = new File("").getAbsolutePath();
				String edir = txname.getText();
				if (!hh.zempty(edir)) {
					String pngdir = basePath + "\\photoes\\" + edir;
					File fpngdir = new File(pngdir);
					hh.deleteDir(fpngdir);
					sql = " delete from pictures where aid= ";
					vsql = sql + did;
					flag = dh.Insupdel(vsql);
				}
				clearFields();
			}
		}
		return flag;
	}

	private void savebutt() {
		DefaultTableModel d1 = (DefaultTableModel) altable.getModel();
		String sql = "";
		String path = "";
		int id = 0;
		String name = txname.getText();
		String cdate = hh.currentDate();

		if (hh.zempty(name) == true) {
			return;
		}
		if (hh.zempty(rowid)) {
			char oldC = ' ';
			char newC = '_';
			path = name.replace(oldC, newC);
		}

		try {
			if (rowid != "") {
				sql = "update  albums set name= '" + name + "' where aid = " + rowid;
			} else {
				sql = "insert into albums (name, createdate, path) values" + " ('" + name + "','" + cdate + "','" + path
						+ "')";
			}
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
				if (rowid == "") {
					int myid = dd.table_maxid("SELECT MAX(aid) AS max_id from albums");
					d1.insertRow(d1.getRowCount(), new Object[] { myid, name, cdate, path });
					hh.gotolastrow(altable);
					if (altable.getRowCount() > 0) {
						int row = altable.getRowCount() - 1;
						altable.setRowSelectionInterval(row, row);
					}
					if (sfrom.equals("loadpict")) {
						dd.albumscombofill(returncmb);
					}
					makealbumdir(path);
				} else {
					table_rowrefresh(name, cdate);
				}
			} else {
				JOptionPane.showMessageDialog(null, "sql error !");
			}
		} catch (Exception e) {
			System.err.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "sql insert hiba");
		}
		clearFields();
	}

	void clearFields() {
		txname.setText("");
		myrow = 0;
		rowid = "";
	}

	private void data_cancel() {
		clearFields();
		txname.requestFocus(true);
	}

	private void table_rowrefresh(String name, String cdate) {
		DefaultTableModel d1 = (DefaultTableModel) altable.getModel();
		d1.setValueAt(name, myrow, 1);
		d1.setValueAt(cdate, myrow, 2);
	}

	private void makealbumdir(String name) {
		String basePath = new File("").getAbsolutePath();
		String dirName = basePath + "\\photoes\\" + name + "\\";
		File theDir = new File(dirName);
		if (!theDir.exists()) {
			theDir.mkdirs();
		} else {
			JOptionPane.showMessageDialog(null, "This album already live. ");
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Addalbum();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JPanel mPanel;
	JLabel lbheader, lbname;
	JTextField txname;
	JButton btnsave, btndelete, btnsendto, btncancel;
	JTable altable;
	JScrollPane jScrollPane1;
}

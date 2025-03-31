package Databaseop;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import Classes.Hhelper;
import Classes.Album;

public class Databaseop {
	Connection con;
	Statement stmt;
	PreparedStatement pst;
	ResultSet rs;	
	DatabaseHelper dh = new DatabaseHelper();
	Hhelper hh = new Hhelper();

	public int data_delete(JTable dtable, String sql) {
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		int sIndex = dtable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String iid = d1.getValueAt(sIndex, 0).toString();
		if (iid.equals("")) {
			return flag;
		}	
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			String vsql = sql + iid;
			flag = dh.Insupdel(vsql);
			if (flag > 0) {
				d1.removeRow(sIndex);
			}
		}
		return flag;
	}	

	public void rtable_delete(JTable dtable, String sql) {
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		int flag = dh.Insupdel(sql);
		if (flag > 0) {
			d1.setRowCount(0);
		}
	}	
	
	public int tdata_delete(JTable dtable, String sql, int row) {
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();	
		flag = dh.Insupdel(sql);
		if (flag == 1) {
			d1.removeRow(row);
		}
		return flag;
	}


	public int table_maxid(String sql) {
		int myid = 0;
		try {
			
			rs = dh.GetData(sql);
			if (!rs.next()) {
				System.out.println("Error.");
			} else {
				myid = rs.getInt("max_id");
			}
			dh.CloseConnection();
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			ex.printStackTrace();
		}
		return myid;
	}

	public Boolean cannotdelete(String sql) {
		Boolean found = false;
		rs = dh.GetData(sql);
		try {
			if (rs.next()) {
				found = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dh.CloseConnection();
		return found;
	}
	
	public void albumscombofill(JComboBox ccombo) {	
		ccombo.removeAllItems();
		Album A = new Album(0, " "," ");
		ccombo.addItem(A);
		String sql = "select aid, name, path  from albums order by upper(name)";
		try {
			ResultSet rs = dh.GetData(sql);
			while (rs.next()) {
				A = new Album(rs.getInt("aid"), rs.getString("name"), rs.getString("path"));
				ccombo.addItem(A);
			}
			dh.CloseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void altable_update(JTable dtable, String what) {
		DefaultTableModel m1 = (DefaultTableModel) dtable.getModel();
		m1.setRowCount(0);
		String Sql = "";
		if (what == "") {
			Sql = "select  aid, name, createdate, path from albums";				
		} else {
			Sql = "select  aid, name, createdate, path  from albums where "  + what ;
		}
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String aid= rs.getString("aid");
				String name= rs.getString("name");
				String cdate = rs.getString("createdate");
				String path = rs.getString("path");
							
				m1.addRow(new Object[] { aid, name, cdate, path});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();			
		}	
		String[] fej = { "aid","Name", "Date","Path"};		
		((DefaultTableModel) dtable.getModel()).setColumnIdentifiers(fej);
		hh.setJTableColumnsWidth(dtable, 440, 0,70,30,0);	
//		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) dtable.getDefaultRenderer(Object.class);
	//	renderer.setHorizontalAlignment(SwingConstants.LEFT);	
	
		dtable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				dtable.scrollRectToVisible(dtable.getCellRect(dtable.getRowCount() - 1, 0, true));
			}
		});
		if (dtable.getRowCount() > 0) {
			int row = dtable.getRowCount() - 1;
			dtable.setRowSelectionInterval(row, row);
		}
	}
}

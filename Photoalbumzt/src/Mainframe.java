import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.swing.*;

import Classes.Hhelper;

public class Mainframe extends JFrame implements ActionListener {
	Color dblue = new Color(52, 72, 95);
	Color yellow = new Color(254, 196, 1);
	Color sblue = new Color(71, 80, 111);
	Hhelper hh = new Hhelper();
	
	Mainframe() {
		init();		
		 ifdirectory();
	}
private void init() {
	lbheader = new JLabel();
	lbheader.setFont(new Font("Footlight MT Light", 1, 36));
	lbheader.setForeground(yellow);
	lbheader.setText("PHOTO ALBUM");
	lbheader.setBounds(300, 60, 600, 50);
	add(lbheader);

	cp = getContentPane();
	cp.setBackground(dblue);
	
	
	btnslideshow = xbutton("Slideshow");
	btnslideshow.setBounds(160, 140, 230, 35);
	add(btnslideshow);
	btnslideshow.addActionListener(this);
	
	btnpictureview = xbutton("Picture view");
	btnpictureview.setBounds(160, 200, 230, 35);
	add(btnpictureview);
	btnpictureview.addActionListener(this);
	
	btntableview = xbutton("Table view");
	btntableview.setBounds(160, 260, 230, 35);
	add(btntableview);
	btntableview.addActionListener(this);
	
	btn4view = xbutton("Picture 4  view");
	btn4view.setBounds(160, 320, 230, 35);
	add(btn4view);
	btn4view.addActionListener(this);
	
	btnexit = xbutton("Exit");
	btnexit.setBounds(290, 380, 230, 35);
	add(btnexit);	
//	btnexit.addActionListener(this);
	btnexit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int x, y, d;
			x = 800;
			y = 500;
			d = 10;
			while (x > 0 && y > 0) {
				setSize(x, y);
				x = x - 2 * d;
				y = y - d;
				setVisible(true);
				try {
					Thread.sleep(10);
				} catch (Exception e1) {
					System.out.println("Error:" + e1);
				}
			}
			dispose();
		}
	});


	btnnewalbum = xbutton("Albums");
	btnnewalbum.setBounds(410, 140, 230, 35);
	add(btnnewalbum);
	btnnewalbum.addActionListener(this);
	
	btnloadpicture = xbutton("Load picture into album");
	btnloadpicture.setBounds(410, 200, 230, 35);
	add(btnloadpicture);
	btnloadpicture.addActionListener(this);
	
	btnloadpngtoalbum = xbutton("Load png into album");
	btnloadpngtoalbum.setBounds(410, 260, 230, 35);
	add(btnloadpngtoalbum);
	btnloadpngtoalbum.addActionListener(this);
	
	btnjpgtopng = xbutton("Convert jpg  to png");
	btnjpgtopng.setBounds(410, 320, 230, 35);
	add(btnjpgtopng);
	btnjpgtopng.addActionListener(this);

}
	private void ifdirectory() {
		String basePath = new File("").getAbsolutePath();
		String dirName = basePath + "\\photoes\\";
		File theDir = new File(dirName);
		if (!theDir.exists())
			theDir.mkdirs();
		dirName = basePath + "\\pngdir\\";
		theDir = new File(dirName);
		if (!theDir.exists())
			theDir.mkdirs();		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	//	if (e.getSource() == btnexit) {
		//	dispose();	
		if (e.getSource() == btnslideshow) {
			Slideshow ob = new Slideshow();				
		} else if (e.getSource() == btnpictureview) {
			 Pictureview ob = new  Pictureview();
		}  else if (e.getSource() == btntableview) {
			 Tableview ob = new  Tableview();
		} else if (e.getSource() == btn4view) {
			Picture4view ob = new Picture4view();
		}else if (e.getSource() == btnnewalbum) {
			Addalbum ob = new Addalbum();
		}else if (e.getSource() == btnnewalbum) {
			Addalbum ob = new Addalbum();
		}else if (e.getSource() == btnloadpicture) {
                      Loadpictures ob = new  Loadpictures();
		} else if (e.getSource() == btnloadpngtoalbum) {
            Loadpngtoalbum ob = new  Loadpngtoalbum();
       } else if (e.getSource() == btnjpgtopng) {
           JPGtoPNG ob = new  JPGtoPNG();
} 
	}

	public static void main(String args[]) {
		Mainframe Main = new Mainframe();
		Main.setUndecorated(true);
		Main.setSize(800, 500);
		Main.setLayout(null);
		Main.setLocationRelativeTo(null);
		Main.setShape(new RoundRectangle2D.Double(10, 10, 780, 480, 50, 50));
		Main.setVisible(true);
	}
	public JButton xbutton(String string) {
		JButton bbutton = new JButton(string);
		bbutton.setBorder(hh.myRaisedBorder);
		bbutton.setForeground(sblue);
		bbutton.setBackground(yellow);
		bbutton.setFont(new Font("Tahoma", Font.BOLD, 18));
		bbutton.setPreferredSize(new Dimension(100, 30));
		bbutton.setMargin(new Insets(10, 10, 10, 10));
		bbutton.setFocusable(false);
		bbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return bbutton;
	}
JLabel lbheader;
JButton btnnewalbum, btnslideshow, btnpictureview, btntableview, btn4view, btnexit,
btnloadpicture, btnloadpngtoalbum, btnjpgtopng;
Container cp;
}

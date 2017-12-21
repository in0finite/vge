package poop2;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Menu {

	
	static	JMenuBar	createMenu() {
		
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;
		
		menuBar = new JMenuBar();
		
		//Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
	//	menu.getAccessibleContext().setAccessibleDescription("");
		menuBar.add(menu);

		
		menuItem = new JMenuItem("New", KeyEvent.VK_N);
		menuItem.addActionListener( (e) -> {
			Editor.closeScene();
		} );
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Open");
		menuItem.addActionListener( (e) -> {
			load();
		} );
		menu.add(menuItem);

		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.addActionListener( (e) -> {
			if(Editor._sceneHasFilePath) {
				// save scene to current file path
				Editor.saveSceneToFile(Editor._sceneFilePath);
			} else {
				// prompt user for file path
				promptForFilePathAndSave();
			}
		} );
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save As");
		menuItem.addActionListener( (e) -> {
			// prompt user for file path
			promptForFilePathAndSave();
		} );
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Close");
		menuItem.addActionListener( (e) -> {
			Editor.closeScene();
		} );
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener( (e) -> {
			System.exit(0);
		} );
		menu.add(menuItem);
		
		/*
		menuItem = new JMenuItem("Both text and icon",
		                         new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menu.add(menuItem);

		//a group of radio button menu items
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		//a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		menu.add(cbMenuItem);

		//a submenu
		menu.addSeparator();
		submenu = new JMenu("A submenu");
		submenu.setMnemonic(KeyEvent.VK_S);

		menuItem = new JMenuItem("An item in the submenu");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_2, ActionEvent.ALT_MASK));
		submenu.add(menuItem);

		menuItem = new JMenuItem("Another item");
		submenu.add(menuItem);
		menu.add(submenu);
		*/

		//Build second menu in the menu bar.
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		
		menuItem = new JMenuItem("About");
		menuItem.addActionListener( (e) -> {
			showAboutDialog();
		} );
		menu.add(menuItem);
		
		
		menuBar.add(menu);
		
		
		return menuBar ;
	}
	
	private	static	void	promptForFilePathAndSave() {
		
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save");
		fc.setFileFilter(new FileNameExtensionFilter("VGE scene file", "vge"));
		int result = fc.showSaveDialog(Editor._frame);
		
		if( JFileChooser.APPROVE_OPTION == result ) {
			Editor._sceneHasFilePath = true ;
			Editor._sceneFilePath = fc.getSelectedFile().getAbsolutePath() ;
			Editor.saveSceneToFile(Editor._sceneFilePath);
			Editor.updateGraphics();
		}
		
	}
	
	private	static	void	load() {
		
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open");
		fc.setFileFilter(new FileNameExtensionFilter("VGE scene file", "vge"));
		int result = fc.showOpenDialog(Editor._frame);
		
		if( JFileChooser.APPROVE_OPTION == result ) {
			String path = fc.getSelectedFile().getAbsolutePath();
			if( Editor.loadSceneFromFile(path) ) {
				Editor._sceneHasFilePath = true ;
				Editor._sceneFilePath = path ;
			}
			Editor.updateGraphics();
		}
		
	}
	
	private	static	void	showAboutDialog() {
		
		JDialog dialog = new JDialog(Editor._frame, "About", true);
		
		int width = 400 ;
		int height = 300 ;
		Rectangle rect = Editor._frame.getBounds();
		rect.x += rect.width / 2 - width / 2 ;
		rect.y += rect.height / 2 - height / 2 ;
		rect.width = width;
		rect.height = height;
		dialog.setBounds(rect);
		
		dialog.setLayout(new BorderLayout());
		
		String text = "<html>VGE - vector graphics editor <br><br> Created by  <FONT color=blue>in0finite</FONT> <br><br> License: GPL v3</html>";
		
		JLabel lbl = new JLabel(text, JLabel.CENTER);
		dialog.add(lbl, BorderLayout.CENTER);
		
		dialog.setVisible(true);
	}
	

}

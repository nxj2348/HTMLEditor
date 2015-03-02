/**
 * 
 */
package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import cmd.SaveFile;
import cmd.UpdateText;
import files.HTMLFile;

/**
 * Represents a tab in the UI
 * 
 * @author nick
 *
 */
public class Tab extends JTextPane {
	/**
	 * The html file to be edited
	 */
	private HTMLFile file;

	/**
	 * Is this tab the focus of the window?
	 */
	private boolean focus;

	/**
	 * The area for tabs
	 */
	private JTabbedPane j;

	/**
	 * Constructs a tab with a new file
	 */
	public Tab() {
		super();
		this.file = new HTMLFile();
	}

	/**
	 * Constructs a tab with an opened file
	 * 
	 * @param file
	 *            - file to be edited
	 * @param j
	 *            - the area of tabs for this to be attached
	 */
	public Tab(HTMLFile file, JTabbedPane j) {
		super();
		this.file = file;
		this.j = j;
		this.setText(file.getText());
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				UpdateText t = new UpdateText((Tab) e.getSource(),
						(JTabbedPane) e.getComponent().getParent());
				t.execute();
			}

		});
	}

	public void close() {
		// TODO close tab method
		if (file.getNeedsToBeSaved()) {
			int reply = JOptionPane
					.showConfirmDialog(
							null,
							"Would you like to save changes to file: "
									+ file.getName(), "Save Changes?",
							JOptionPane.WARNING_MESSAGE);
			if (reply == JOptionPane.YES_OPTION) {
				SaveFile s = new SaveFile(this); // NEED a better way
				s.execute();
			}
		}
	}

	public HTMLFile getFile() {
		return file;
	}

	public boolean getFocus() {
		return focus;
	}

	public void setFocus(boolean b) {
		focus = b;
	}
}

/**
 * 
 */
package cmd;

import java.io.FileNotFoundException;

import ui.EditorWindow;
import ui.MessageBox;

import javax.swing.JOptionPane;
import files.HTMLFile;

/**
 * Represents the Open command to open a file
 * 
 * @author Nick James
 * @author Matt Gallagher
 *
 */
public class OpenFile implements Command {

	/**
	 * The path to the file
	 */
	private String path;
	private EditorWindow ew;
	/**
	 * OpenFile Constructor
	 * 
	 * @param w
	 *            - The application window
	 * @param j
	 *            - The pane of tabs in the application window
	 */
	public OpenFile(String path,EditorWindow e) {
		this.path = path;
		ew = e;
	}

	/**
	 * Executes the openFile command: Opens a File Browser to load a file If
	 * file cannot be loaded, error message displays The load is cancelled A tab
	 * with the file's html code is added to the window
	 */
	@Override
	public void execute() {
		EditorWindow w = ew;

		HTMLFile f = new HTMLFile();
		
		try {
			String str = f.load(path);
			w.NewTab(f, str);
		}
		catch (FileNotFoundException e) {
			new MessageBox("Error", "File cannot be opened. Is it already in use?", JOptionPane.ERROR_MESSAGE);
		}
	}
}

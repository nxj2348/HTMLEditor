import javax.swing.UIManager;

import cmd.OpenFile;
import ui.EditorWindow;
import ui.MessageBox;


public class User {
	public static void main(String[] args) {

		// Try using the user's system theme
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			// We don't really care if we can't update the theme
		}
		
		EditorWindow w = EditorWindow.getInstance();
		w.setVisible(true);
		
		for(String s : args) {
			new OpenFile(s).execute();
		}
	}
}

package ui;
import javax.swing.*;

import files.HTMLFile;

public class Tab extends JTextPane {
	private HTMLFile file;
	private boolean focus; 
	public Tab() {
		super();
		this.file = new HTMLFile();
	}
	
	public Tab(HTMLFile file) {
		super();
		this.file = file;
		this.setText(file.getText());
	}
	
	public void close(){
		// TODO close tab method
		//Prompt to save if changes have been made
		
		
	}
	public HTMLFile getFile(){
		return file;
	}
	public boolean getFocus(){
		return focus;
	}
	public void setFocus(boolean b){
		focus = b;
	}
}

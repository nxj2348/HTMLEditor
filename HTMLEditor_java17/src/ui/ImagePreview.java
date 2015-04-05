/**
 * 
 */
package ui;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Nick James
 *
 */
public class ImagePreview extends JFrame{
	
	JLabel label;
	
	/**
	 * Constructor
	 * @param image - the image to preview
	 */
	public ImagePreview(Image image) {

		this.label = new JLabel();
		this.label.setIcon(new ImageIcon(image));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(label);
		this.pack();
		this.setLocation(200, 200);

	}

}

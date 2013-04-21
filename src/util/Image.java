package util;

import javax.swing.ImageIcon;

public class Image {
	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path) {
		return new ImageIcon(path);
	}
}

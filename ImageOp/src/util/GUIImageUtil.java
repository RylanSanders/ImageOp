package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUIImageUtil {
	public static void getImageFrame(BufferedImage img) {
		JFrame frame = new ImageFrame(img);
		frame.setSize(img.getWidth(), img.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static BufferedImage getDot() {
		BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.fillOval(100, 100, 300, 300);
		g.dispose();
		return img;
	}
	
	public static Color getRandomColor() {
		int r =(int)( Math.random()*255);
		int g =(int)( Math.random()*255);
		int b =(int)( Math.random()*255);
		return new Color(r,g,b);
	}
}

class ImageFrame extends JFrame {
	Image img;

	public ImageFrame(Image img) {
		super("Test Image Frame");
		this.img = img;
	}
	
	public ImageFrame() {
		super("Test Image Frame");
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}

package imageOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
public class GifUtil {
	
	//TODO make a version of this that uses the Python implementation
	public static void generateGif(List<BufferedImage> imgs, String outputFile, int delay) {
		try{
		BufferedImage first = imgs.get(0);
		File resFile = new File(outputFile);
		if(!resFile.exists())
			resFile.createNewFile();
		ImageOutputStream output = new FileImageOutputStream(resFile);
		GifSequenceWriter writer = new GifSequenceWriter(output, first.getType(), delay, true);
	    writer.writeToSequence(first);
	    
	    
	    for (BufferedImage img : imgs) {
	        writer.writeToSequence(img);
	    }
	    
	    writer.close();
	    output.close();
	    
	    System.out.println("Done Generating Gif");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void emptyDir(String path) {
		System.out.println("Empty Dir: " + path + "/");
		File dir = new File(path);
		for(File file: dir.listFiles()) {
			if(file.isDirectory())
				emptyDir(file.getPath());
		    file.delete();
		}
	}


	public static void waiter() {
		try {
			Thread.sleep(10);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

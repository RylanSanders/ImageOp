package imageOp;

import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

public class ImageUtil {

	
	public static BufferedImage toPixelImage(BufferedImage input, int width, int height) {
		int originalWidth = input.getWidth();
		int originalHeight = input.getHeight();
		
		BufferedImage temp = rescaleImage(input, width, height);
		
		return rescaleImage(temp, originalWidth, originalHeight);
	}
	
	public static BufferedImage toPixelImage(BufferedImage input, double percentage) {
		int width = (int)(input.getWidth()*percentage);
		int height = (int)(input.getHeight()*percentage);
		
		return toPixelImage(input, width, height);
	}
	
	public static BufferedImage toDifferentColorSpace(BufferedImage input, int colorSpaceConst) {
		ColorSpace colorSpace = ColorSpace.getInstance(colorSpaceConst);
		ColorConvertOp op = new ColorConvertOp(colorSpace, null);
		
		return op.filter(input, null);
	}
	
	public static BufferedImage rescaleImage(BufferedImage input, int width, int height) {
		double widthRatio = (double)width/(double)input.getWidth();
		double heightRatio = (double)height/(double)input.getHeight();
		
		AffineTransform trans = AffineTransform.getScaleInstance(widthRatio, heightRatio);
		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(input, null);
	}
	
	public static BufferedImage rescaleImage(BufferedImage input, double percentage) {
		int width = (int)(input.getWidth()*percentage);
		int height = (int)(input.getHeight()*percentage);
		
		return rescaleImage(input, width, height);
	}
	
	//TODO handle the black edges, resize to fit
	//TODO possible change to the more efficient model on the Filthy Rich Clients github
	//Modified from Filthy Rich Clients
	public static BufferedImage boxBlurImage(BufferedImage input, int radius) {
		int size = 2*radius+1;
		float weight = 1.0f/(size*size);
		float[] data = new float[size*size];
		
		for(int i =0; i<data.length; i++)
			data[i] = weight;
		
		Kernel kernel = new Kernel(size, size, data);
		ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		return op.filter(input, null);
	}
	
	//TODO handle the black edges, probably a rendering hint
	//TODO possible change to the more efficient model on the Filthy Rich Clients github
	//Modified from Filthy Rich Clients
	public static BufferedImage gaussianBlurImage(BufferedImage input, int radius, boolean horizontal) {
		int size = radius * 2 + 1;
		float[] data = new float[size];
		
		float sigma = radius/3.0f;
		float twoSigSquare = 2.0f*sigma*sigma;
		float sigmaRoot = (float)Math.sqrt(twoSigSquare*Math.PI);
		float total = 0.0f;
		
		for(int i = -radius; i<=radius;i++) {
			float distance = i*i;
			int index = i + radius;
			data[index] = (float) Math.exp(-distance/twoSigSquare)/sigmaRoot;
			total += data[index];
		}
		
		for(int i=0;i<data.length; i++)
			data[i]/=total;
		
		Kernel kernel = null;
		if(horizontal)
			kernel = new Kernel(size, 1, data);
		else
			kernel = new Kernel(1, size, data);
		
		ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		return op.filter(input, null);
		
	}
	
	public static BufferedImage optimizedGaussianBlurImage(BufferedImage input, int radius, boolean horizontal) {
		BufferedImage temp = rescaleImage(input, 0.5);
		temp = gaussianBlurImage(temp, radius, horizontal);
		return rescaleImage(temp, 2.0);
	}
	
	public static BufferedImage changeBrightness(BufferedImage input, float brightness) {
		float[] factors = new float[] {brightness, brightness, brightness};
		float[] offsets = new float[] {0, 0, 0};
		
		RescaleOp op = new RescaleOp(factors, offsets, null);
		return op.filter(input, null);
		}

	
	public static BufferedImage simpleShapenImage(BufferedImage input) {
		float[] arr = new float[] {0, -1.0f, 0,
									-1.0f, 5.0f, -1.0f,
									0, -1.0f, 0 };
		Kernel kernel = new Kernel(3, 3, arr);
		ConvolveOp op = new ConvolveOp(kernel);
		return op.filter(input, null);
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
}

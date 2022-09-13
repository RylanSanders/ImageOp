package imageOp;

import java.awt.Color;

public class CommonFunctions {

	public static Color getColorBW(double val) {
		int color = Color.HSBtoRGB(0, 0, (int)val);
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		return new Color(red, green, blue);
	}
	
	public static Color getColor(double val) {
		
		if(val<1)
			//Deep Sea
			return new Color(63, 44, 113);
		if(val<3)
			//Sea
			return new Color(63, 64, 227);
		if(val<5)
			//Light Water
			return new Color(88, 161, 219);
		if(val<6)
			//Sand
			return new Color(229, 194, 67);
		if(val<10)
			//Dark Grass
			return new Color(70, 114, 52);
		if(val<12)
			//Light Grass
			return new Color(70, 174, 52);
		if(val<20)
			//Mountain
			return new Color(164, 97, 41);
		//Snow
		return new Color(209, 237, 251);
	}
}

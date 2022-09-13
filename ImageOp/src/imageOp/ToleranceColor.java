package imageOp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.GUIImageUtil;

public class ToleranceColor{
	
	
	public static BufferedImage applyMap(Color[] colMap, BufferedImage img) {
		for(int y=0;y<img.getHeight();y++) {
			for(int x=0;x<img.getWidth();x++) {
				int intCol = img.getRGB(x, y);
				Color col = intToColor(intCol);
				Color newCol = findColor(colMap, col);
				img.setRGB(x, y, newCol.getRGB());
			}
		}
		return img;
	}
	
	
	public static Color intToColor(int col) {
		int a = ((col >>24) & 0xFF);
		int r = ((col >>16) & 0xFF);
		int g = ((col >>8) & 0xFF);
		int b = ((col >>0) & 0xFF);
		return new Color(r,g,b);
	}
	
	
	public static Color findColor(Color[] colMap, Color c) {
		int min = 100000;
		Color toRet = Color.black;
		for(int i=0;i<colMap.length;i++) {
			int diff = getColDiff(colMap[i], c);
			if(diff <min) {
				min = diff;
				toRet =colMap[i];
			}
		}
		
		return toRet;
	}
	
	public static Color[] colMap(int numSections) {
		Color[] map = new Color[numSections];
		for(int i=0;i<numSections;i++) {
			map[i] = GUIImageUtil.getRandomColor();
		}
		return map;
	}
	
	public static Color[] mostColMap(int numSections, BufferedImage img) {
		Color[] map = new Color[numSections];
		Map<Integer, Integer> popColMap = new HashMap<>();
		for(int y=0;y<img.getHeight();y++) {
			for(int x=0;x<img.getWidth();x++) {
				int newCol = img.getRGB(x, y);
				if(popColMap.containsKey(newCol))
					popColMap.put(newCol, popColMap.get(newCol)+1);
				else
					popColMap.put(newCol, 0);
			}
		}
		
		List<Integer> cols = new ArrayList<>();
		cols.addAll(popColMap.keySet());
		cols.sort((c1,c2)->popColMap.get(c1)-popColMap.get(c2));
		
		for(int i=0;i<map.length;i++) {
			String hexColor = String.format("#%06X", (0xFFFFFF & cols.get(i)));
			map[i] = Color.decode(hexColor);
		}
		return map;
	}
	
	
	public static int getColDiff(Color c1, Color c2) {
		int r = Math.abs(c1.getRed() - c2.getRed());
		int g = Math.abs(c1.getGreen() - c2.getGreen());
		int b = Math.abs(c1.getBlue() - c2.getBlue());
		
		return r+g+b;
	}
	
	
}
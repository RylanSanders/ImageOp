package imageOp;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.*;

public class Mappers {

	public static List<Point> pointsToLines(List<Point> points){
		String p = CmdUtil.getLongPythonOutput("TriangleGenerator.py", pointsToString(points), "q");
		return StringToPoints(p);
	}
	
	private static String pointsToString(List<Point> points) {
		String s =points.stream().map(p->p.x + " " + p.y + "p").collect(Collectors.joining());
		return s.substring(0, s.length()-1);
	}
	
	private static List<Point> StringToPoints(String str){
		String stripped = str.substring(2, str.length()-2);
		String[] points = stripped.split("\\] \\[");
		List<Point> Ps = new ArrayList<>();
		for(String point:points) {
			String[] parts = point.trim().replace("  ", " ").split(" ");
			List<String> finalParts = new ArrayList<>();
			for(String part : parts)
				if(!removeBrackets(part).trim().equals(""))
					finalParts.add(removeBrackets(part));
			Ps.add(new Point((int)(Double.parseDouble(finalParts.get(0))), (int)(Double.parseDouble(finalParts.get(1)))));
		}
		return Ps;
	}
	
	private static String removeBrackets(String str) {
		return str.replace("[", "").replace("]", "").trim();
	}
	
}

package util;

public class NumUtil {

	public static int rand(int max) {
		return (int)(Math.random()*max);
	}
	
	public static int rand(int start, int range) {
		return (int)(Math.random()*range + start);
	}

}

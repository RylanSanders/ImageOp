package util;

public class ThreadUtil {
	private static long lastTime;
	public static void waitThread(int ms) {
		try {
			Thread.sleep(ms);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void startTimer() {
		lastTime = System.currentTimeMillis();
	}
	
	public static void printTime() {
		System.out.println(System.currentTimeMillis()-lastTime);
	}
	
	public static void printTime(String str) {
		System.out.println(str + Long.toString(System.currentTimeMillis()-lastTime));
	}
	
	public static void chainTime() {
		printTime();
		lastTime = System.currentTimeMillis();
	}
	
	public static void chainTime(String str) {
		printTime(str);
		lastTime = System.currentTimeMillis();
	}
}

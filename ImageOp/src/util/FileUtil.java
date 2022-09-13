package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.stream.Collectors;

public class FileUtil {
	public static void emptyDir(String path) {
		emptyDir(new File(path));
	}
	
	public static void emptyDir(File dir) {
		for(File file : dir.listFiles()) {
			if(!file.isDirectory())
				file.delete();
			else
				emptyDir(file);
		}
	}
	
	public static void writeTxtFile(String path, String txt) {
		try {
			File f = new File(path);
			if(!f.exists()){
				f.createNewFile();
			}
			FileWriter myWriter = new FileWriter(f);
			myWriter.write(txt);
			myWriter.close();
		}
		catch(Exception e) {
			
		}
	}
	
	public static String readTxtFile(String path) {
		try {
			File f = new File(path);
			BufferedReader b = new BufferedReader(new FileReader(f));
			return b.lines().collect(Collectors.joining());
		}
		catch(Exception e) {
			return e.toString();
		}
	}
}

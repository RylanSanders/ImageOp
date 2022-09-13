package imageOp;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import util.FileUtil;
import util.ThreadUtil;

import java.util.UUID;

public class CmdUtil {
public static final String TMP_DIR = "TmpDir/";
public static final String SCRIPTS_DIR = "ExternalScripts";
	public static String getPythonOutput(String filePath) {
		ProcessBuilder processBuilder = new ProcessBuilder("python", filePath);
		processBuilder.directory(new File(SCRIPTS_DIR));
		processBuilder.redirectErrorStream(true);

		try {
			Process process = processBuilder.start();
			return readInputStream(process.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getLongPythonOutput(String filePath, String longArg, String...args) {
		String[] formattedArgs = new String[args.length+4];
		formattedArgs[0]= "python";
		formattedArgs[1] = filePath;
		UUID uuid = UUID.randomUUID();
        String inputFileString = TMP_DIR + uuid.toString() + ".txt";
        UUID uuid2 = UUID.randomUUID();
        String outputFileString = TMP_DIR + uuid2.toString() + ".txt";
        FileUtil.writeTxtFile(inputFileString, longArg);
		formattedArgs[2] = inputFileString;
		formattedArgs[3] = outputFileString;
		for(int i=0;i<args.length;i++) {
			formattedArgs[i+4] = args[i];
		}
		ProcessBuilder processBuilder = new ProcessBuilder(formattedArgs);
		processBuilder.directory(new File(SCRIPTS_DIR));
		processBuilder.redirectErrorStream(true);

		try {
			Process process = processBuilder.start();
			int exitVal = process.waitFor();
			if(exitVal==0)
				return FileUtil.readTxtFile(outputFileString);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static String getPythonOutput(String filePath, String...args) {
		String[] formattedArgs = new String[args.length+2];
		formattedArgs[0]= "python";
		formattedArgs[1] = filePath;
		for(int i=0;i<args.length;i++) {
			formattedArgs[i+2] = args[i];
		}
		ProcessBuilder processBuilder = new ProcessBuilder(formattedArgs);
		processBuilder.directory(new File(SCRIPTS_DIR));
		processBuilder.redirectErrorStream(true);

		try {
			Process process = processBuilder.start();
			return readInputStream(process.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static BufferedImage getPythonImageOutput(BufferedImage imgInput, Script script,  String ...args) {
		
		UUID uuid = UUID.randomUUID();
        String inputFileString = TMP_DIR + uuid.toString() + ".png";
        UUID uuid2 = UUID.randomUUID();
        String outputFileString = TMP_DIR + uuid2.toString() + ".png";
        try {
        ImageIO.write(imgInput, "PNG", new File(inputFileString));
        String[] formattedArgs = formatArgs(script, new File(inputFileString).getAbsolutePath(), new File(outputFileString).getAbsolutePath(),args);
		ProcessBuilder processBuilder = new ProcessBuilder(formattedArgs);
		processBuilder.directory(new File(SCRIPTS_DIR));
		processBuilder.redirectErrorStream(true);

		
		Process process = processBuilder.start();
		int exitVal = process.waitFor();
		if(exitVal==0)
			return ImageIO.read(new File(outputFileString));
		else
			System.out.println("Failure in getPythonImageOutput with Script: " + script + " and Args: "+ Arrays.toString(args));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return "";
		return null;
	}
	
	private static String[] formatArgs(Script script, String inputPath, String outputPath, String...args) {
		String[] toRet = new String[args.length+4];
		toRet[0]="python";
		toRet[1] = addPy(script);
		toRet[2]=inputPath;
		toRet[3]=outputPath;
		for(int i=4;i<args.length+4;i++) {
			toRet[i]=args[i-4];
		}
		return toRet;
	}
	
	private static String addPy(Script s) {
		return s.name()+".py";
	}
	

	private static String readInputStream(InputStream s) {

		InputStreamReader isr = new InputStreamReader(s, StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		return br.lines().collect(Collectors.joining());
	}

}

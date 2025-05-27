package gameengine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import gameengine.Main;

public final class FileUtils {

	public static String readTextFile(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			StringBuilder sourceBuilder = new StringBuilder();
			String line;
			
			while( (line = reader.readLine()) != null ) {
				sourceBuilder.append(line);
				sourceBuilder.append('\n');
			}
			
			reader.close();
			
			return sourceBuilder.toString();
			
		} catch( Exception e ) {
			System.out.println(
				"[FileUtils.readTextFile(String)]\n" + 
				"ERROR: Unable to read file: \n" +
				path
			);
		}
		
		return "";
	}
	
	public static String getResourcePath(String relativePath) {
		URL url = Main.class.getResource("/" + relativePath);
		
		if( url == null ) {
			System.out.println(
				"[FileUtils.getResourcePath(String)]\n"+
				"ERROR: Unable to resolve relative resource path: \n"+
				relativePath
			);
			return null;
		}
		
		File file = new File(url.getPath());
		return file.getAbsolutePath();
	}
}

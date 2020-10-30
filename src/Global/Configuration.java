package Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Configuration {
	
	
	public static FileInputStream charge(String fic){
		FileInputStream in = null;
   		File fichier = null;
   		
		try {
			fichier = new File(fic);
			in = new  FileInputStream(fic);
		} catch (FileNotFoundException e) {
			System.err.println("Le fichier "+fic+" non trouve");
		}
		return in;
	}
}

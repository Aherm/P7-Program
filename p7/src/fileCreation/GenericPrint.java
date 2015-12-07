package fileCreation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class GenericPrint {

	public static void PRINTER(String filepath,String toPrint){
		PrintWriter writer;
		
		try {
			writer = new PrintWriter(filepath);
			writer.write(toPrint);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
}

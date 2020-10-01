import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.*; 

public class A1{
	public static void main(String[] args) {

		String filename;
		CDScanner inputScanner;

		try {
			filename = args[0]; 
			String textFile = new String(Files.readAllBytes(Paths.get(filename)));
			inputScanner = new CDScanner(textFile);
	
		}catch(Exception e){
			System.out.println("FILE NOT FOUND");
            System.out.println(e.toString());
            return;
		}

		do{
			do{
				try{
					Token currentToken = inputScanner.scan();
					inputScanner.printToken(currentToken);
				}catch(Exception e){
					
				}
	
			}while (!inputScanner.eof());
			//sometimes when the end of the file has been reached the buffer will still contain a possible token,
			//further scan(s) is required
		}while(!inputScanner.isBufferEmpty());

		inputScanner.printToken(inputScanner.EOFToken());
	}
}
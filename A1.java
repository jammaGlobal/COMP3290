import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class A1{
	public static void main(String[] args) {

		ArrayList<Token> tokenList = new ArrayList<Token>();
		CD20Scanner inputScanner;
		String filename;

		try {
			filename = args[0];
			inputScanner = new CD20Scanner(filename);
			System.out.println(inputScanner);
	
		}catch(Exception e){
			System.out.println("FILE NOT FOUND");
            System.out.println(e.toString());
            return;
		}

		while (!inputScanner.eof()){
			Token currentToken = inputScanner.scan();
			inputScanner.printToken(Token);
			tokenList.add(currentToken);
		}
		

		
	}
}
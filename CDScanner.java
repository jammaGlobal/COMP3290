
import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public enum STATE{
    SCAN, IDENT, DELIM_OPERATOR, S_COMMENT, M_COMMENT, KEYWORD, INTEGER, FLOAT, STRINGCONST
}

public class CDScanner{
    private char[] buffer;

    public CDScanner(String input){
        System.out.println(input);
    }


    
}


public Token scan(){

    
}

public boolean isBufferEmpty(){

}



public boolean eof(){

}

public void printToken(Token cToken){

}

//public Token commentState(){

//}
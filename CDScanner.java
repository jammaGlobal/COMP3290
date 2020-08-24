
import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class CDScanner{

    private enum STATE{
        START,
        KEYWORD, IDENT, DELIM_OPERATOR, 
        SL_COMMENT, ML_COMMENT, 
        INTEGER, FLOAT, 
        STRINGCONST, 
        ERROR, 
        END
    }

    private String buffer;

    public CDScanner(String input){ 
        System.out.println(input);
    }

    public Token scan(){
        Token newTok = new Token();
        return newTok;
    }
    
    public boolean isBufferEmpty(){
        return true;
    }
    
    public boolean eof(){
        return true;
    }
    
    public void printToken(Token cToken){
    
    }
    
}




//public Token commentState(){

//}
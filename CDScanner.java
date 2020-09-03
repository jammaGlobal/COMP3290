
import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CDScanner{

    private static enum STATE{
        START,
        KEYWORD, IDENT, DELIM_OPERATOR, 
        COMMENT, SL_COMMENT, ML_COMMENT, 
        INTEGER, FLOAT, 
        STRINGCONST, 
        ERROR, 
        END
    }

    private String text;
    private String buffer;
    private int currChar;

    private int linNo;
    private int colNo;

    private STATE currState;

    private int noOfChars;

    public CDScanner(String text){ 
        this.text = text;
        noOfChars = text.length();
        buffer = "";
        currChar = 0;
        currState = STATE.START;
        Token.popReservedList();

        linNo = 1;
        colNo = 1;
    }

    public void scan(){
        //Check if whitespace ASCII

        System.out.println("# Chars: "+noOfChars);
        
        while(isNonChar(text.charAt(currChar))){
            if(eof()){
                break;
            }
            currChar++;
            System.out.println("currChar: "+currChar);
            
        }
        
        
        //currChar++;
        //Check if tab ASCII
        //Check if new line char ASCII (important for linNo)
        //Check if carriage return char ASCII
        //Check if end of file ASCII 


        /*
        switch(currState){
           // Token tok = new Token();

            case START:

                break;
            case KEYWORD:
                if(Character.isLetterOrDigit(text.charAt(currChar))){
                    buffer.concat(String.valueOf(text.charAt(currChar)));
                }
                else if(text.charAt(currChar) == '_'){
                    buffer.concat(String.valueOf(text.charAt(currChar)));
                    currState = STATE.IDENT;
                }
                else{
                    Token.
                    return new Token(Token., colNo, linNo, buffer); //keyword token
                }
                break;
            case IDENT:
                if(Character.isLetterOrDigit(text.charAt(currChar)) || text.charAt(currChar) == '_'){
                    buffer.concat(String.valueOf(text.charAt(currChar)));
                }
                else{
                    return new Token(Token.TIDEN, colNo, linNo, buffer); //identifier token
                }
                break;
            case DELIM_OPERATOR:
                break;
            case SL_COMMENT:
                break;
            case ML_COMMENT:
                break;
            case INTEGER:
                break;
            case FLOAT:
                break;
            case STRINGCONST:
                break;
            case ERROR:
                break;
            default:
            //end state
                
        }
        stateTransition(text.charAt(currChar));
        */
        
    }

    public boolean isNonChar(char c){
        int decimal = (int) c;

        //Carriage return, precursor to new line for Windows
        if(decimal == 13){
            System.out.println("CR");
            return true;
        }
        //Newline, new line for Windows and Mac
        else if(decimal == 10){
            System.out.println("NL");
            return true;
        }
        else if(decimal == 9){
            System.out.println("TAB");
            return true;
        }
        else if(Character.isWhitespace(c)){
            System.out.println("WS");
            return true;
        }
        else{
            return false;
        }

    }
    /*
    public void stateTransition(char c){
        if(Character.isAlphabetic(c)){
            currState = STATE.KEYWORD;
        }
        else if(Character.isDigit(c)){
            currState = STATE.INTEGER;
        }
        else if(currChar == '"'){
            currState = STATE.STRINGCONST;
        }
        else if(currChar == '/'){
            currState = STATE.COMMENT;
        }
        else if(isDelimOperator()){
            currState = STATE.DELIM_OPERATOR;
        }
        else{
            c
        }
    }*/

    public boolean isBufferEmpty(){
        if(buffer.isEmpty()){
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean isDelimOperator(){
        return true;
    }
    
    public boolean eof(){

        if(currChar == noOfChars-1){
            return true;
        }
        else
            return false;
    }
    
    public void printToken(Token cToken){
        //cum
    }
    
}




//public Token commentState(){

//}
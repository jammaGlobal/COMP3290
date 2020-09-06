
import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CDScanner{

    private enum STATE{
        START, WHITESPACE,
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
        System.out.println("# Chars: "+noOfChars);

        linNo = 1;
        colNo = 1;
    }

    public Token scan(){
        //Checks if whitespace ASCII
        Token tokenFound = null;

        System.out.println("currChar: "+currChar+"|"+text.charAt(currChar));
        
        int i = 0;
        while(tokenFound == null){ 

            System.out.println("Buffer: "+buffer);
            
            switch(currState){

                case START:
                stateTransition(text.charAt(currChar));
                    break;
                case WHITESPACE:
                stateTransition(text.charAt(currChar));
                    break;
                case KEYWORD:
                    if(Character.isLetterOrDigit(text.charAt(currChar))){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                        
                    }
                    else if(text.charAt(currChar) == '_'){
                        currState = STATE.IDENT;
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;    
                        currChar++;
                    }
                    else{
                        //no currChar++, holds currChar in attention for next scan()
                        if(Token.checkReserved(buffer) == -1){
                            tokenFound = new Token(Token.TIDEN, colNo, linNo, buffer);
                            buffer = "";
                        }
                        else{
                            tokenFound = new Token(Token.checkReserved(buffer), colNo, linNo, buffer); //keyword token
                            buffer = "";
                            System.out.println("help: ");
                        }
                    }
                    break;
                case IDENT:
                    if(Character.isLetterOrDigit(text.charAt(currChar)) || text.charAt(currChar) == '_'){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    else{
                        tokenFound = new Token(Token.TIDEN, colNo, linNo, buffer); //identifier token
                        buffer = "";
                    }
                    break;
                case DELIM_OPERATOR:
                    //Check buffer for any
                    if(buffer.isEmpty()){
                        tokenFound = new Token(Token.checkReserved(String.valueOf(text.charAt(currChar))), colNo, linNo, String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    break;
                case COMMENT:

                    break;
                case SL_COMMENT:
                    break;
                case ML_COMMENT:
                    break;
                case INTEGER:
                    if(Character.isDigit(text.charAt(currChar))){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    else if(text.charAt(currChar) == '.'){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                        currState = STATE.FLOAT;
                    }
                    else{
                        tokenFound = new Token(Token.TINTG, colNo, linNo, buffer);
                        buffer = "";
                    }
                    break;
                case FLOAT:
                    //call
                    if(buffer.endsWith(".")){
                        if(Character.isDigit(text.charAt(currChar))){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                        }
                        else{   //char after dot (.) can be any non-int char, not just operators
                            tokenFound = new Token(Token.TILIT, colNo, linNo, buffer.substring(0, buffer.length()-1));
                            //buffer = ".";
                            //currState = STATE.DELIM_OPERATOR;
                            stateTransition(text.charAt(currChar));
                        }
                    }
                    else{
                        if(Character.isDigit(text.charAt(currChar))){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                        }
                        else{
                            tokenFound = new Token(Token.TFLIT, colNo, linNo, buffer);
                            buffer = "";
                        }
                    }

                    
                    break;
                case STRINGCONST:
                    break;
                default:
                    System.out.println("HELP2");
                    
            }

            if(eof()){

                if(Token.checkReserved(buffer) == -1){
                    tokenFound = new Token(Token.TIDEN, colNo, linNo, buffer);
                    buffer = "";
                }
                else{
                    tokenFound = new Token(Token.checkReserved(buffer), colNo, linNo, buffer); //keyword token
                    buffer = "";
                    System.out.println("help: ");
                }
                break;
            }
            
                    
        }   //while(token not found)
        System.out.println("TokenFound: "+tokenFound.getLexeme()+", "+tokenFound.getTokenNo());
        
        
        if(!eof()){
            stateTransition(text.charAt(currChar));
        }

        if(tokenFound != null)
            return tokenFound;
        else
            return new Token(Token.TEQEQ, colNo, linNo, buffer);
    }
    
    public void stateTransition(char c){
        System.out.println(c+ "<-char");

        if(!buffer.isEmpty()){
            
        }

        //Capturing whitespace and end of lines
        int decimal = (int) c;

        if(decimal == 13){
            System.out.println("CR");
            currChar++;
            currState = STATE.WHITESPACE;
        }

        //Newline, new line for Windows and Mac
        else if(decimal == 10){
            System.out.println("NL");
            linNo++;
            currChar++;
            currState = STATE.WHITESPACE;
        }
        else if(decimal == 9){
            System.out.println("TAB");
            currChar++;
            currState = STATE.WHITESPACE;
        }
        else if(Character.isWhitespace(c)){
            System.out.println("WS");
            colNo++;
            currChar++;
            currState = STATE.WHITESPACE;
        }

        //Identifying beginnings of a Token

        else if(Character.isAlphabetic(c)){
            currState = STATE.KEYWORD;
            System.out.println("a");
        }
        else if(Character.isDigit(c)){
            currState = STATE.INTEGER;
            System.out.println("b");
        }
        else if(currChar == '"'){
            currState = STATE.STRINGCONST;
            System.out.println("c");
        }
        else if(currChar == '/'){
            currState = STATE.COMMENT;
            System.out.println("d");
        }
        else if(isDelimOperator(c)){
            currState = STATE.DELIM_OPERATOR;
            System.out.println("e");
        }
        else{
            System.out.println("f");
        }
    }

    public boolean isBufferEmpty(){
        if(buffer.isEmpty()){
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean isDelimOperator(char c){
        if(Token.checkReserved(String.valueOf(c)) == -1){
            return false;
        }
        else{
            System.out.println("yo");
            return true;
            
        }
        
    }
    
    public boolean eof(){
        //System.out.println("currChar: "+currChar);
        if(currChar == noOfChars){
            return true;
        }
        else
            return false;
    }
    
    public void printToken(Token cToken){
        //cum
    }
    
}

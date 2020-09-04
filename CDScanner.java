
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
        while(tokenFound == null && i != 10){ 
            i++;

            System.out.println("Buffer: "+buffer);


            if(eof()){
                break;
            }

            
            switch(currState){

                case START:
                stateTransition(text.charAt(currChar));
                    break;
                case WHITESPACE:

                    break;
                case KEYWORD:
                    if(Character.isLetterOrDigit(text.charAt(currChar))){
                        buffer += (String.valueOf(text.charAt(currChar)));
                       // buffer.concat(String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    else if(text.charAt(currChar) == '_'){
                        buffer.concat(String.valueOf(text.charAt(currChar)));
                        currState = STATE.IDENT;
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
                System.out.println("CUM3: ");
                    if(Character.isLetterOrDigit(text.charAt(currChar)) || text.charAt(currChar) == '_'){
                        buffer.concat(String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    else{
                        tokenFound = new Token(Token.TIDEN, colNo, linNo, buffer); //identifier token
                        buffer = "";
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
                default:
                    System.out.println("HELP2");
                    
            }
            //tokenFound = new Token(Token.TGEQL, colNo, linNo, buffer);
                    
        }   //while(token not found)
        stateTransition(text.charAt(currChar));

        if(tokenFound != null)
            return tokenFound;
        else
            return new Token(Token.TEQEQ, colNo, linNo, buffer);
    }
/*
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
            linNo++;
            return true;
        }
        else if(decimal == 9){
            System.out.println("TAB");
            return true;
        }
        else if(Character.isWhitespace(c)){
            System.out.println("WS");
            colNo++;
            return true;
        }
        else{
            return false;
        }

    }
    */
    
    public void stateTransition(char c){
        System.out.println(c+ "<-char");

        //Capturing whitespace and end of lines
        int decimal = (int) c;

        if(decimal == 13){
            System.out.println("CR");
            currChar++;
        }
        //Newline, new line for Windows and Mac
        else if(decimal == 10){
            System.out.println("NL");
            linNo++;
            currChar++;
        }
        else if(decimal == 9){
            System.out.println("TAB");
            currChar++;
        }
        else if(Character.isWhitespace(c)){
            System.out.println("WS");
            colNo++;
            currChar++;
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
        else if(isDelimOperator()){
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

    public boolean isDelimOperator(){
        return true;
    }
    
    public boolean eof(){
        //System.out.println("currChar: "+currChar);
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

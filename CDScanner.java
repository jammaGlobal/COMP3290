
import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//TO-DO:
//-Fix column numbers to be the start of lexeme
//-Create a end-of-file function to check the nature of the last possible token


public class CDScanner{

    private enum STATE{
        START, WHITESPACE,
        KEYWORD, IDENT, DELIM_OPERATOR, 
        COMMENT, SL_COMMENT, ML_COMMENT, 
        INTEGER, FLOAT, 
        STRINGCONST, 
        ERROR
    }

    private String text;
    private String buffer;
    private int currChar;

    private int linNo;
    private int colNo;

    private STATE currState;

    private int noOfChars;

    private int outChar;
    private String outBuffer;

    public CDScanner(String text){ 
        this.text = text;
        noOfChars = text.length();
        buffer = "";
        currChar = 0;
        currState = STATE.START;
        Token.popReservedList();

        linNo = 1;
        colNo = 1;

        outChar = 0;
        outBuffer = "";
    }

    public Token scan() throws Exception{
        //Checks if whitespace ASCII
        Token tokenFound = null;

        while(tokenFound == null){ 
            // fart
            if(!buffer.isEmpty()){
                if(buffer.equals(".")){
                    tokenFound = new Token(Token.TDOTT, linNo, colNo-1, buffer);
                    buffer = "";
                    break;
                }
                else if(buffer.equals("-")){
                    tokenFound = new Token(Token.TMINS, linNo, colNo-1, null);
                    buffer = "";
                    break;
                }
                //do a buffer excess consumption for "/-" and "/*"
            }
            
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
                            tokenFound = new Token(Token.TIDEN, linNo, colNo-buffer.length(), buffer);
                            buffer = "";
                        }
                        else{
                            tokenFound = new Token(Token.checkReserved(buffer), linNo, colNo-buffer.length(), buffer); //keyword token
                            buffer = "";
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
                        tokenFound = new Token(Token.TIDEN, linNo, colNo-buffer.length(), buffer); //identifier token
                        buffer = "";
                    }
                    break;
                case DELIM_OPERATOR:
                    //Unique one-time intial event for DELIM_OPERATOR STATE
                    if(buffer.isEmpty()){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                        
                    }
                    else{
                        
                        String check = buffer + String.valueOf(text.charAt(currChar));

                        //  The case where the end of a double char operator is identified
                        if(Token.checkAssignment(check) != -1){
                            tokenFound = new Token(Token.checkAssignment(check), linNo, colNo-1, null);
                            buffer = "";
                            colNo++;
                            currChar++;
                        }
                        // The case where there is a second single char operator found, return current buffer with single char operator and add
                        // current Char to buffer
                        else if(Token.checkOperators(String.valueOf(text.charAt(currChar))) != -1 && Token.checkAssignment(check) == -1){
                            tokenFound = new Token((Token.checkOperators(buffer)), linNo, colNo-1, null);
                            buffer = String.valueOf(text.charAt(currChar));
                            colNo++;
                            currChar++;
                        }
                        //The case where there is no operator found at current char, return buffer as single char operator token
                        else{
                            tokenFound = new Token((Token.checkOperators(buffer)), linNo, colNo-1, null);
                            buffer = "";
                        }
                    }

                    break;

                case COMMENT:
                    if(buffer.isEmpty()){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    else if(buffer.length() == 1){
                        if(text.charAt(currChar) == '-'){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                            currState = STATE.SL_COMMENT;

                        }
                        else if(text.charAt(currChar) == '*'){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                            currState = STATE.ML_COMMENT;
                        }
                        else{
                            tokenFound = new Token(Token.TDIVD, linNo, colNo-1, null);
                            buffer = "";
                        }
                    }

                    break;
                case SL_COMMENT:
                    if(buffer.length() == 2){
                        //Single comment state
                        if(text.charAt(currChar) == '-'){
                            buffer = "";
                            colNo++;
                            currChar++;
                        }
                        else{
                            tokenFound = new Token(Token.TDIVD, linNo, colNo-2, null);
                            buffer = "-";
                        }
                    }
                    else{
                        if((int) text.charAt(currChar) == 13){
                            currChar++;
                            currState = STATE.WHITESPACE;
                        }
                        else{
                            colNo++;
                            currChar++;
                        }

                    }
                    
                    
                    break;
                case ML_COMMENT:
                    if(buffer.equals("/*")){
                        //Multiline comment state
                        if(text.charAt(currChar) == '*'){
                            buffer = "";
                            colNo++;
                            currChar++;
                        }
                        else{
                            tokenFound = new Token(Token.TDIVD, linNo, colNo-2, null);
                            buffer = "*";
                        }
                    }
                    else if(buffer.isEmpty()){
                        if(text.charAt(currChar) == '*'){
                            buffer = "*";
                            colNo++;
                            currChar++;
                        }
                        else{
                            colNo++;
                            currChar++;
                        }
                    }
                    else if(buffer.equals("*")){
                        if(text.charAt(currChar) == '*'){
                            buffer = "**";
                            colNo++;
                            currChar++;
                        }
                        else{
                            buffer = "";
                            colNo++;
                            currChar++;
                        }
                    }
                    else if(buffer.equals("**")){
                        if(text.charAt(currChar) == '/'){
                            buffer = "";
                            colNo++;
                            currChar++;
                            if(eof()){

                            }
                            else{
                                stateTransition(text.charAt(currChar));
                            }
                        }
                        else{
                            buffer = "";
                            colNo++;
                            currChar++;
                        }
                    }
                    break;
                //
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
                        tokenFound = new Token(Token.TINTG, linNo, colNo-buffer.length(), buffer);
                        buffer = "";
                    }
                    break;
                case FLOAT:
                    //instance just after the transition from Integer to Float state
                    if(buffer.endsWith(".")){
                        if(Character.isDigit(text.charAt(currChar))){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                        }
                        else{   //char after dot (.) can be any non-int char, not just operators
                            tokenFound = new Token(Token.TILIT, linNo, colNo-buffer.length(), buffer.substring(0, buffer.length()-1));
                            buffer = ".";
                            //currState = STATE.DELIM_OPERATOR;
                            //stateTransition(text.charAt(currChar));
                        }
                    }
                    else{
                        if(Character.isDigit(text.charAt(currChar))){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                        }
                        else{
                            tokenFound = new Token(Token.TFLIT, linNo, colNo-buffer.length(), buffer);
                            buffer = "";
                        }
                    }

                    
                    break;
                case STRINGCONST:
                    //
                    if((int) text.charAt(currChar) == 34){
                        tokenFound = new Token(Token.TSTRG, linNo, colNo-buffer.length(), buffer);
                        buffer = "";
                        colNo++;
                        currChar++;
                    }
                    else if((int) text.charAt(currChar) == 13){
                        currChar++;
                    }
                    else if((int) text.charAt(currChar) == 10){
                        linNo++;
                        currChar++;
                        tokenFound = new Token(Token.TUNDF, linNo, colNo-buffer.length(), buffer);
                        colNo = 0;
                        buffer = "";
                    }
                    else{
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    break;
                case ERROR:
                    if(errorStateRemain((text.charAt(currChar)))){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    else{
                        tokenFound = new Token(Token.TUNDF, linNo, colNo-buffer.length(), buffer);
                        buffer = "";
                    }

                    break;
                default:
                    
            }

           // System.out.print("buffer:"+buffer+"|");

            if(eof() && tokenFound == null){
                if(Token.checkReserved(buffer) == -1){
                    if(currState == STATE.ML_COMMENT){
                        
                    }
                    else if(currState == STATE.FLOAT){
                        tokenFound = new Token(Token.TFLIT, colNo-buffer.length(), linNo, buffer);
                        buffer = "";
                    }
                    else if(currState == STATE.INTEGER){
                        tokenFound = new Token(Token.TILIT, colNo-buffer.length(), linNo, buffer);
                        buffer = "";
                    }
                    else if(currState == STATE.STRINGCONST){
                        tokenFound = new Token(Token.TUNDF, colNo-buffer.length(), linNo, buffer);
                        buffer = "";
                    }
                    else{
                        tokenFound = new Token(Token.TIDEN, colNo-buffer.length(), linNo, buffer);
                        buffer = "";
                    }
                }
                else{
                    tokenFound = new Token(Token.checkReserved(buffer), colNo, linNo, buffer); //keyword token
                    buffer = "";
                }
                break;
            }
            
                    
        }   //while(token not found)
        
        if(!eof()){
            if(!isBufferEmpty()){

            }
            else{
                stateTransition(text.charAt(currChar));
            }
                
        }

        if(tokenFound != null)
            return tokenFound;
        else
            throw new Exception("Exception message");
    }
    
    public void stateTransition(char c){

        //Capturing whitespace and end of lines
        int decimal = (int) c;
        
        if(decimal == 13){
            currChar++;
            currState = STATE.WHITESPACE;
        }

        //Newline, new line for Windows and Mac
        else if(decimal == 10){
            linNo++;
            colNo = 1  ;
            currChar++;
            currState = STATE.WHITESPACE;
        }
        else if(decimal == 9){
            currChar++;
            currState = STATE.WHITESPACE;
        }
        else if(Character.isWhitespace(c)){
            colNo++;
            currChar++;
            currState = STATE.WHITESPACE;
        }

        //Identifying beginnings of a Token

        else if(Character.isAlphabetic(c)){
            currState = STATE.KEYWORD;
        }
        else if(Character.isDigit(c)){
            currState = STATE.INTEGER;
        }
        else if(c == '/'){
            currState = STATE.COMMENT;
        }
        else if(isDelimOperator(c)){
            currState = STATE.DELIM_OPERATOR;
        }
        else if(decimal == 34){
            currState = STATE.STRINGCONST;
            currChar++;
            colNo++;
        }
        else{
            currState = STATE.ERROR;
        }

    }

    public boolean errorStateRemain(char c){

        //Capturing whitespace and end of lines
        int decimal = (int) c;

        if(decimal == 13){
            return false;
        }

        //Newline, new line for Windows and Mac
        else if(decimal == 10){
            return false;
        }
        else if(decimal == 9){
            return false;
        }
        else if(Character.isWhitespace(c)){
            return false;
        }

        //Identifying beginnings of a Token

        else if(Character.isAlphabetic(c)){
            return false;
        }
        else if(Character.isDigit(c)){
            return false;
        }
        else if(c == '/'){
            return false;
        }
        else if(isDelimOperator(c)){
            return false;
        }
        else if(decimal == 34){
            return false;
        }
        else{
            return true;
        }
    }

    public Token EOFToken(){
        return new Token(Token.T_EOF, linNo, colNo, null);
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
            return true;
            
        }
        
    }
    
    public boolean eof(){
        if(currChar == noOfChars){
            return true;
        }
        else
            return false;
    }

    public boolean eof(int c){
        if(c == noOfChars-1){
            return true;
        }
        else
            return false;
    }
    
    public void printToken(Token cToken){

        if(outChar >= 66){
            System.out.print("\n");
            outChar = 0;
        }

        if(cToken.getTokenNo() == 58 || cToken.getTokenNo() == 59 || 
        cToken.getTokenNo() == 60 || cToken.getTokenNo() == 61){
            outBuffer += Token.TPRINT[cToken.getTokenNo()];
            outBuffer += "";
            outBuffer += cToken.getLexeme();
            outBuffer += " ";

            outChar += outBuffer.length();
            System.out.print(outBuffer);
        }
        else if(cToken.getTokenNo() == 62){
            outBuffer += Token.TPRINT[cToken.getTokenNo()];
            outBuffer += "lexical error ";
            outBuffer += cToken.getLexeme();
            outBuffer += " ";

            outChar += outBuffer.length();
            System.out.print(outBuffer);
        }
        else{
            outBuffer += Token.TPRINT[cToken.getTokenNo()];

            outChar += outBuffer.length();
            System.out.print(outBuffer);
        }


        outBuffer = "";
    
        

    }
    
}

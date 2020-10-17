/*


*/
import java.util.ArrayList; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CDScanner{

    //Enum values that dictate each possible state within the theoretical state machine the program attempts to simulate
    private enum STATE{
        START, WHITESPACE,
        KEYWORD, IDENT, DELIM_OPERATOR, 
        COMMENT, SL_COMMENT, ML_COMMENT, 
        INTEGER, FLOAT, 
        STRINGCONST, 
        ERROR
    }

    //Stores text file as a single String
    private String text;
    //Used as a buffer to evaluate before returning a Token
    private String buffer;
    //Used as a pointer counter for which char the Scanner is looking at currently
    private int currChar;

    //Keeps track of what line the Scanner is scanning
    private int linNo;
    //Keeps track of what column/position the Scanner is scanning
    private int colNo;
    //Denotes the current state machine State
    private STATE currState;
    //The total number of chars, used to check when the end of file has been reached
    private int noOfChars; 

    //Output variables
    private int outChar;
    private String outBuffer;

    public CDScanner(String text){ 
        this.text = text;
        noOfChars = text.length();
        buffer = "";
        currChar = 0;
        currState = STATE.START;

        //Populates list of reserved keywords, delimiters and operators
        Token.popReservedList();

        linNo = 1;
        colNo = 1;

        outChar = 0;
        outBuffer = "";
    }


    // The main workings of the Scanner, uses a switch statment to act as a state machine with different actions for each state.
    // Returns a single token tuple (id, line number, column number, lexeme) each call 
    public Token scan() throws Exception{
        
        Token tokenFound = null;

        //loops until a Token has been constructed
        while(tokenFound == null){ 

            //Checks specific buffer excess from consuming integers with a dot operator succeeding
            //and checks the buffer for a minus operator in the case of a possible single line comment failing
            if(!buffer.isEmpty()){
                if(buffer.equals(".")){
                    tokenFound = new Token(Token.TDOTT, linNo, colNo-1, buffer);
                    buffer = "";
                    break;
                }
                else if(buffer.equals("-") && currState == STATE.SL_COMMENT){
                    tokenFound = new Token(Token.TMINS, linNo, colNo-1, null);
                    buffer = "";
                    break;
                }
            }
            
            //Switch statement containing most states in the state machine, some states are only accessible through other states
            switch(currState){

                //Initial state will immediately state transition dependending on the first char of text file
                case START:
                stateTransition(text.charAt(currChar));
                    break;
                //Forwards to state transition while has the whitespace consuming capabilites
                case WHITESPACE:
                stateTransition(text.charAt(currChar));
                    break;
                //When the start of a new possible token is an alphabetic char it is added to the buffer in the state
                //transition and the next char is evaluated in the Keyword state
                case KEYWORD:
                    if(Character.isLetterOrDigit(text.charAt(currChar))){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                        
                    }
                    //If an underscore is consumed then a state transition to the Identifier state occurs
                    else if(text.charAt(currChar) == '_'){
                        currState = STATE.IDENT;
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;    
                        currChar++;
                    }
                    else{
                        //Evaluates if current buffer matches any of the keywords in the Token class
                        //if it is then a keyword token is returned, if not an identifier token is returned
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
                //Transitioned to whenever an underscore char is found while in the keyword state
                //Whenever an alphabetic, numeric or underscore char is found it will be added to the buffer, else
                //the current buffer will be returned as an Identifier token
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
                //Transitioned to whenever the start of a new token is a single-char delimiter/operator
                //or whenever there is a failed single-line/multi-line comment beginning 
                case DELIM_OPERATOR:
                    //if the buffer is empty we will add the operator char and suspend an evaluation for the second char
                    //as the operator could possibly be one of the assignment operators
                    if(buffer.isEmpty()){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                        
                    }
                    //since the buffer is not empty we will evaluate the buffer and current char as a double char string
                    //and check if it is an double char assignment operator
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
                //When a slash is found in the state transition it will transition to the Comment state and add it to the
                //buffer within the state
                case COMMENT:
                    if(buffer.isEmpty()){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    else if(buffer.length() == 1){
                        //if a minus operator succeeds the slash, the state is changed to the Single Line Comment state
                        if(text.charAt(currChar) == '-'){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                            currState = STATE.SL_COMMENT;

                        }
                        //if a star operator succeeds the slash, the state is changed to the Multi Line Comment state
                        else if(text.charAt(currChar) == '*'){
                            buffer += (String.valueOf(text.charAt(currChar)));
                            colNo++;
                            currChar++;
                            currState = STATE.ML_COMMENT;
                        }
                        //else the slash operator is returned as an operator token
                        else{
                            tokenFound = new Token(Token.TDIVD, linNo, colNo-1, null);
                            buffer = "";
                        }
                    }

                    break;
                // Single Line Comment state
                case SL_COMMENT:
                    if(buffer.length() == 2){
                        //Second minus operator succeeds first, stay in Single Line Comment state for subsequent char(s)
                        if(text.charAt(currChar) == '-'){
                            buffer = "";
                            colNo++;
                            currChar++;
                        }
                        //If a different char succeeds, then slash token is returned and the minus operator substitutes it
                        //in the buffer
                        else{
                            tokenFound = new Token(Token.TDIVD, linNo, colNo-2, null);
                            buffer = "-";
                        }
                    }
                    //When buffer is not length 2 the Single Line Comment state is engaged and chars will be consumed without
                    //adding to the buffer until a new line char is found, in that case the state transitions to whitespace where
                    //new line characters are handled
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
                
                // Multiline Comment State
                case ML_COMMENT:
                    if(buffer.equals("/*")){
                        //Second star operator succeeds first, stay in Multi Line Comment state for subsequent char(s)
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
                    //Next if else statements look for possible char sequence to end multi-line comment
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
                            // Condition where End of file cuts off the final char of the comment-end char sequence,
                            // any other time the 
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
                // When start of token is numerical, machine transitions to Integer state
                case INTEGER:
                    if(Character.isDigit(text.charAt(currChar))){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                    }
                    //Possible float/real value, transition 
                    else if(text.charAt(currChar) == '.'){
                        buffer += (String.valueOf(text.charAt(currChar)));
                        colNo++;
                        currChar++;
                        currState = STATE.FLOAT;
                    }
                    else{
                        tokenFound = new Token(Token.TILIT, linNo, colNo-buffer.length(), buffer);
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


            if(buffer != ""){
                if(eof() && tokenFound == null){
                    if(Token.checkReserved(buffer) == -1){
                        if(currState == STATE.ML_COMMENT){
                            buffer = "";
                            
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
                    else if(Token.checkReserved(buffer) != -1 && currState == STATE.ML_COMMENT){
                        buffer = "";
                    }
                    else{
                        tokenFound = new Token(Token.checkReserved(buffer), colNo, linNo, buffer); //keyword token
                        buffer = "";
                    }
                    break;
                }
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
    
    //checks if the end of file has been reached
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
    
    //Prints token in spec format
    public void printToken(Token cToken){

        if(outChar >= 66){
            System.out.print("\n");
            outChar = 0;
        }

        if(cToken.getTokenNo() == 58 || cToken.getTokenNo() == 59 || 
        cToken.getTokenNo() == 60 || cToken.getTokenNo() == 61){
            outBuffer += Token.TPRINT[cToken.getTokenNo()];
            outBuffer += "";
            if(cToken.getTokenNo() == 61){
                outBuffer += "\"";
                outBuffer += cToken.getLexeme();
                outBuffer += "\"";
            }
            else{
                outBuffer += cToken.getLexeme();
            }
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

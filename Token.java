import java.util.HashMap;
import java.util.Map;

import java.util.Iterator;

public class Token {

    public static final int

    T_EOF =  0,	  // Token value for end of file

    // The 30 keywords

    TCD20 =  1,	TCONS = 2,	TTYPS = 3,	TTTIS = 4,	TARRS = 5,	TMAIN = 6,
    TBEGN =  7,	TTEND = 8,	TARAY = 9,	TTTOF = 10,	TFUNC = 11,	TVOID = 12,
    TCNST = 13,	TINTG = 14,	TREAL = 15,	TBOOL = 16,	TTFOR = 17,	TREPT = 18,
    TUNTL = 19,	TIFTH = 20,	TELSE = 21,	TINPT = 22,	TPRIN = 23,	TPRLN = 24,
    TRETN = 25,	TNOTT = 26,	TTAND = 27,	TTTOR = 28,	TTXOR = 29,	TTRUE = 30,
    TFALS = 31,

    // the operators and delimiters
                TCOMA = 32,	TLBRK = 33,	TRBRK = 34,	TLPAR = 35,	TRPAR = 36,
    TEQUL = 37,	TPLUS = 38,	TMINS = 39,	TSTAR = 40,	TDIVD = 41,	TPERC = 42,
    TCART = 43,	TLESS = 44,	TGRTR = 45,	TCOLN = 46,	TLEQL = 47,	TGEQL = 48,
    TNEQL = 49,	TEQEQ = 50,	TPLEQ = 51,	TMNEQ = 52,	TSTEQ = 53,	TDVEQ = 54,
    TSEMI = 56,	TDOTT = 57,

    // the tokens which need tuple values

    TIDEN = 58,	TILIT = 59,	TFLIT = 60,	TSTRG = 61,	TUNDF = 62;

    public static final String TPRINT[] = {

        "TEOF  ",

        "TCD19 ",	"TCONS ",	"TTYPS ",	"TIS   ",	"TARRS ",	"TMAIN ",
        "TBEGN ",	"TEND  ",	"TARAY ",	"TOF   ",	"TFUNC ",	"TVOID ",
        "TCNST ",	"TINTG ",	"TREAL ",	"TBOOL ",	"TFOR  ",	"TREPT ",
        "TUNTL ",	"TIFTH ",	"TELSE ",	"TINPT ",	"TPRIN ",	"TPRLN ",
        "TRETN ",	"TNOT  ",	"TAND  ",	"TOR   ",	"TXOR  ",	"TTRUE ",
        "TFALS ",	
        
                    "TCOMA ",	"TLBRK ",	"TRBRK ",	"TLPAR ",	"TRPAR ",
        "TEQUL ",	"TPLUS ",	"TMINS ",	"TSTAR ",	"TDIVD ",	"TPERC ",
        "TCART ",	"TLESS ",	"TGRTR ",	"TCOLN ",	"TLEQL ",	"TGEQL ",
        "TNEQL ",	"TEQEQ ",	"TPLEQ ",	"TMNEQ ",	"TSTEQ ",	"TDVEQ ",
        "TPCEQ ",	"TSEMI ",	"TDOT  ",

        "TIDEN ",	"TILIT ",	"TFLIT ",	"TSTRG ",	"TUNDF "};

    private int tokNo, ln, col;
    private String lex;
    private static HashMap<String, Integer> reservedTokenVal = new HashMap<String, Integer>();
    private static HashMap<String, Integer> assignmentOperators = new HashMap<String, Integer>();
    private static HashMap<String, Integer> singleOperators = new HashMap<String, Integer>();

    public Token(){
        tokNo = 0;
        ln = 0;
        col = 0;
        lex = "";
    }

    public Token(int tokNo, int ln, int col, String lex){
        this.tokNo = tokNo;
        this.ln = ln;
        this.col = col;
        this.lex = lex;
    }

    public int getTokenNo(){
        return tokNo;
    }

    public int getLine(){
        return ln;
    }

    public int getColumn(){
        return col;
    }

    public String getLexeme(){
        return lex;
    }

    public static void popReservedList(){
        if(reservedTokenVal.isEmpty()){

            //adds keywords into list of reserved characters/character sequences
            reservedTokenVal.put("cd20",     TCD20);
            reservedTokenVal.put("constants",TCNST);
            reservedTokenVal.put("types",    TTYPS);
            reservedTokenVal.put("is",       TTTIS);
            reservedTokenVal.put("arrays",   TARRS);
            reservedTokenVal.put("main",     TMAIN);
            reservedTokenVal.put("begin",    TBEGN);
            reservedTokenVal.put("end",      TTEND);
            reservedTokenVal.put("array",    TARAY);
            reservedTokenVal.put("of",       TTTOF);
            reservedTokenVal.put("func",     TFUNC);
            reservedTokenVal.put("void",     TVOID);
            reservedTokenVal.put("const",    TCONS);
            reservedTokenVal.put("int",      TINTG);
            reservedTokenVal.put("real",    TREAL);
            reservedTokenVal.put("bool",    TBOOL);
            reservedTokenVal.put("for",     TTFOR);
            reservedTokenVal.put("repeat",  TREPT);
            reservedTokenVal.put("until",   TUNTL);
            reservedTokenVal.put("if",      TIFTH);
            reservedTokenVal.put("else",    TELSE);
            reservedTokenVal.put("input",   TINPT);
            reservedTokenVal.put("print",   TPRIN);
            reservedTokenVal.put("println", TPRLN);
            reservedTokenVal.put("return",  TRETN);
            reservedTokenVal.put("not",     TNOTT);
            reservedTokenVal.put("and",     TTAND);
            reservedTokenVal.put("or",      TTTOR);
            reservedTokenVal.put("xor",     TTXOR);
            reservedTokenVal.put("true",    TTRUE);
            reservedTokenVal.put("false",   TFALS);

            //adds delimiters into list of reserved characters/character sequences 
            reservedTokenVal.put(";",       TSEMI);
            reservedTokenVal.put("[",       TLBRK);
            reservedTokenVal.put("]",       TRBRK);
            reservedTokenVal.put("(",       TLPAR);
            reservedTokenVal.put(")",       TRPAR);
            reservedTokenVal.put(",",       TCOMA);
            reservedTokenVal.put("=",       TEQUL);
            reservedTokenVal.put("+",       TPLUS);
            reservedTokenVal.put("-",       TMINS);
            reservedTokenVal.put("*",       TSTAR);
            //reservedTokenVal.put("/",       TDIVD);
            reservedTokenVal.put("%",       TPERC);
            reservedTokenVal.put("^",       TCART);
            reservedTokenVal.put("<",       TLESS);
            reservedTokenVal.put(">",       TGRTR);
            reservedTokenVal.put("!",       TNOTT);
            //reservedTokenVal.put("\"",      TSTRG);       Change this to ascii value
            reservedTokenVal.put(":",       TCOLN);
            reservedTokenVal.put(".",       TDOTT);
            reservedTokenVal.put("<=",      TLEQL);
            reservedTokenVal.put(">=",      TGEQL);
            reservedTokenVal.put("!=",      TNEQL);
            reservedTokenVal.put("==",      TEQEQ);
            reservedTokenVal.put("+=",      TPLEQ);
            reservedTokenVal.put("-=",      TMNEQ);
            reservedTokenVal.put("*=",      TSTEQ);
            reservedTokenVal.put("/=",      TDVEQ);

        }

        if(assignmentOperators.isEmpty()){
            assignmentOperators.put("<=",      TLEQL);
            assignmentOperators.put(">=",      TGEQL);
            assignmentOperators.put("!=",      TNEQL);
            assignmentOperators.put("==",      TEQEQ);
            assignmentOperators.put("+=",      TPLEQ);
            assignmentOperators.put("-=",      TMNEQ);
            assignmentOperators.put("*=",      TSTEQ);
            assignmentOperators.put("/=",      TDVEQ);

        }

        if(singleOperators.isEmpty()){
            singleOperators.put(";",       TSEMI);
            singleOperators.put("[",       TLBRK);
            singleOperators.put("]",       TRBRK);
            singleOperators.put("(",       TLPAR);
            singleOperators.put(")",       TRPAR);
            singleOperators.put(",",       TCOMA);
            singleOperators.put("=",       TEQUL);
            singleOperators.put("+",       TPLUS);
            singleOperators.put("-",       TMINS);
            singleOperators.put("*",       TSTAR);
            singleOperators.put("/",       TDIVD);
            singleOperators.put("%",       TPERC);
            singleOperators.put("^",       TCART);
            singleOperators.put("<",       TLESS);
            singleOperators.put(">",       TGRTR);
            singleOperators.put("!",       TNOTT);
            singleOperators.put(":",       TCOLN);
            singleOperators.put(".",       TDOTT);
        }


    }

    public static int checkReserved(String lexeme){
        lexeme = lexeme.toLowerCase();  //convert lexeme to lowercase since keywords are not required to be case sensitive
        Integer tokenVal = 0;
        
        for(Map.Entry<String, Integer> special : reservedTokenVal.entrySet()){
            if(lexeme.equals(special.getKey())){
                tokenVal = special.getValue();
            }
        }

        if(tokenVal == 0){
            return -1;
        }
        else{
            return tokenVal;
        }
    }

    public static int checkOperators(String lexeme){
        lexeme = lexeme.toLowerCase();  //convert lexeme to lowercase since keywords are not required to be case sensitive
        Integer tokenVal = 0;
        
        for(Map.Entry<String, Integer> special : singleOperators.entrySet()){
            if(lexeme.equals(special.getKey())){
                tokenVal = special.getValue();
            }
        }

        if(tokenVal == 0){
            return -1;
        }
        else{
            return tokenVal;
        }
    }

    public static int checkAssignment(String lexeme){
        //lexeme = lexeme.toLowerCase();  //convert lexeme to lowercase since keywords are not required to be case sensitive
        Integer tokenVal = 0;
        
        for(Map.Entry<String, Integer> special : assignmentOperators.entrySet()){
            if(lexeme.equals(special.getKey())){
                tokenVal = special.getValue();
            }
        }

        if(tokenVal == 0){
            return -1;
        }
        else{
            return tokenVal;
        }
    }

}
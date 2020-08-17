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

    private int tokNo, ln, col;
    private String lex;

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

}
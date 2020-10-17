/*
NASGNS <asgnlist> ::= <asgnstat> , <asgnlist>
Special <asgnlist> ::= <asgnstat>
    -left factoring-
        <asgnlist> ::= <asgnstat> opt_asgnlist
        opt_asgnlist ::= , <asgnlist> | e

Special <asgnstat> ::= <var> <asgnop> <bool>
NASGN <asgnop> ::= =
NPLEQ <asgnop> ::= +=
NMNEQ <asgnop> ::= -=
NSTEQ <asgnop> ::= *=
NDVEQ <asgnop> ::= /=
*/
import java.util.ArrayList; 
public class NASGNS{

    public static StNode asgnlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NASGNSnode = new StNode();
        NASGNSnode.setNodeID("NASGNS");

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }
        StNode asgnstat = NSTATS.asgnstat(tokenList, sTable);
        NASGNSnode.setLeft(asgnstat);

        StNode opt_asgnlist = opt_asgnlist(tokenList, sTable);
        NASGNSnode.setRight(opt_asgnlist);
        
        return NASGNSnode;
    }

    public static StNode opt_asgnlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_asgnlist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);
            StNode asgnlist = asgnlist(tokenList, sTable);
            opt_asgnlist.setLeft(asgnlist);
        }

        return opt_asgnlist;
    }
}
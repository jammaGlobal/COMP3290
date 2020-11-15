/*
NREPT <repstat> ::= repeat ( <asgnlist> ) <stats> until <bool>
*/
import java.util.ArrayList; 
public class NREPT{

    public static StNode reptstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NREPTnode = new StNode();
        

        if(tokenList.get(0).getTokenNo() != 18){
            String error = "For statement missing 'repeat' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NREPTnode;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 35){
            String error = "For statement missing left parenthesis '('";
            sTable.parseError(tokenList.get(0), error);
            return NREPTnode;
        }
        tokenList.remove(0);

        StNode asgnlist = NASGNS.asgnlist(tokenList, sTable);
        if(asgnlist.isNUNDEF() && asgnlist.isNotEmptyContainsError()){
            return NREPTnode;
        }

        if(tokenList.get(0).getTokenNo() != 36){
            String error = "For statement missing right parenthesis ')'";
            sTable.parseError(tokenList.get(0), error);
            return NREPTnode;
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList, sTable);
        if(stats.isNUNDEF() && stats.isNotEmptyContainsError()){
            return NREPTnode;
        }
        

        if(tokenList.get(0).getTokenNo() != 19){
            String error = "For statement missing 'until' keyword'";
            sTable.parseError(tokenList.get(0), error);
            return NREPTnode;
        }
        tokenList.remove(0);


        StNode bool = NBOOL.bool(tokenList, sTable);
        if(bool.isNUNDEF() && bool.isNotEmptyContainsError()){
            return NREPTnode;
        }

        NREPTnode.setLeft(asgnlist);
        NREPTnode.setMiddle(stats);
        NREPTnode.setRight(bool);

        NREPTnode.setNodeID("NREPT");
        return NREPTnode;
    }
}
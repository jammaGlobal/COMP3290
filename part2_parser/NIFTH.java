/*
NIFTH <ifstat> ::= if ( <bool> ) <stats> end
NIFTE <ifstat> ::= if ( <bool> ) <stats> else <stats> end
    -left factoring-
        <ifstat> ::= if ( <bool> ) <stats> opt_else_stats end
        opt_else_stats ::= else <stats> | e 
*/

//Need to code the left factoring
import java.util.ArrayList; 
public class NIFTH{

    public static StNode ifstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NIFTHnode = new StNode();

        if(tokenList.get(0).getTokenNo() != 20){
            String error = "If statement missing initial 'if' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NIFTHnode;
        }
        tokenList.remove(0);


        if(tokenList.get(0).getTokenNo() != 35){
            String error = "If statement missing left parenthesis '('";
            sTable.parseError(tokenList.get(0), error);
            return NIFTHnode;
        }
        tokenList.remove(0);

        StNode bool = NBOOL.bool(tokenList, sTable);
        if(bool.isNUNDEF() && bool.isNotEmptyContainsError()){
            return NIFTHnode;
        }

        if(tokenList.get(0).getTokenNo() != 36){
            String error = "If statement missing right parenthesis ')'";
            sTable.parseError(tokenList.get(0), error);
            return NIFTHnode;
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList, sTable);
        if(stats.isNUNDEF() && stats.isNotEmptyContainsError()){
            return NIFTHnode;
        }

        if(tokenList.get(0).getTokenNo() == 8){
            NIFTHnode.setRight(stats);
            tokenList.remove(0);
            NIFTHnode.setNodeID("NIFTH");
        }
        else if(tokenList.get(0).getTokenNo() == 21){
            
            tokenList.remove(0);

            StNode stats_ = NSTATS.stats(tokenList, sTable);
            if(stats_.isNUNDEF() && stats_.isNotEmptyContainsError()){
                return NIFTHnode;
            }
            

            if(tokenList.get(0).getTokenNo() != 8){
                String error = "If statement missing 'end' keyword";
                sTable.parseError(tokenList.get(0), error);
                return NIFTHnode;
            }
            tokenList.remove(0);

            NIFTHnode.setMiddle(stats);
            NIFTHnode.setRight(stats_);
            NIFTHnode.setNodeID("NIFTE");
        }
        else{
            String error = "If statement missing 'end' keyword if not if-statement with 'else' keyword OR missing 'else' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NIFTHnode;
        }

        NIFTHnode.setLeft(bool);
        
        return NIFTHnode;
    }
}
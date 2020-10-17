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
            //error
        }
        tokenList.remove(0);


        if(tokenList.get(0).getTokenNo() != 35){
            //error
        }
        tokenList.remove(0);

        StNode bool = NBOOL.bool(tokenList, sTable);
        NIFTHnode.setLeft(bool);

        if(tokenList.get(0).getTokenNo() != 36){
            //error
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList, sTable);
        

        if(tokenList.get(0).getTokenNo() == 8){
            NIFTHnode.setRight(stats);
            tokenList.remove(0);
            NIFTHnode.setNodeID("NIFTH");
        }
        else if(tokenList.get(0).getTokenNo() == 21){
            NIFTHnode.setMiddle(stats);
            tokenList.remove(0);

            StNode stats_ = NSTATS.stats(tokenList, sTable);
            NIFTHnode.setRight(stats_);

            if(tokenList.get(0).getTokenNo() != 8){

            }
            tokenList.remove(0);
            NIFTHnode.setNodeID("NIFTE");
        }
        else{
            //error
        }


        
        
        return NIFTHnode;
    }
}
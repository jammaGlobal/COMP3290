/*
NREPT <repstat> ::= repeat ( <asgnlist> ) <stats> until <bool>
*/
import java.util.ArrayList; 
public class NREPT{

    public static StNode reptstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NREPTnode = new StNode();
        NREPTnode.setNodeID("NREPT");

        if(tokenList.get(0).getTokenNo() != 18){
            //error
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 35){
            //error
        }
        tokenList.remove(0);

        StNode asgnlist = NASGNS.asgnlist(tokenList, sTable);
        NREPTnode.setLeft(asgnlist);

        if(tokenList.get(0).getTokenNo() != 36){
            //error
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList, sTable);
        NREPTnode.setMiddle(stats);

        if(tokenList.get(0).getTokenNo() != 19){
            //error
        }
        tokenList.remove(0);


        StNode bool = NBOOL.bool(tokenList, sTable);
        NREPTnode.setRight(bool);

        
        return NREPTnode;
    }
}
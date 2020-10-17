/*
NFOR <forstat> ::= for ( <asgnlist> ; <bool> ) <stats> end
*/
import java.util.ArrayList; 
public class NFOR{

    public static StNode forstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFORnode = new StNode();
        NFORnode.setNodeID("NFOR");
        
        if(tokenList.get(0).getTokenNo() != 17){
            //error
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 35){
            //error
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }

        StNode asgnlist = NASGNS.asgnlist(tokenList, sTable);
        NFORnode.setLeft(asgnlist);

        if(tokenList.get(0).getTokenNo() != 56){
            //error
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }

        StNode bool = NBOOL.bool(tokenList, sTable);
        NFORnode.setMiddle(bool);
        
        if(tokenList.get(0).getTokenNo() != 36){
            //error
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList, sTable);
        NFORnode.setRight(stats);

        if(tokenList.get(0).getTokenNo() != 8){
            //error
        }
        tokenList.remove(0);

        return NFORnode;
    }
}
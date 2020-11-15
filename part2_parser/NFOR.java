/*
NFOR <forstat> ::= for ( <asgnlist> ; <bool> ) <stats> end
*/
import java.util.ArrayList; 
public class NFOR{

    public static StNode forstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFORnode = new StNode();
        
        
        if(tokenList.get(0).getTokenNo() != 17){
            String error = "For statement missing initial 'for' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NFORnode;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 35){
            String error = "For statement missing left parenthesis '('";
            sTable.parseError(tokenList.get(0), error);
            return NFORnode;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "For statement missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return NFORnode;
        }

        StNode asgnlist = NASGNS.asgnlist(tokenList, sTable);
        if(asgnlist.isNUNDEF() && asgnlist.isNotEmptyContainsError()){
            return NFORnode;
        }


        if(tokenList.get(0).getTokenNo() != 56){
            String error = "For statement missing semicolon";
            sTable.parseError(tokenList.get(0), error);
            return NFORnode;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 58){        //might have to get rid of this identifier if statement
            String error = "For statement missing identifier in conditional boolean";
            sTable.parseError(tokenList.get(0), error);
            return NFORnode;
        }

        StNode bool = NBOOL.bool(tokenList, sTable);
        if(bool.isNUNDEF() && bool.isNotEmptyContainsError()){
            return NFORnode;
        }
        
        if(tokenList.get(0).getTokenNo() != 36){
            String error = "For statement missing right parenthesis ')'";
            sTable.parseError(tokenList.get(0), error);
            return NFORnode;
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList, sTable);
        if(stats.isNUNDEF() && stats.isNotEmptyContainsError()){
            return NFORnode;
        }

        if(tokenList.get(0).getTokenNo() != 8){
            String error = "For statement missing 'end' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NFORnode;
        }
        tokenList.remove(0);

        NFORnode.setLeft(asgnlist);
        NFORnode.setMiddle(bool);
        NFORnode.setRight(stats);

        NFORnode.setNodeID("NFOR");

        return NFORnode;
    }
}
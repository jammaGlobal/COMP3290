/*
NMAIN <mainbody> ::= main <slist> begin <stats> end CD20 <id>
*/
import java.util.ArrayList; 
public class NMAIN {
    public static StNode mainbody(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NMAINnode = new StNode();
        

        if(tokenList.get(0).getTokenNo() != 6){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                String error = "Main is missing 'main' keyword";
                sTable.parseError(tokenList.get(0), error);
                return NMAINnode;
            }
        }
        tokenList.remove(0);
        
        StNode slist = NSDLST.slist(tokenList,sTable);
        if(slist.isNotEmptyContainsError()){
            return NMAINnode;
        }

        if(tokenList.get(0).getTokenNo() != 7){
            String error = "Main is missing 'begin' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NMAINnode;
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList,sTable);
        if(stats.isNUNDEF() && stats.isNotEmptyContainsError()){       //change to error
            return NMAINnode;
        }

        if(tokenList.get(0).getTokenNo() != 8){
            String error = "Main is missing 'end' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NMAINnode;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 1){
            String error = "Main is missing 'CD20' keyword";
            sTable.parseError(tokenList.get(0), error);
            return NMAINnode;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Main is missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return NMAINnode;
        }
        tokenList.remove(0);

        NMAINnode.setLeft(slist);
        NMAINnode.setRight(stats);

        NMAINnode.setNodeID("NMAIN");

        return NMAINnode;
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 6){
                tokPos = i;
                tokNo = tokenList.get(i).getTokenNo();
                break;
            }
        }

        if(tokPos == -1){
            //Unsuccessful error recovery
            return false;
        }
        else{
            //Found comma before funcs and before main
            if(tokNo == 6){
                while(tokenList.get(0).getTokenNo() != 6){
                    tokenList.remove(0);
                }
            }
            return true;
        }
        //check next comma 
        //check next function
        //check main
        
    }




}
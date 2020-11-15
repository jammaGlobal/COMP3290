/*
        <elist> ::= <bool> opt_elist 
        opt_elist ::= , <elist> | e
*/
import java.util.ArrayList; 
public class NEXPL{
    public static StNode elist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NEXPLnode = new StNode();

        StNode bool = NBOOL.bool(tokenList,sTable);
        if(bool.isNUNDEF() && bool.isNotEmptyContainsError()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NEXPLnode.notEmptyContainsError();
                return NEXPLnode;
            }
        }
        

        StNode opt_elist = opt_elist(tokenList,sTable);
        if(opt_elist == null && !bool.isNotEmptyContainsError()){  
            NEXPLnode.setLeft(bool);
            return NEXPLnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_elist == null && bool.isNotEmptyContainsError()){
            return NEXPLnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_elist != null && (opt_elist.isNotEmptyContainsError() && opt_elist.isNUNDEF())){
            NEXPLnode.setLeft(bool);
            return NEXPLnode;
        }
        

        NEXPLnode.setLeft(bool);
        NEXPLnode.setRight(opt_elist);
        NEXPLnode.setNodeID("NEXPL");

        return NEXPLnode;
    }

    public static StNode opt_elist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_elist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode elist = elist(tokenList,sTable);
            if(elist.isNUNDEF() && elist.isNotEmptyContainsError()){
                opt_elist.notEmptyContainsError();
                return opt_elist;
            }
            opt_elist.setLeft(elist);

            return opt_elist;
        }

        return null;
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 32){
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
            //Found comma
            if(tokNo == 32){
                while(tokenList.get(0).getTokenNo() != 32){
                    tokenList.remove(0);
                }
            }
            return true;
        }
        
    }
}
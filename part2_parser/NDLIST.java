/*
NDLIST <dlist> ::= <decl> , <dlist>
Special <dlist> ::= <decl>
    -left factoring-
        <dlist> ::= <decl> opt_dlist
        opt_dlist ::= , <dlist> | e
Special <decl> ::= <sdecl> | <arrdecl>

*/
import java.util.ArrayList; 
public class NDLIST{

    public static StNode dlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NDLISTnode = new StNode();
        

        StNode decl = decl(tokenList,sTable);
        if(decl.isNUNDEF() && decl.isNotEmptyContainsError()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NDLISTnode.notEmptyContainsError();
                return NDLISTnode;
            }
        }
        

        StNode opt_dlist = opt_dlist(tokenList,sTable);

        //The case where there is only arrdecl, opt_arrdecls is empty
        if(opt_dlist == null && !decl.isNotEmptyContainsError()){  
            NDLISTnode.setLeft(decl);
            return NDLISTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_dlist == null && decl.isNotEmptyContainsError()){
            return NDLISTnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_dlist != null && (opt_dlist.isNotEmptyContainsError() && opt_dlist.isNUNDEF())){
            NDLISTnode.setLeft(decl);
            return NDLISTnode;
        }

        NDLISTnode.setLeft(decl);
        NDLISTnode.setRight(opt_dlist);
        NDLISTnode.setNodeID("NDLIST");
        return NDLISTnode;
    }

    public static StNode decl(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode decl = new StNode();


        if(tokenList.get(2).getTokenNo() == 14 || tokenList.get(2).getTokenNo() == 15 || tokenList.get(2).getTokenNo() == 16){
            StNode sdecl = NFLIST.sdecl(tokenList,sTable);
            decl.setLeft(sdecl);
        }
        else{
            StNode arrdecl = NALIST.arrdecl(tokenList,sTable);
            decl.setLeft(arrdecl);
        }
        
        return decl;
    }

    public static StNode opt_dlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_dlist = new StNode();
        
        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode dlist = dlist(tokenList, sTable);
            if(dlist.isNUNDEF() && dlist.isNotEmptyContainsError()){
                opt_dlist.notEmptyContainsError();
                return opt_dlist;
            }

            opt_dlist.setLeft(dlist);
            return opt_dlist;
        }
        else{
            return null;
        }

    } 

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 32 || tokenList.get(i).getTokenNo() == 11 || 
                tokenList.get(i).getTokenNo() == 6){
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
            if(tokNo == 32){
                while(tokenList.get(0).getTokenNo() != 32){
                    tokenList.remove(0);
                }
            }
            //Found no comma and found funcs before main
            else if(tokNo == 11){
                while(tokenList.get(0).getTokenNo() != 11){
                    tokenList.remove(0);
                }
            }
            //Found no comma or funcs and found main
            else if(tokNo == 6){
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
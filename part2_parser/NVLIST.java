/*
NVLIST <vlist> ::= <var> , <vlist>
Special <vlist> ::= <var>
    -left factoring-
        <vlist> ::= <var> opt_vlist
        opt_vlist = , <vlist> | e
*/
import java.util.ArrayList; 
public class NVLIST{

    public static StNode vlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NVLISTnode = new StNode();

        StNode var = NPOW.var(tokenList, sTable);

        //The combination of both the var and optional vlist having errors is not possible
        //as this if statement prevents the function continuing to the optional vlist if there
        //is an unrecoverable error in var
        if(var.isNUNDEF()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NVLISTnode.notEmptyContainsError();
                return NVLISTnode;
            }
        }
        

        StNode opt_vlist = opt_vlist(tokenList, sTable);
        if(opt_vlist == null && !var.isNUNDEF()){  
            NVLISTnode.setLeft(var);
            return NVLISTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_vlist == null && var.isNUNDEF()){
            return NVLISTnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_vlist != null && (opt_vlist.isNotEmptyContainsError() && opt_vlist.isNUNDEF())){
            NVLISTnode.setLeft(var);
            return NVLISTnode;
        }

        NVLISTnode.setLeft(var);
        NVLISTnode.setRight(opt_vlist);
        NVLISTnode.setNodeID("NVLIST");

        return NVLISTnode;
    }

    public static StNode opt_vlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_vlist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode vlist = vlist(tokenList, sTable); 
            if(vlist.isNUNDEF() && vlist.isNotEmptyContainsError()){
                opt_vlist.notEmptyContainsError();
                return opt_vlist;
            }

            opt_vlist.setLeft(vlist);

            return opt_vlist;
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
/*
NALIST <arrdecls> ::= < > , <arrdecls>
Special <arrdecls> ::= <arrdecl>
    -left factoring-
        <arrdecls> ::= <arrdecl> opt_arrdecls
        opt_arrdecls ::= , <arrdecls> | ε

    

*/
import java.util.ArrayList; 
public class NALIST{
    public static StNode arrdecls(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NALISTnode = new StNode();
        
        
        StNode arrdecl = arrdecl(tokenList,sTable);

        //Case where there is an error in arrdecl
        if(arrdecl.isNUNDEF()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NALISTnode.notEmptyContainsError();
                return NALISTnode;
            }
        }

        StNode opt_arrdecls = opt_arrdecls(tokenList,sTable);

        //The case where there is only arrdecl, opt_arrdecls is empty
        if(opt_arrdecls == null && !arrdecl.isNUNDEF()){  
            NALISTnode.setLeft(arrdecl);
            return NALISTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_arrdecls == null && arrdecl.isNUNDEF()){
            return NALISTnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_arrdecls != null && (opt_arrdecls.isNotEmptyContainsError() && opt_arrdecls.isNUNDEF())){
            NALISTnode.setLeft(arrdecl);
            return NALISTnode;
        }

        NALISTnode.setNodeID("NALIST");

        NALISTnode.setLeft(arrdecl);
        NALISTnode.setRight(opt_arrdecls);

        return NALISTnode;
    }

    public static StNode opt_arrdecls(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_arrdecls = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);
 
            StNode arrdecls = arrdecls(tokenList,sTable);
            if(arrdecls.isNUNDEF() && arrdecls.isNotEmptyContainsError()){
                opt_arrdecls.notEmptyContainsError();
                return opt_arrdecls;
            }

            opt_arrdecls.setLeft(arrdecls);
            return opt_arrdecls;
        }
        else{
            return null;
        }
        
    }

    //This production cannot be empty, therefore it is fine to use an empty node to determine errors
    public static StNode arrdecl(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode arrdecl = new StNode();
        

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Array declaration missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return arrdecl;
        }

        TableEntry entry = new TableEntry(tokenList.get(0));
        

        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() == 46){
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 58){
                String error = "Array declaration missing type id";
                sTable.parseError(tokenList.get(0), error);
                return arrdecl;
            }
            
            TableEntry ent = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(ent);
            arrdecl.setSymbolTableReference(ent);
            tokenList.remove(0);
        }
        else{
            String error = "Array declaration missing colon operator ':'";
            sTable.parseError(tokenList.get(0), error);
            return arrdecl;
        }

        sTable.setTableEntry(entry);
        arrdecl.setSymbolTableReference(entry);

        arrdecl.setNodeID("NARRD");
        return arrdecl;
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
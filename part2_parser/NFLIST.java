/*
NFLIST <fields> ::= <sdecl> , <fields>
Special <fields> ::= <sdecl>
    -left factoring-
        <fields> ::= <sdecl> opt_fields
        opt_fields ::= , <fields> | ε

NSDECL <sdecl> ::= <id> : <stype>
*/
import java.util.ArrayList; 
public class NFLIST{
    public static StNode fields(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFLISTnode = new StNode();
        

        StNode sdecl = sdecl(tokenList, sTable);

        if(sdecl.isNUNDEF()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NFLISTnode.notEmptyContainsError();
                return NFLISTnode;
            }
        }
        

        StNode opt_fields = opt_fields(tokenList, sTable);

        if(opt_fields == null && !sdecl.isNUNDEF()){  
            NFLISTnode.setLeft(sdecl);
            return NFLISTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_fields == null && sdecl.isNUNDEF()){
            return NFLISTnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_fields != null && (opt_fields.isNotEmptyContainsError() && opt_fields.isNUNDEF())){
            NFLISTnode.setLeft(sdecl);
            return NFLISTnode;
        }
        

        NFLISTnode.setLeft(sdecl);
        NFLISTnode.setRight(opt_fields);

        NFLISTnode.setNodeID("NFLIST");   

        return NFLISTnode;
    }

    public static StNode sdecl(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode sdecl = new StNode();
        

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Statement declaration missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return sdecl;
        }

        TableEntry entry = new TableEntry(tokenList.get(0));
        

        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() == 46){
            tokenList.remove(0);
            
            StNode stype = NFUND.stype(tokenList, sTable);
            if(stype.isNUNDEF() && stype.isNotEmptyContainsError()){
                return sdecl;
            }
            sdecl.setLeft(stype);
        }
        else{
            String error = "Statement declaration missing colon";
            sTable.parseError(tokenList.get(0), error);
            return sdecl;
        }

        sTable.setTableEntry(entry);
        sdecl.setSymbolTableReference(entry);
        sdecl.setNodeID("NSDECL");

        return sdecl;

    }
    

    public static StNode opt_fields(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_fields = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode fields = fields(tokenList, sTable);
            //fields will be NUNDEF but not errored if it is the last decleration
            if(fields.isNUNDEF() && fields.isNotEmptyContainsError()){      
                opt_fields.notEmptyContainsError();
                return opt_fields;
            }
            opt_fields.setLeft(fields);

            return opt_fields;
        }
        else{
            return null;
        }

        
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 32 || tokenList.get(i).getTokenNo() == 9 || 
                tokenList.get(i).getTokenNo() == 11 || tokenList.get(i).getTokenNo() == 6){
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
            //Found comma before arrays, funcs and main
            if(tokNo == 32){
                while(tokenList.get(0).getTokenNo() != 32){
                    tokenList.remove(0);
                }
            }
            //Found no comma and found funcs before main
            else if(tokNo == 9){
                while(tokenList.get(0).getTokenNo() != 9){
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
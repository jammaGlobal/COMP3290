/*
NPLIST <params> ::= <param> , <params>
Special <params> ::= <param>
    -left factoring-
        <params> ::= <param> opt_params
        opt_params ::= , <params> | e



NSIMP <param> ::= <sdecl>
NARRP <param> ::= <arrdecl>
NARRC <param> ::= const <arrdecl>

*/
import java.util.ArrayList; 
public class NPLIST{
    public static StNode params(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NPLISTnode = new StNode();
        

        StNode param = param(tokenList,sTable);
        if(param.isNUNDEF()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NPLISTnode.notEmptyContainsError();
                return NPLISTnode;
            }
        }
        

        StNode opt_params = opt_params(tokenList,sTable);
        
        if(opt_params == null && !param.isNUNDEF()){  
            NPLISTnode.setLeft(param);
            return NPLISTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_params == null && param.isNUNDEF()){
            return NPLISTnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_params != null && (opt_params.isNotEmptyContainsError() && opt_params.isNUNDEF())){
            NPLISTnode.setLeft(param);
            return NPLISTnode;
        }

        NPLISTnode.setNodeID("NPLIST");
        NPLISTnode.setLeft(param);
        NPLISTnode.setRight(opt_params);

        return NPLISTnode;
    }

    public static StNode param(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode param = new StNode();
        String nodeId = "";

        if(tokenList.get(0).getTokenNo() == 13){
            tokenList.remove(0);

            StNode arrdecl = NALIST.arrdecl(tokenList,sTable);
            if(arrdecl.isNUNDEF() && arrdecl.isNotEmptyContainsError()){
                return param;
            }

            param.setLeft(arrdecl);
            nodeId = "NARRC";
        }
        else if(tokenList.get(0).getTokenNo() == 58){
            //Check if token list is length-3
            if(tokenList.get(2).getTokenNo() == 14 || tokenList.get(2).getTokenNo() == 15 || tokenList.get(2).getTokenNo() == 16){
                StNode sdecl = NFLIST.sdecl(tokenList,sTable);
                if(sdecl.isNUNDEF() && sdecl.isNotEmptyContainsError()){
                    return param;
                }

                param.setLeft(sdecl);
                nodeId = "NSIMP";
            }
            else{
                StNode arrdecl = NALIST.arrdecl(tokenList,sTable);
                if(arrdecl.isNUNDEF() && arrdecl.isNotEmptyContainsError()){
                    return param;
                }

                param.setLeft(arrdecl);
                nodeId = "NARRP";
            }
            

        }
        else{
            String error = "Parameter missing initial 'const' keyword or missing inital identifier";
            sTable.parseError(tokenList.get(0), error);
            return param;
        }

        param.setNodeID(nodeId);
        return param;
    }

    public static StNode opt_params(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_params = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode params = params(tokenList,sTable);
            if(params.isNUNDEF() && params.isNotEmptyContainsError()){
                opt_params.notEmptyContainsError();
                return opt_params;
            }
            opt_params.setLeft(params);
            return opt_params;
        }
        

        return null;
    }

    //In Funcs so check if next parameter or main
    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 32 || tokenList.get(i).getTokenNo() == 6){
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
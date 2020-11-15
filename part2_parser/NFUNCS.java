/*
NFUNCS  <funcs> ::= <func> <opt_funcs>
        <opt_funcs> ::= <funcs> | ε
Special <funcs> ::= ε


*/
import java.util.ArrayList; 
public class NFUNCS{
    public static StNode funcs(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFUNCSnode = new StNode();

        StNode func = NFUND.func(tokenList, sTable);
        if(func == null){
            if(!errorRecovery(tokenList, sTable)){
                return NFUNCSnode;
            }

            if(tokenList.get(0).getTokenNo() == 6){
                return NFUNCSnode;
            }
        }
        else if(func.isNUNDEF()){
            if(!errorRecovery(tokenList, sTable)){
                NFUNCSnode.notEmptyContainsError();
                return NFUNCSnode;
            }
        }

        //func error recovery fix
        
        StNode opt_funcs = opt_funcs(tokenList, sTable);

        
        if(opt_funcs == null && func != null && !func.isNUNDEF()){  
            NFUNCSnode.setLeft(func);
                return NFUNCSnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_funcs == null && func != null && func.isNUNDEF()){
            return NFUNCSnode;
        }
        //
        else if(opt_funcs != null && (opt_funcs.isNotEmptyContainsError() && opt_funcs.isNUNDEF())){
            NFUNCSnode.setLeft(func);
            return NFUNCSnode;
        }

        NFUNCSnode.setLeft(func);
        NFUNCSnode.setRight(opt_funcs);

        NFUNCSnode.setNodeID("NFUNCS");
        
        return NFUNCSnode;
    }

    public static StNode opt_funcs(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_funcs = new StNode();

        if(tokenList.get(0).getTokenNo() == 11){

            StNode funcs = NFUNCS.funcs(tokenList, sTable);

            if(funcs.isNUNDEF() && funcs.isNotEmptyContainsError()){
                opt_funcs.notEmptyContainsError();
                return opt_funcs;
            }

            opt_funcs.setLeft(funcs);
            return opt_funcs;

        }

        return null;
        
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){
        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 11 || tokenList.get(i).getTokenNo() == 6){
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
            //Found no comma and found funcs before main
            if(tokNo == 11){
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
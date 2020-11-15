import java.util.ArrayList; 
public class NILIST{
        //<initlist> ::= <init> opt_comma
        //opt_comma ::= , <initlist> | ε
    public static StNode initlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NILISTnode = new StNode();
        
        StNode NINITnode = NINIT.init(tokenList, sTable);

        //Case where there is an error in init
        if(NINITnode.isNUNDEF()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NINITnode.notEmptyContainsError();
                return NINITnode;
            }
        }
        

        StNode opt_comma = opt_comma(tokenList, sTable);

        if(opt_comma == null && !NINITnode.isNUNDEF()){  
            NILISTnode.setLeft(NINITnode);
             return NILISTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_comma == null && NINITnode.isNUNDEF()){
            return NILISTnode;
        }
        //
        else if(opt_comma != null && (opt_comma.isNotEmptyContainsError() && opt_comma.isNUNDEF())){
            NILISTnode.setNodeID("NILIST");
            NILISTnode.setLeft(NINITnode);
            return NILISTnode;
        }

        NILISTnode.setLeft(NINITnode);
        NILISTnode.setRight(opt_comma);

        NILISTnode.setNodeID("NILIST");
        return NILISTnode;

    }

    public static StNode opt_comma(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_comma = new StNode(); 

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode initlist = initlist(tokenList, sTable); 
            if(initlist.isNUNDEF() && initlist.isNotEmptyContainsError()){
                opt_comma.notEmptyContainsError();
                return opt_comma;
            }

            opt_comma.setLeft(initlist);
            return opt_comma;
        }
        else{
            return null;
        }
        //when opt_comma id is NUNDEF then its an empty case, if comma then other case
        
        
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){
        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 32 || tokenList.get(i).getTokenNo() == 3 || 
                tokenList.get(i).getTokenNo() == 9 || tokenList.get(i).getTokenNo() == 11 || 
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

        //Found comma before types, arrays, funcs and main
        if(tokNo == 32){
            while(tokenList.get(0).getTokenNo() != 32){
                tokenList.remove(0);
            }
        }
        //Found types before comma, arrays, funcs and main
        else if(tokNo == 3){
            while(tokenList.get(0).getTokenNo() != 3){
                tokenList.remove(0);
            }
        }
        //Found arrays before comma, types, funcs and main
        else if(tokNo == 9){
            while(tokenList.get(0).getTokenNo() != 9){
                tokenList.remove(0);
            }
        }
        //Found funcs before comma, types, arrays and main
        else if(tokNo == 11){
            while(tokenList.get(0).getTokenNo() != 11){
                tokenList.remove(0);
            }
        }
        //Found main before comma, types, funcs and arrays
        else if(tokNo == 6){
            while(tokenList.get(0).getTokenNo() != 6){
                tokenList.remove(0);
            }
        }
        return true;
        
    }

    


}
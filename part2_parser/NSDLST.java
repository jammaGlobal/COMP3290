/*
NSDLST <slist> ::= <sdecl> , <slist>
Special <slist> ::= <sdecl>
Special <slist> ::= ε
    -left factoring-
        <slist> ::= <sdecl> opt_slist | e
        opt_slist ::= , <slist> | e
*/
import java.util.ArrayList; 
public class NSDLST {
    public static StNode slist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NSDLSTnode = new StNode();

        if(tokenList.get(0).getTokenNo() == 58){
            StNode sdecl = NFLIST.sdecl(tokenList, sTable);
            if(sdecl.isNUNDEF()){
                if(errorRecovery(tokenList,sTable)){

                }
                else{
                    NSDLSTnode.notEmptyContainsError();
                    return NSDLSTnode;
                }
            }

            StNode opt_slist = opt_slist(tokenList, sTable);

            if(opt_slist == null && !sdecl.isNUNDEF()){  
                NSDLSTnode.setLeft(sdecl);
                 return NSDLSTnode;
            }
            //there has been an error in the next arrdecl that is within the next arrdecls function
            else if(opt_slist == null && sdecl.isNUNDEF()){
                return NSDLSTnode;
            }
            //
            else if(opt_slist != null && (opt_slist.isNotEmptyContainsError() && opt_slist.isNUNDEF())){
                NSDLSTnode.setLeft(sdecl);
                return NSDLSTnode;
            }

            NSDLSTnode.setLeft(sdecl);
            NSDLSTnode.setRight(opt_slist);
            NSDLSTnode.setNodeID("NSDLST");

            return NSDLSTnode;
        }
        
        return null;
    }

    public static StNode opt_slist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_slist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode slist = slist(tokenList, sTable);
            if(slist != null && slist.isNUNDEF() && slist.isNotEmptyContainsError()){
                opt_slist.notEmptyContainsError();
                return opt_slist;
            }

            opt_slist.setLeft(slist);
            return opt_slist;
        }
        else{
            return null;
        }
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){
        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 32 || tokenList.get(i).getTokenNo() == 7){
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
            //Found comma before begin
            if(tokNo == 32){
                while(tokenList.get(0).getTokenNo() != 32){
                    tokenList.remove(0);
                }
            }
            //Found no comma and found begin
            else if(tokNo == 7){
                while(tokenList.get(0).getTokenNo() != 7){
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

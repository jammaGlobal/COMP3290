/*
NPRLST <prlist> ::= <printitem> , <prlist>
Special <prlist> ::= <printitem>
    -left factoring-
        <prlist> ::= <printitem> opt_printlist
        opt_printlist ::= , <prlist> | e

Special <printitem> ::= <expr>
NSTRG <printitem> ::= <string>
*/
import java.util.ArrayList; 
public class NPRLST{
    public static StNode prlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NPRLSTnode = new StNode();
        

        StNode printitem = printitem(tokenList, sTable);
        if(printitem.isNUNDEF() && printitem.isNotEmptyContainsError()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NPRLSTnode.notEmptyContainsError();
                return NPRLSTnode;
            }
        }
        

        StNode opt_printlist = opt_printlist(tokenList, sTable);
        if(opt_printlist == null && !printitem.isNotEmptyContainsError()){  
            NPRLSTnode.setLeft(printitem);
            return NPRLSTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_printlist == null && printitem.isNotEmptyContainsError()){
            return NPRLSTnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_printlist != null && (opt_printlist.isNotEmptyContainsError() && opt_printlist.isNUNDEF())){
            NPRLSTnode.setLeft(printitem);
            return NPRLSTnode;
        }


        NPRLSTnode.setLeft(printitem);
        NPRLSTnode.setRight(opt_printlist);
        NPRLSTnode.setNodeID("NPRLST");
        return NPRLSTnode;
    }

    public static StNode printitem(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode printitem = new StNode();

        if(tokenList.get(0).getTokenNo() == 61){
            printitem.setNodeID("NSTRG");

            TableEntry entry = new TableEntry(tokenList.get(0));

            sTable.setTableEntry(entry);
            printitem.setSymbolTableReference(entry);

            tokenList.remove(0);
        }
        else{
            StNode expr = NNOT.expr(tokenList, sTable);
            if(expr.isNUNDEF() && expr.isNotEmptyContainsError()){
                printitem.notEmptyContainsError();
                return printitem;
            }

            printitem.setLeft(expr);
        }


        return printitem;
    }

    public static StNode opt_printlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_printlist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode prlist = prlist(tokenList, sTable);
            if(prlist.isNUNDEF() && prlist.isNotEmptyContainsError()){
                opt_printlist.notEmptyContainsError();
                return opt_printlist;
            }
            opt_printlist.setLeft(prlist);

            return opt_printlist;
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
            //Found comma before funcs and before main
            if(tokNo == 32){
                while(tokenList.get(0).getTokenNo() != 32){
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
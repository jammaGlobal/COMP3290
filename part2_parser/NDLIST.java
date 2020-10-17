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
        NDLISTnode.setNodeID("NDLIST");

        StNode sdecl = decl(tokenList,sTable);
        NDLISTnode.setLeft(sdecl);

        StNode opt_dlist = opt_dlist(tokenList,sTable);
        NDLISTnode.setRight(opt_dlist);

        

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
        }

        return opt_dlist;
    } 
}
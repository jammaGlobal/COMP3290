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
        NSDLSTnode.setNodeID("NSDLST");

        if(tokenList.get(0).getTokenNo() == 58){
            StNode sdecl = NFLIST.sdecl(tokenList, sTable);
            NSDLSTnode.setLeft(sdecl);

            StNode opt_slist = opt_slist(tokenList, sTable);
            NSDLSTnode.setRight(opt_slist);
            
        }

        

        return NSDLSTnode;
    }

    public static StNode opt_slist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_slist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode slist = slist(tokenList, sTable);
            opt_slist.setLeft(slist);
        }
        
        return opt_slist;
    }
}

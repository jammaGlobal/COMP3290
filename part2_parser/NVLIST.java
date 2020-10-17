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
        NVLISTnode.setNodeID("NVLIST");

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }

        StNode var = NPOW.var(tokenList, sTable);
        NVLISTnode.setLeft(var);

        StNode opt_vlist = opt_vlist(tokenList, sTable);
        NVLISTnode.setRight(opt_vlist);

        return NVLISTnode;
    }

    public static StNode opt_vlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_vlist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);
            StNode vlist = vlist(tokenList, sTable); 
            opt_vlist.setLeft(vlist);
        }

        return opt_vlist;
    }
}
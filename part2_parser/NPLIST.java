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
        NPLISTnode.setNodeID("NPLIST");

        StNode param = param(tokenList,sTable);
        NPLISTnode.setLeft(param);

        StNode opt_params = opt_params(tokenList,sTable);
        NPLISTnode.setRight(opt_params);

        return NPLISTnode;
    }

    public static StNode param(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode param = new StNode();

        if(tokenList.get(0).getTokenNo() == 13){
            tokenList.remove(0);
            StNode arrdecl = NALIST.arrdecl(tokenList,sTable);
            param.setLeft(arrdecl);
            param.setNodeID("NARRC");
        }
        else{
            //Check if token list is length-3
            if(tokenList.get(2).getTokenNo() == 14 || tokenList.get(2).getTokenNo() == 15 || tokenList.get(2).getTokenNo() == 16){
                StNode sdecl = NFLIST.sdecl(tokenList,sTable);
                param.setLeft(sdecl);
                param.setNodeID("NSIMP");
            }
            else{
                StNode arrdecl = NALIST.arrdecl(tokenList,sTable);
                param.setLeft(arrdecl);
                param.setNodeID("NARRP");
            }

        }

        return param;
    }

    public static StNode opt_params(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_params = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode params = params(tokenList,sTable);
            opt_params.setLeft(params);
        }
        

        return opt_params;
    }
}
/*
        <elist> ::= <bool> opt_elist 
        opt_elist ::= , <elist> | e
*/
import java.util.ArrayList; 
public class NEXPL{
    public static StNode elist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NEXPLnode = new StNode();

        StNode bool = NBOOL.bool(tokenList,sTable);
        NEXPLnode.setLeft(bool);

        StNode opt_elist = opt_elist(tokenList,sTable);
        NEXPLnode.setRight(opt_elist);

        return NEXPLnode;
    }

    public static StNode opt_elist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_elist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode elist = elist(tokenList,sTable);
            opt_elist.setLeft(elist);
        }

        return opt_elist;
    }
}
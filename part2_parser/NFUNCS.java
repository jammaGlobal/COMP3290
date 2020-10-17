/*
NFUNCS <funcs> ::= <func> <funcs>
Special <funcs> ::= ε
*/
import java.util.ArrayList; 
public class NFUNCS{
    public static StNode funcs(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFUNCSnode = new StNode();

        if(tokenList.get(0).getTokenNo() == 11){
            StNode func = NFUND.func(tokenList, sTable);
            NFUNCSnode.setLeft(func);

            StNode funcs_ = NFUNCS.funcs(tokenList, sTable);
            NFUNCSnode.setRight(funcs_);
        }
        
        return NFUNCSnode;
    }
}
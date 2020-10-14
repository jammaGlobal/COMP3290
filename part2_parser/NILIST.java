import java.util.ArrayList; 
public class NILIST{
        //<initlist> ::= <init> opt_comma
        //opt_comma ::= , <initlist> | ε
    public static StNode initlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NILISTnode = new StNode();
        NILISTnode.setNodeID("NILIST");
        
        StNode NINITnode = NINIT.init(tokenList, sTable);
        NILISTnode.setLeft(NINITnode);

        StNode opt_comma = opt_comma(tokenList, sTable);
        NILISTnode.setRight(opt_comma);

        return NILISTnode;

    }

    public static StNode opt_comma(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_comma = new StNode(); 

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);
            StNode initlist = initlist(tokenList, sTable); 
            opt_comma.setLeft(initlist);
        }
        //when opt_comma id is NUNDEF then its an empty case, if comma then other case
        return opt_comma;
        
    }

    


}
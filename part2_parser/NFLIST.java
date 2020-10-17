/*
NFLIST <fields> ::= <sdecl> , <fields>
Special <fields> ::= <sdecl>
    -left factoring-
        <fields> ::= <sdecl> opt_fields
        opt_fields ::= , <fields> | ε

NSDECL <sdecl> ::= <id> : <stype>
*/
import java.util.ArrayList; 
public class NFLIST{
    public static StNode fields(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFLISTnode = new StNode();
        NFLISTnode.setNodeID("NFLIST");
        if(tokenList.get(0).getTokenNo() == 58){

            StNode sdecl = sdecl(tokenList, sTable);
            NFLISTnode.setLeft(sdecl);

            StNode opt_fields = opt_fields(tokenList, sTable);
            NFLISTnode.setRight(opt_fields);

            
            
        }
        else{
            //error
        }

        return NFLISTnode;
    }

    public static StNode sdecl(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode sdecl = new StNode();
        sdecl.setNodeID("NSDECL");

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }

        TableEntry entry = new TableEntry(tokenList.get(0));
        sTable.setTableEntry(entry);
        sdecl.setSymbolTableReference(entry);

        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() == 46){
            tokenList.remove(0);
            
            StNode stype = NFUND.stype(tokenList, sTable);
            sdecl.setLeft(stype);
        }
        else{
            //error
        }

        return sdecl;

    }
    

    public static StNode opt_fields(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_fields = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode fields = fields(tokenList, sTable);
            opt_fields.setLeft(fields);
        }

        return opt_fields;
    }

}
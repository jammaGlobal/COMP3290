/*
NALIST <arrdecls> ::= <arrdecl> , <arrdecls>
Special <arrdecls> ::= <arrdecl>
    -left factoring-
        <arrdecls> ::= <arrdecl> opt_arrdecls
        opt_arrdecls ::= , <arrdecls> | ε

*/
import java.util.ArrayList; 
public class NALIST{
    public static StNode arrdecls(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NALISTnode = new StNode();
        NALISTnode.setNodeID("NALIST");
        
        StNode arrdecl = arrdecl(tokenList,sTable);
        NALISTnode.setLeft(arrdecl);

        StNode opt_arrdecls = opt_arrdecls(tokenList,sTable);
        NALISTnode.setLeft(opt_arrdecls);

        return NALISTnode;
    }

    public static StNode arrdecl(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode arrdecl = new StNode();
        arrdecl.setNodeID("NARRD");

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }

        TableEntry entry = new TableEntry(tokenList.get(0));
        sTable.setTableEntry(entry);
        arrdecl.setSymbolTableReference(entry);

        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() == 46){
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 58){
                //error
            }
            
            TableEntry ent = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(ent);
            arrdecl.setSymbolTableReference(ent);
            tokenList.remove(0);
        }
        else{
            //error
        }


        return arrdecl;
    }

    public static StNode opt_arrdecls(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_arrdecls = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);

            StNode arrdecls = arrdecls(tokenList,sTable);
            opt_arrdecls.setLeft(arrdecls);
        }

        return opt_arrdecls;
    }
}
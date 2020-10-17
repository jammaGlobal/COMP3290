/*
NPRLST <prlist> ::= <printitem> , <prlist>
Special <prlist> ::= <printitem>
    -left factoring-
        <prlist> ::= <printitem> opt_printlist
        opt_printlist ::= , <prlist> | e
*/
import java.util.ArrayList; 
public class NPRLST{
    public static StNode prlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NPRLSTnode = new StNode();
        NPRLSTnode.setNodeID("NPRLST");

        StNode printitem = printitem(tokenList, sTable);
        NPRLSTnode.setLeft(printitem);

        StNode opt_printlist = opt_printlist(tokenList, sTable);
        NPRLSTnode.setLeft(opt_printlist);

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

        }


        return printitem;
    }

    public static StNode opt_printlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_printlist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);
            StNode prlist = prlist(tokenList, sTable);
        }

        return opt_printlist;
    }
}
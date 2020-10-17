/*
NCALL <callstat> ::= <id> ( <elist> ) | <id> ( )
    Left factoring
    <callstat> ::= <id> ( opt_elist )
    opt_elist ::= <elist> | e
*/
import java.util.ArrayList; 
public class NCALL{

    public static StNode callstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NCALLnode = new StNode();
        NCALLnode.setNodeID("NCALL");

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }

        TableEntry entry = new TableEntry(tokenList.get(0));
        sTable.setTableEntry(entry);
        NCALLnode.setSymbolTableReference(entry);
            
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 35){
            //error
        }
        tokenList.remove(0);

        

        if(tokenList.get(0).getTokenNo() != 36){
            //error
        }
        tokenList.remove(0);
        
        return NCALLnode;
    }

    public static StNode opt_elist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_elist = new StNode();

        StNode elist = NEXPL.elist(tokenList, sTable);
        //if elist returns null then act as if it was empty case

        opt_elist.setMiddle(elist);

        return opt_elist;
    }
}
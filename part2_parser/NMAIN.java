/*
NMAIN <mainbody> ::= main <slist> begin <stats> end CD20 <id>
*/
import java.util.ArrayList; 
public class NMAIN {
    public static StNode mainbody(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NMAINnode = new StNode();
        NMAINnode.setNodeID("NMAIN");

        if(tokenList.get(0).getTokenNo() != 6){
            //error
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }
        
        StNode slist = NSDLST.slist(tokenList,sTable);
        NMAINnode.setLeft(slist);

        if(tokenList.get(0).getTokenNo() != 7){
            //error
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList,sTable);
        NMAINnode.setRight(stats);

        if(tokenList.get(0).getTokenNo() != 8){
            //error
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 1){
            //error
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 58){
            //error
        }

        /*
        TableEntry entry = new TableEntry(tokenList.get(0));
        sTable.setTableEntry(entry);
        NMAINnode.setSymbolTableReference(entry);
        */
        tokenList.remove(0);

        return NMAINnode;
    }
}
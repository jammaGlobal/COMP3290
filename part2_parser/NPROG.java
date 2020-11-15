//NPROG <program> ::= CD20 <id> <globals> <funcs> <mainbody>
import java.util.ArrayList; 
public class NPROG {
    //Pre-condition: The CD20 token must have been parsed
    public static StNode program(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NPROGnode = new StNode();
        TableEntry entry;
        
        if(tokenList.get(0).getTokenNo() == 58){
            entry = new TableEntry(tokenList.get(0));
        }
        else{
            String error = "Program missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return NPROGnode;
        }
        tokenList.remove(0);

        StNode NGLOBnode = NGLOB.globals(tokenList, sTable);
        if(NGLOBnode.isNUNDEF() && NGLOBnode.isNotEmptyContainsError()){
            return NPROGnode;
        }

        StNode NFUNCSnode = NFUNCS.funcs(tokenList, sTable);
        if(NFUNCSnode.isNUNDEF() && NGLOBnode.isNotEmptyContainsError()){
            return NFUNCSnode;
        }
        
        StNode NMAINnode = NMAIN.mainbody(tokenList, sTable);
        if(NMAINnode.isNUNDEF()){
            return NPROGnode;
        }

        sTable.setTableEntry(entry);
        NPROGnode.setSymbolTableReference(entry);

        NPROGnode.setNodeID("NPROG");
        NPROGnode.setLeft(NGLOBnode);
        NPROGnode.setMiddle(NFUNCSnode);
        NPROGnode.setRight(NMAINnode);
        
        return NPROGnode;
    } 

}
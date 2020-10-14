import java.util.ArrayList; 
public class NPROG {

    //Pre-condition: The CD20 token must have been parsed
    public static StNode program(ArrayList<Token> tokenList, SymbolTable sTable){

        StNode NPROGnode = new StNode();
        if(tokenList.get(0).getTokenNo() == 58){
            TableEntry entry = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(entry);
            NPROGnode.setSymbolTableReference(entry);
            NPROGnode.setNodeID("NPROG");
        }
        else{
            //generate syntax error
        }
        tokenList.remove(0);
        StNode NGLOBnode = NGLOB.globals(tokenList, sTable);
        //StNode NFUNCSnode = NFUNCS.funcs(tokenList, sTable);
        //StNode NMAINnode = NMAIN.mainbody(tokenList, sTable);

        NPROGnode.setLeft(NGLOBnode);
        //NPROGnode.setMiddle(NFUNCSnode);
        //NPROGnode.setRight(NMAINnode);
        
        return NPROGnode;
    } 

}
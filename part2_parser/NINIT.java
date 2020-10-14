//  NINIT <init> ::= <id> = <expr>
import java.util.ArrayList; 
public class NINIT{
    public static StNode init(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NINITnode = new StNode();

        if(tokenList.get(0).getTokenNo() == 58){
            TableEntry entry = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(entry);
            NINITnode.setSymbolTableReference(entry);
            NINITnode.setNodeID("NINIT");
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() == 37){
            tokenList.remove(0);
        }
        
        //for <expr> utilise parsing from NNOT class
        StNode expr = NNOT.rel(tokenList, sTable);
        NINITnode.setLeft(expr);

        
        return NINITnode;
    }
}
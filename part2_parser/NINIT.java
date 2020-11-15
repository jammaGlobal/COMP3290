//  NINIT <init> ::= <id> = <expr>
import java.util.ArrayList; 
public class NINIT{
    public static StNode init(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NINITnode = new StNode();

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Initial constant missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return NINITnode;
        }
        TableEntry entry = new TableEntry(tokenList.get(0));
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 37){
            String error = "Initial constant missing 'equals' operator";
            sTable.parseError(tokenList.get(0), error);
            return NINITnode;
        }
        tokenList.remove(0);
        
        //for <expr> utilise parsing from NNOT class
        StNode expr = NNOT.rel(tokenList, sTable);
        if(expr.isNotEmptyContainsError()){
            //NINITnode.notEmptyContainsError();
            return NINITnode;
        }
        
        sTable.setTableEntry(entry);
        NINITnode.setSymbolTableReference(entry);

        NINITnode.setNodeID("NINIT");
        NINITnode.setLeft(expr);
        return NINITnode;
    }
}
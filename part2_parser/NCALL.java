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
        

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Call statement missing initial identifier";
            sTable.parseError(tokenList.get(0), error);
            return NCALLnode;
        }

        TableEntry entry = new TableEntry(tokenList.get(0));
        
            
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 35){
            String error = "Call statement missing left parenthesis '('";
            sTable.parseError(tokenList.get(0), error);
            return NCALLnode;
        }
        tokenList.remove(0);

        StNode opt_elist = opt_elist(tokenList,sTable);


        if(tokenList.get(0).getTokenNo() != 36){
            String error = "Call statement missing right parenthesis ')'";
            sTable.parseError(tokenList.get(0), error);
            return NCALLnode;
        }
        tokenList.remove(0);
        
        if(opt_elist != null){
            opt_elist.setLeft(opt_elist);
        }
        

        sTable.setTableEntry(entry);
        NCALLnode.setSymbolTableReference(entry);

        NCALLnode.setNodeID("NCALL");
        
        return NCALLnode;
    }

    //Since the first token to be found through an elist non-terminal is too ambiguous
    //This optional function will never return an error if elist contains an error, in which
    //case null will be returned
    public static StNode opt_elist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_elist = new StNode();

        StNode elist = NEXPL.elist(tokenList, sTable);
        if(elist.isNUNDEF() && elist.isNotEmptyContainsError()){
            return null;
        }

        opt_elist.setLeft(elist);

        return opt_elist;
    }
}
//NFCALL    <fncall> ::= <id> ( <opt_elist> )
//          <opt_elist> ::= <elist> | e
import java.util.ArrayList; 
public class NFCALL{
    public static StNode fncall(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFCALLnode = new StNode();
        if(tokenList.get(0).getTokenNo() == 58){
            TableEntry entry = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(entry);
        }
        else{
            //return error, however the precondition for this function is that there is
        }

        tokenList.remove(0);
        
        if(tokenList.get(0).getTokenNo() == 35){
            tokenList.remove(0);
        }
        else{
            //error
        }

        StNode opt_elist = opt_elist(tokenList, sTable);
        NFCALLnode.setLeft(opt_elist);

        if(tokenList.get(0).getTokenNo() == 36){
            tokenList.remove(0);
        }
        else{
            //error, unclosed bracket
        }


        return NFCALLnode;
    }

    //In the cases of where the production is either a single non-terminal or an empty (e) 
    //if we decide to just continue to parse then we might hit an error where there is no terminal, that error might be invalid as an (e) option was available
    //earlier on
    public static StNode opt_elist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_elist = new StNode();

        StNode elist = NEXPL.elist(tokenList, sTable);
        opt_elist.setLeft(elist);

        return opt_elist;

    }
}
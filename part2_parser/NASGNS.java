/*
NASGNS <asgnlist> ::= <asgnstat> , <asgnlist>
Special <asgnlist> ::= <asgnstat>
    -left factoring-
        <asgnlist> ::= <asgnstat> opt_asgnlist
        opt_asgnlist ::= , <asgnlist> | e

Special <asgnstat> ::= <var> <asgnop> <bool>
NASGN <asgnop> ::= =
NPLEQ <asgnop> ::= +=
NMNEQ <asgnop> ::= -=
NSTEQ <asgnop> ::= *=
NDVEQ <asgnop> ::= /=
*/
import java.util.ArrayList; 
public class NASGNS{

    public static StNode asgnlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NASGNSnode = new StNode();

        StNode asgnstat = NSTATS.asgnstat(tokenList, sTable);
        if(asgnstat.isNUNDEF() && asgnstat.isNotEmptyContainsError()){
            if(errorRecovery(tokenList,sTable)){

            }
            else{
                NASGNSnode.notEmptyContainsError();
                return NASGNSnode;
            }
        }

        StNode opt_asgnlist = opt_asgnlist(tokenList, sTable);
        if(opt_asgnlist == null && !asgnstat.isNotEmptyContainsError()){  
            NASGNSnode.setLeft(asgnstat);
            return NASGNSnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_asgnlist == null && asgnstat.isNotEmptyContainsError()){
            return NASGNSnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_asgnlist != null && (opt_asgnlist.isNotEmptyContainsError() && opt_asgnlist.isNUNDEF())){
            NASGNSnode.setLeft(asgnstat);
            return NASGNSnode;
        }

        NASGNSnode.setLeft(asgnstat);
        NASGNSnode.setRight(opt_asgnlist);
        NASGNSnode.setNodeID("NASGNS");
        
        return NASGNSnode;
    }

    public static StNode opt_asgnlist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_asgnlist = new StNode();

        if(tokenList.get(0).getTokenNo() == 32){
            tokenList.remove(0);
            StNode asgnlist = asgnlist(tokenList, sTable);
            if(asgnlist.isNUNDEF() && asgnlist.isNotEmptyContainsError()){
                opt_asgnlist.notEmptyContainsError();
                return opt_asgnlist;
            }
            opt_asgnlist.setLeft(asgnlist);

            return opt_asgnlist;
        }

        return null;
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 32){
                tokPos = i;
                tokNo = tokenList.get(i).getTokenNo();
                break;
            }
        }

        if(tokPos == -1){
            //Unsuccessful error recovery
            return false;
        }
        else{
            //Found comma
            if(tokNo == 32){
                while(tokenList.get(0).getTokenNo() != 32){
                    tokenList.remove(0);
                }
            }
            return true;
        }
        
    }
}
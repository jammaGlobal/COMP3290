/*
NSTATS <stats> ::= <stat> ; <stats> | <strstat> <stats>
Special <stats> ::= <stat> ; | <strstat>
    -left factoring-
        <stats> ::= <stat> ; opt_stats | <strstat> opt_stats
        opt_stats ::= <stats> | e

Special <strstat> ::= <forstat> | <ifstat>
Special <stat> ::= <reptstat> | <asgnstat>
Special <stat> ::= <iostat> | <callstat> | <returnstat>
*/
import java.util.ArrayList; 
public class NSTATS{

    public static StNode stats(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NSTATSnode = new StNode();
        StNode strstat, stat;
        

        if(tokenList.get(0).getTokenNo() == 17 || tokenList.get(0).getTokenNo() == 20){
            strstat = strstat(tokenList,sTable);
            if(strstat.isNUNDEF() && strstat.isNotEmptyContainsError()){
                if(!strstatErrorRecovery(tokenList,sTable)){
                    NSTATSnode.notEmptyContainsError();
                    return NSTATSnode;
                }
            }

            StNode opt_stats = opt_stats(tokenList,sTable);

            if(opt_stats == null && !strstat.isNotEmptyContainsError()){  
                NSTATSnode.setLeft(strstat);
                return NSTATSnode;
            }
            //there has been an error in the next statement that is within the next arrdecls function
            else if(opt_stats == null && strstat.isNotEmptyContainsError()){
                return NSTATSnode;
            }
            
            else if(opt_stats != null && (opt_stats.isNotEmptyContainsError() && opt_stats.isNUNDEF())){
                NSTATSnode.setLeft(strstat);
                return NSTATSnode;
            }

            NSTATSnode.setLeft(strstat);
            NSTATSnode.setRight(opt_stats);
            NSTATSnode.setNodeID("NSTATS");
            return NSTATSnode;
        }

        //This accounts for a repeat, assignment, io, call or return statement
        stat = stat(tokenList,sTable);

        if(stat.isNUNDEF() && stat.isNotEmptyContainsError()){
            if(!statErrorRecovery(tokenList,sTable)){
                NSTATSnode.notEmptyContainsError();
                return NSTATSnode;
            }
        }

        if(tokenList.get(0).getTokenNo() != 56){
            String error = "Statement missing semicolon";
            sTable.parseError(tokenList.get(0), error);
            
            return NSTATSnode;
        }
        tokenList.remove(0);

        StNode opt_stats = opt_stats(tokenList,sTable);

        if(opt_stats == null && !stat.isNotEmptyContainsError()){  
            NSTATSnode.setLeft(stat);
            return NSTATSnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_stats == null && stat.isNotEmptyContainsError()){
            return NSTATSnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_stats != null && (opt_stats.isNotEmptyContainsError() && opt_stats.isNUNDEF())){
            NSTATSnode.setLeft(stat);
            return NSTATSnode;
        }

        NSTATSnode.setLeft(stat);
        NSTATSnode.setRight(opt_stats);
        NSTATSnode.setNodeID("NSTATS");
        return NSTATSnode;

    }

    public static StNode opt_stats(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_stats = new StNode();

        if(tokenList.get(0).getTokenNo() == 17 || tokenList.get(0).getTokenNo() == 20 ||
            tokenList.get(0).getTokenNo() == 18 || tokenList.get(0).getTokenNo() == 22 || 
            tokenList.get(0).getTokenNo() == 23 || tokenList.get(0).getTokenNo() == 24 ||
            tokenList.get(0).getTokenNo() == 25 || tokenList.get(0).getTokenNo() == 58)
        {
            StNode stats = stats(tokenList,sTable);
            if(stats.isNUNDEF() && stats.isNotEmptyContainsError()){
                opt_stats.notEmptyContainsError();
                return opt_stats;
            }

            opt_stats.setLeft(stats);

            return opt_stats;
        }
        else{
            return null;
        }

        
    }

    public static StNode strstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode strstat = new StNode();
            
        if(tokenList.get(0).getTokenNo() == 17){
            StNode forstat = NFOR.forstat(tokenList,sTable);
            if(forstat.isNUNDEF()){
                strstat.notEmptyContainsError();
                return strstat;
            }

            strstat.setLeft(forstat);
        }
        else if(tokenList.get(0).getTokenNo() == 20){
            StNode ifstat = NIFTH.ifstat(tokenList,sTable);
            if(ifstat.isNUNDEF()){
                strstat.notEmptyContainsError();
                return strstat;
            }

            strstat.setLeft(ifstat);
        }
        else{
            String error = "Statement missing starting 'for' or 'if' keyword";
            sTable.parseError(tokenList.get(0), error);
            strstat.notEmptyContainsError();
            return strstat;
        }

        return strstat;
    }

    // Special <stat> ::= <reptstat> | <asgnstat>
    // Special <stat> ::= <iostat> | <callstat> | <returnstat>
    public static StNode stat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode stat = new StNode();

        if(tokenList.get(0).getTokenNo() == 18){
            StNode reptstat = NREPT.reptstat(tokenList,sTable);
            if(reptstat.isNUNDEF()){
                stat.notEmptyContainsError();
                return stat;
            }
            stat.setLeft(reptstat);
        }
        else if(tokenList.get(0).getTokenNo() == 58 && tokenList.get(1).getTokenNo() != 35)
        {
            StNode asgnstat = asgnstat(tokenList,sTable);
            if(asgnstat.isNUNDEF() && asgnstat.isNotEmptyContainsError()){
                stat.notEmptyContainsError();
                return stat;
            }
            stat.setLeft(asgnstat);
        }
        else if(tokenList.get(0).getTokenNo() == 25){
            StNode returnstat = returnstat(tokenList,sTable);
            if(returnstat.isNUNDEF()){
                stat.notEmptyContainsError();
                return stat;
            }
            stat.setLeft(returnstat);
        }
        else if(tokenList.get(0).getTokenNo() == 22 || tokenList.get(0).getTokenNo() == 23 || tokenList.get(0).getTokenNo() == 24){
            StNode iostat = iostat(tokenList,sTable);
            if(iostat.isNUNDEF()){
                stat.notEmptyContainsError();
                return stat;
            }
            stat.setLeft(iostat);
        }
        else if(tokenList.get(0).getTokenNo() == 58 && tokenList.get(1).getTokenNo() == 35){
            StNode callstat = NCALL.callstat(tokenList,sTable);
            if(callstat.isNUNDEF()){
                stat.notEmptyContainsError();
                return stat;
            }
            stat.setLeft(callstat);
        }
        else{
            String error = "Statement missing starting token which excludes it from the remaining repeat, assignment, io, call, and return statement options";
            sTable.parseError(tokenList.get(0), error);
            stat.notEmptyContainsError();
            return stat;
        }


        return stat;
    }

    //Special <asgnstat> ::= <var> <asgnop> <bool>
    public static StNode asgnstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode asgnstat = new StNode();

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Assignment statement missing starting identifier";
            sTable.parseError(tokenList.get(0), error);
            asgnstat.notEmptyContainsError();
            return asgnstat;
        }

        StNode var = NPOW.var(tokenList, sTable);
        if(var.isNUNDEF() && var.isNotEmptyContainsError()){ //&& error
            asgnstat.notEmptyContainsError();
            return asgnstat;
        }

        if(tokenList.get(0).getTokenNo() == 37){
            StNode asgnop = new StNode();
            asgnop.setNodeID("NASGN");
            asgnstat.setMiddle(asgnop);
        }
        else if(tokenList.get(0).getTokenNo() == 51){
            StNode asgnop = new StNode();
            asgnop.setNodeID("NPLEQ");
            asgnstat.setMiddle(asgnop);
        }
        else if(tokenList.get(0).getTokenNo() == 52){
            StNode asgnop = new StNode();
            asgnop.setNodeID("NMNEQ");
            asgnstat.setMiddle(asgnop);
        }
        else if(tokenList.get(0).getTokenNo() == 53){
            StNode asgnop = new StNode();
            asgnop.setNodeID("NSTEQ");
            asgnstat.setMiddle(asgnop);
        }
        else if(tokenList.get(0).getTokenNo() == 54){
            StNode asgnop = new StNode();
            asgnop.setNodeID("NDVEQ");
            asgnstat.setMiddle(asgnop);
        }
        else{
            String error = "Assignment statement missing assignment operator";
            sTable.parseError(tokenList.get(0), error);
            asgnstat.notEmptyContainsError();
            return asgnstat;
        }

        tokenList.remove(0);

        StNode bool = NBOOL.bool(tokenList,sTable);
        if(bool.isNUNDEF() && bool.isNotEmptyContainsError()){
            asgnstat.notEmptyContainsError();
            return asgnstat;
        }

        asgnstat.setLeft(var);
        asgnstat.setRight(bool);
        
        return asgnstat;
    }

    public static StNode returnstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode returnstat = new StNode();
        

        if(tokenList.get(0).getTokenNo() == 25){
            tokenList.remove(0);
        }
        else{
            String error = "Return statement missing 'return' keyword";
            sTable.parseError(tokenList.get(0), error);
            return returnstat;
        }

        //expr function has too many possible beginning terminals so we need to return nulls to determine
        //if they are

        if(tokenList.get(0).getTokenNo() == 56){

        }
        else{
            StNode expr = NNOT.expr(tokenList, sTable);
            if(expr.isNUNDEF() && expr.isNotEmptyContainsError()){

                return returnstat;
            }
            returnstat.setLeft(expr);
        }

        returnstat.setNodeID("NRETN");

        return returnstat;
    }

    public static StNode iostat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode iostat = new StNode();

        if(tokenList.get(0).getTokenNo() == 22){
            
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 58){
                String error = "IO statement missing identifier";
                sTable.parseError(tokenList.get(0), error);
                return iostat;
            }

            StNode vlist = NVLIST.vlist(tokenList, sTable);
            if(vlist.isNUNDEF() && vlist.isNotEmptyContainsError()){
                return iostat;
            }

            iostat.setLeft(vlist);
            iostat.setNodeID("NINPUT");
        }
        else if(tokenList.get(0).getTokenNo() == 23){
            
            tokenList.remove(0);

            StNode prlist = NPRLST.prlist(tokenList, sTable);
            if(prlist.isNUNDEF() && prlist.isNotEmptyContainsError()){
                return iostat;
            }
            iostat.setLeft(prlist);
            iostat.setNodeID("NPRINT");
        }
        else if(tokenList.get(0).getTokenNo() == 24){
            
            tokenList.remove(0);

            StNode prlist = NPRLST.prlist(tokenList, sTable);
            if(prlist.isNUNDEF() && prlist.isNotEmptyContainsError()){
                return iostat;
            }
            iostat.setLeft(prlist);
            iostat.setNodeID("NPRLN");
        }
        else{
            String error = "IO statement missing initial 'input', 'print', or 'println' keyword";
            sTable.parseError(tokenList.get(0), error);
            return iostat;
        }
        
        return iostat;
    }

    public static boolean statErrorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        //We need to find next semicolon and also repeat, an identifier, input, print, println, or return


        //The current form doesn't assure the finding of the "couple" of a semicolon and specific keyword/ident
        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 56){
                if(checkIfStatTokenNext(tokenList, i)){
                    tokPos = i;
                }
                break;
            }
        }
        
        if(tokPos == -1){
            //Unsuccessful error recovery
            return false;
        }
        else{

            while(tokenList.get(0).getTokenNo() != 56){
                tokenList.remove(0);
            }
            return true;
        }
        //check next comma 
        //check next function
        //check main
        
    }

    public static boolean checkIfStatTokenNext(ArrayList<Token> tokenList, int tokPos){
        int[] tokenNumbers = {17,18,20,22,23,24,25,58};

        for(int j = 0; j < tokenNumbers.length ; j++){
            if(tokenList.get(tokPos+1).getTokenNo() == tokenNumbers[j]){
                return true;
            }
        }
        return false;
    }

    public static boolean strstatErrorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){

        //We don't need to find a semicolon but we do need to find for, if, repeat, an identifier, input, 
        //print, println, or return

        int tokPos = -1;
        int tokNo = 0;
        int[] tokenNumbers = {17,18,20,22,23,24,25,58};

        tokenTraverse:
        for(int i = 0; i < tokenList.size() ; i++){
            for(int j = 0; j < tokenNumbers.length ; j++){
                if(tokenList.get(i).getTokenNo() == tokenNumbers[j]){
                    tokPos = i;
                    tokNo = tokenList.get(i).getTokenNo();
                    break tokenTraverse;
                }
            }
        }

        if(tokPos == -1){
            //Unsuccessful error recovery
            return false;
        }
        else{
            for(int k = 0; k < tokenNumbers.length ; k++){
                if(tokNo == tokenNumbers[k]){
                    while(tokenList.get(0).getTokenNo() != tokenNumbers[k]){
                        tokenList.remove(0);
                    }
                }
            }
            return true;
        }
        
    }
    

}
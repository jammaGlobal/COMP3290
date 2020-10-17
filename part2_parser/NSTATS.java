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
        NSTATSnode.setNodeID("NSTATS");

        if(tokenList.get(0).getTokenNo() == 17 || tokenList.get(0).getTokenNo() == 20){
            StNode strstat = strstat(tokenList,sTable);
            NSTATSnode.setLeft(strstat);

            StNode opt_stats = opt_stats(tokenList,sTable);
            NSTATSnode.setRight(opt_stats);
        }
        else{
            StNode stat = stat(tokenList,sTable);
            NSTATSnode.setLeft(stat);

            if(tokenList.get(0).getTokenNo() != 56){
                //error
            }
            tokenList.remove(0);

            StNode opt_stats = opt_stats(tokenList,sTable);
            NSTATSnode.setRight(opt_stats);


        }
        

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
            opt_stats.setLeft(stats);
        }

        return opt_stats;
    }

    public static StNode strstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode strstat = new StNode();

        if(tokenList.get(0).getTokenNo() == 17){
            StNode forstat = NFOR.forstat(tokenList,sTable);
            strstat.setLeft(forstat);
        }
        else if(tokenList.get(0).getTokenNo() == 20){
            StNode ifstat = NIFTH.ifstat(tokenList,sTable);
            strstat.setLeft(ifstat);
        }
        else{
            //error
        }

        return strstat;
    }

    // Special <stat> ::= <reptstat> | <asgnstat>
    // Special <stat> ::= <iostat> | <callstat> | <returnstat>
    public static StNode stat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode stat = new StNode();

        if(tokenList.get(0).getTokenNo() == 18){
            StNode reptstat = NREPT.reptstat(tokenList,sTable);
            stat.setLeft(reptstat);
        }
        else if(tokenList.get(0).getTokenNo() == 58 && tokenList.get(1).getTokenNo() != 35)
        {
            StNode asgnstat = asgnstat(tokenList,sTable);
            stat.setLeft(asgnstat);
        }
        else if(tokenList.get(0).getTokenNo() == 25){
            StNode returnstat = returnstat(tokenList,sTable);
            stat.setLeft(returnstat);
        }
        else if(tokenList.get(0).getTokenNo() == 22 || tokenList.get(0).getTokenNo() == 23 || tokenList.get(0).getTokenNo() == 24){
            StNode iostat = iostat(tokenList,sTable);
            stat.setLeft(iostat);
        }
        else if(tokenList.get(0).getTokenNo() == 58 && tokenList.get(1).getTokenNo() == 35){
            StNode callstat = NCALL.callstat(tokenList,sTable);
            stat.setLeft(callstat);
        }
        else{
            //error
        }


        return stat;
    }

    //Special <asgnstat> ::= <var> <asgnop> <bool>
    public static StNode asgnstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode asgnstat = new StNode();

        if(tokenList.get(0).getTokenNo() == 58){
            StNode var = NPOW.var(tokenList, sTable);
            asgnstat.setLeft(var);
        }
        else{
            //error
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
            //error
        }

        tokenList.remove(0);

        StNode bool = NBOOL.bool(tokenList,sTable);
        asgnstat.setLeft(bool);
        
        return asgnstat;
    }

    public static StNode returnstat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode returnstat = new StNode();
        returnstat.setNodeID("NRETN");

        if(tokenList.get(0).getTokenNo() == 25){
            tokenList.remove(0);
        }
        else{
            //error
        }

        if(tokenList.get(0).getTokenNo() == 25){
            //don't know how to look ahead cleanly yet
        }
        

        return returnstat;
    }

    public static StNode iostat(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode iostat = new StNode();

        if(tokenList.get(0).getTokenNo() == 22){
            iostat.setNodeID("NINPUT");
            tokenList.remove(0);
            if(tokenList.get(0).getTokenNo() != 58){
                //error
            }

            StNode vlist = NVLIST.vlist(tokenList, sTable);
            iostat.setLeft(vlist);
        }
        else if(tokenList.get(0).getTokenNo() == 23){
            iostat.setNodeID("NPRINT");
            tokenList.remove(0);

            StNode prlist = NPRLST.prlist(tokenList, sTable);
            iostat.setLeft(prlist);
        }
        else if(tokenList.get(0).getTokenNo() == 24){
            iostat.setNodeID("NPRLN");
            tokenList.remove(0);

            StNode prlist = NPRLST.prlist(tokenList, sTable);
            iostat.setLeft(prlist);


        }
        else{
            //return error
        }
        


        
        return iostat;
    }

}
/*
    <fact> ::= <exponent> rec_fact
    rec_fact ::= ^ <exponent> rec_fact | e
*/
import java.util.ArrayList; 
public class NPOW{
    public static StNode fact(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NPOWnode = new StNode();
        NPOWnode.setNodeID("NPOW");

        StNode exponent = exponent(tokenList, sTable);
        NPOWnode.setLeft(exponent);

        StNode rec_fact = rec_fact(tokenList, sTable);
        NPOWnode.setRight(rec_fact);

        return NPOWnode;
    }

    public static StNode exponent(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode exponent = new StNode();

        if(tokenList.get(0).getTokenNo() == 59){
            tokenList.remove(0);
            exponent.setNodeID("NILIT");
        }
        else if(tokenList.get(0).getTokenNo() == 60){
            tokenList.remove(0);
            exponent.setNodeID("NFLIT");
        }
        else if(tokenList.get(0).getTokenNo() == 30){
            tokenList.remove(0);
            exponent.setNodeID("NTRUE");
        }
        else if(tokenList.get(0).getTokenNo() == 31){
            tokenList.remove(0);
            exponent.setNodeID("NFALS");
        }
        else if(tokenList.get(0).getTokenNo() == 35){
            tokenList.remove(0);

            StNode bool = NBOOL.bool(tokenList, sTable);
            exponent.setLeft(bool);

            if(tokenList.get(0).getTokenNo() == 36){
                tokenList.remove(0);
            }
            else{
                //error, unclosed bracket
            }
        }
        else if(tokenList.get(0).getTokenNo() == 58){

                if(tokenList.get(1).getTokenNo() == 35){
                    StNode fncall = NFCALL.fncall(tokenList, sTable);
                    exponent.setLeft(fncall);
                }
                else{
                    StNode var = var(tokenList, sTable);
                    exponent.setLeft(var);
                }

        }
        else{
            //error
        }

        return exponent;
    }

    //      <var> ::= <id> opt_lbrkt
    //      opt_lbrkt ::= [<expr>] . <id> | e 

    public static StNode var(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode var = new StNode();
        if(tokenList.get(0).getTokenNo() == 58){
            TableEntry entry = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(entry);
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() == 33){
            var.setNodeID("NARRV");
        }
        else{
            var.setNodeID("NSIMV");
        }

        StNode opt_lbrkt = opt_lbrkt(tokenList, sTable);
        var.setLeft(opt_lbrkt);

        return var;
    } 

    public static StNode opt_lbrkt(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_lbrkt = new StNode();

        if(tokenList.get(0).getTokenNo() == 33){
            tokenList.remove(0);

            StNode expr = NNOT.expr(tokenList, sTable);
            opt_lbrkt.setLeft(expr);

            if(tokenList.get(0).getTokenNo() == 57){
                tokenList.remove(0);

                if(tokenList.get(0).getTokenNo() == 58){
                    TableEntry entry = new TableEntry(tokenList.get(0));
                    sTable.setTableEntry(entry);

                    return opt_lbrkt;
                }
                else{
                    return null;
                    //error
                }
            }
            else{
                return null;
                //error
            }
        }
        else{
            return opt_lbrkt;
        }
    }

    public static StNode rec_fact(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_fact = new StNode();

        if(tokenList.get(0).getTokenNo() == 43){
            tokenList.remove(0);
            
            StNode exponent = exponent(tokenList, sTable);
            rec_fact.setLeft(exponent);

            StNode rec_fact_ = rec_fact(tokenList, sTable);
            rec_fact.setRight(rec_fact_);

            return rec_fact;
        }
        else{
            return null;
        }


    }
}
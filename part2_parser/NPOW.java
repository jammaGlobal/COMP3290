/*
    <fact> ::= <exponent> rec_fact
    rec_fact ::= ^ <exponent> rec_fact | e
*/
import java.util.ArrayList;

import javax.xml.namespace.QName; 
public class NPOW{
    public static StNode fact(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NPOWnode = new StNode();
        

        StNode exponent = exponent(tokenList, sTable);
        if(exponent.isNUNDEF() && exponent.isNotEmptyContainsError()){
            NPOWnode.notEmptyContainsError();
            return NPOWnode;
        }
        
        StNode rec_fact = rec_fact(tokenList, sTable);
        if(rec_fact == null){  
            NPOWnode.setLeft(exponent);
            return NPOWnode;
        }
        else if(rec_fact.isNUNDEF() && rec_fact.isNotEmptyContainsError()){
            NPOWnode.notEmptyContainsError();
            return NPOWnode;
        }
        
        NPOWnode.setNodeID("NPOW");
        NPOWnode.setLeft(exponent);
        NPOWnode.setRight(rec_fact);

        return NPOWnode;
    }

    /*
    rec_fact ::= ^ <exponent> rec_fact | e
    */
    public static StNode rec_fact(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_fact = new StNode();

        if(tokenList.get(0).getTokenNo() == 43){
            tokenList.remove(0);
            
            StNode exponent = exponent(tokenList, sTable);
            if(exponent.isNUNDEF() && exponent.isNotEmptyContainsError()){
                rec_fact.notEmptyContainsError();
                return rec_fact;
            }
            

            StNode rec_fact_ = rec_fact(tokenList, sTable);
            if(rec_fact_ == null && !exponent.isNUNDEF()){  
                rec_fact.setLeft(exponent);
                return rec_fact;
            }
            //there has been an error in the next arrdecl that is within the next arrdecls function
            else if(rec_fact_ == null && exponent.isNUNDEF()){
                return rec_fact;
            }
            // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
            // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
            else if(rec_fact_ != null && (rec_fact_.isNotEmptyContainsError() && rec_fact_.isNUNDEF())){
                rec_fact.setLeft(exponent);
                return rec_fact;
            }

            if(rec_fact_.isNUNDEF() && rec_fact_.isNotEmptyContainsError()){
                rec_fact.notEmptyContainsError();
                return rec_fact;
            }

            rec_fact.setNodeID("NPOW");
            rec_fact.setLeft(exponent);
            rec_fact.setRight(rec_fact_);

            return rec_fact;
        }

        return null;

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
            if(bool.isNUNDEF() && bool.isNotEmptyContainsError()){
                exponent.notEmptyContainsError();
                return exponent;
            }
            exponent.setLeft(bool);

            if(tokenList.get(0).getTokenNo() == 36){
                tokenList.remove(0);
            }
            else{
                String error = "(<bool>) exponent missing right parenthesis";
                sTable.parseError(tokenList.get(0), error);
                exponent.notEmptyContainsError();
                return exponent;
            }
        }
        else if(tokenList.get(0).getTokenNo() == 58){

                if(tokenList.get(1).getTokenNo() == 35){
                    StNode fncall = NFCALL.fncall(tokenList, sTable);
                    if(fncall.isNUNDEF()){
                        exponent.notEmptyContainsError();
                        return exponent;
                    }
                    exponent.setLeft(fncall);
                }
                else{
                    StNode var = var(tokenList, sTable);
                    if(var.isNUNDEF()){
                        exponent.notEmptyContainsError();
                        return exponent;
                    }
                    exponent.setLeft(var);
                }

        }
        else{
            String error = "Exponent not found";
            sTable.parseError(tokenList.get(0), error);
            exponent.notEmptyContainsError();
            return exponent;
        }

        return exponent;
    }

    //      <var> ::= <id> opt_lbrkt
    //      opt_lbrkt ::= [<expr>] . <id> | e 

    public static StNode var(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode var = new StNode();

        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Variable missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return var;
            //need to make symbol table ref
        }
        TableEntry entry = new TableEntry(tokenList.get(0));
        tokenList.remove(0);

        
        

        

        StNode opt_lbrkt = opt_lbrkt(tokenList,sTable);
        if(opt_lbrkt != null && opt_lbrkt.isNUNDEF() && opt_lbrkt.isNotEmptyContainsError()){
            return var;
        }
        else if(opt_lbrkt != null && !opt_lbrkt.isNotEmptyContainsError()){

            TableEntry entry_ = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(entry);
            var.setSymbolTableReference(entry);
            tokenList.remove(0);

            var.setNodeID("NARRV");
        }
        else if(opt_lbrkt == null){
            var.setNodeID("NSIMV");
        }

        sTable.setTableEntry(entry);
        var.setSymbolTableReference(entry);

        return var;
    } 

    //      opt_lbrkt ::= [<expr>] . <id> | e 

    public static StNode opt_lbrkt(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_lbrkt = new StNode();

        if(tokenList.get(0).getTokenNo() == 33){
            tokenList.remove(0);

            StNode expr = NNOT.expr(tokenList, sTable);
            if(expr.isNUNDEF() && expr.isNotEmptyContainsError()){
                opt_lbrkt.notEmptyContainsError();
                return opt_lbrkt;
            }
            opt_lbrkt.setLeft(expr);

            if(tokenList.get(0).getTokenNo() != 34){
                String error = "Variable missing right bracket operator ']'";
                sTable.parseError(tokenList.get(0), error);
                opt_lbrkt.notEmptyContainsError();
                return opt_lbrkt;
            }
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 57){
                String error = "Variable missing dot operator '.''";
                sTable.parseError(tokenList.get(0), error);
                opt_lbrkt.notEmptyContainsError();
                return opt_lbrkt;
            }
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 58){
                String error = "Variable missing second identifier";
                sTable.parseError(tokenList.get(0), error);
                opt_lbrkt.notEmptyContainsError();
                return opt_lbrkt;
            }

            return opt_lbrkt;
        }
        return null;

    }

}
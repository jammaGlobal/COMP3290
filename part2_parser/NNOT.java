/** 
NNOT <rel> ::= not <expr> <relop> <expr>
-left factoring-
<rel> ::= <expr> opt_relop
opt_relop ::= <relop> <expr> | e



NNOT <rel> ::= not <rel_> | <rel_>
<rel_> ::= <expr> opt_relop
opt_relop ::= <relop> <expr> | e


**/
import java.util.ArrayList; 
public class NNOT{
    public static StNode rel(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NNOTnode = new StNode();
        

        if(tokenList.get(0).getTokenNo() == 26){
            tokenList.remove(0);
        }

        StNode expr = expr(tokenList, sTable);
        if(expr.isNUNDEF() && expr.isNotEmptyContainsError()){
            NNOTnode.notEmptyContainsError();
            return NNOTnode;
        }
        

        StNode opt_relop = opt_relop(tokenList, sTable);
        if(opt_relop == null && !expr.isNotEmptyContainsError()){  
            NNOTnode.setLeft(expr);
            return NNOTnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_relop == null && expr.isNotEmptyContainsError()){
            return NNOTnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(opt_relop != null && (opt_relop.isNotEmptyContainsError() && opt_relop.isNUNDEF())){
            NNOTnode.setLeft(expr);
            return NNOTnode;
        }

        NNOTnode.setLeft(expr);
        NNOTnode.setRight(opt_relop);

        NNOTnode.setNodeID("NNOT");

        return NNOTnode;
    }

    //
    //  <expr> ::= <term> rec_expr
    //  rec_expr ::= + <term> rec_expr | - <term> rec_expr | e
    //
    public static StNode expr(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode expr = new StNode();
        String nodeID = "";

        StNode term = term(tokenList, sTable);
        if(term.isNUNDEF() && term.isNotEmptyContainsError()){
            expr.notEmptyContainsError();
            return expr;
        }

        

        if(tokenList.get(0).getTokenNo() == 38){
            nodeID = "NADD";
        }
        else if(tokenList.get(0).getTokenNo() == 39){
            nodeID = "NSUB";
        }

        StNode rec_expr = rec_expr(tokenList,sTable);
        if(rec_expr == null){  
            expr.setLeft(term);
            return expr;
        }else if(rec_expr.isNotEmptyContainsError()){
            expr.notEmptyContainsError();
            return expr;
        }

        if(!nodeID.equals("")){
            expr.setNodeID(nodeID);
        }
        

        expr.setLeft(term);
        expr.setRight(rec_expr);

        
        return expr;
    }

    //has to return a node id for expr; either NADD or NSUB || currently rec_expr gets the node id
    public static StNode rec_expr(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_expr = new StNode();
        String nodeID = "";

        if(tokenList.get(0).getTokenNo() == 38 || tokenList.get(0).getTokenNo() == 39){

            // if(tokenList.get(0).getTokenNo() == 38){
            //     nodeID = "NADD";
            // }
            // else if(tokenList.get(0).getTokenNo() == 39){
            //     nodeID = "NSUB";
            // }
            tokenList.remove(0);
            
            StNode term = term(tokenList, sTable);
            if(term.isNUNDEF() && term.isNotEmptyContainsError()){
                rec_expr.notEmptyContainsError();
                return rec_expr;
            }


            if(!nodeID.equals("")){
                rec_expr.setNodeID(nodeID);
            }

            StNode rec_expr_ = rec_expr(tokenList, sTable);

            if(rec_expr_ == null){  
                rec_expr.setLeft(term);
                return rec_expr;
            }else if(rec_expr_.isNotEmptyContainsError()){
                rec_expr.notEmptyContainsError();
                return rec_expr;
            }
            if(!nodeID.equals("")){
                rec_expr.setNodeID(nodeID);
            }

            rec_expr.setLeft(term);
            rec_expr.setRight(rec_expr_);

            return rec_expr;
        }
         
        return null;
    }
    //
    //  <term> ::= <fact> rec_term
    //  rec_term ::= * <fact> rec_term | / <fact> rec_term | % <fact> rec_term | e
    //
    public static StNode term(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode term = new StNode();
        String nodeID = "";

        StNode fact = NPOW.fact(tokenList, sTable);
        if(fact.isNotEmptyContainsError()){
            term.notEmptyContainsError();
        }

        

        if(tokenList.get(0).getTokenNo() == 40){
            nodeID = "NMUL";
        }
        else if(tokenList.get(0).getTokenNo() == 41){
            nodeID = "NDIV";
        }
        else if(tokenList.get(0).getTokenNo() == 42){
            nodeID = "NMOD";
            
        }

        StNode rec_term = rec_term(tokenList, sTable);
        if(rec_term == null){  
            term.setLeft(fact);
            return term;
        }else if(rec_term.isNotEmptyContainsError()){
            term.notEmptyContainsError();
            return term;
        }

        if(!nodeID.equals("")){
            term.setNodeID(nodeID);
        }

        term.setLeft(fact);
        term.setRight(rec_term);

        return term;
    }

    public static StNode rec_term(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_term = new StNode();
        String nodeID = "";

        if(tokenList.get(0).getTokenNo() == 40 || tokenList.get(0).getTokenNo() == 41 || tokenList.get(0).getTokenNo() == 42){
            tokenList.remove(0);

            StNode fact = NPOW.fact(tokenList, sTable);
            if(fact.isNotEmptyContainsError()){
                rec_term.notEmptyContainsError();
            }

            if(tokenList.get(0).getTokenNo() == 40){
                nodeID = "NMUL";
            }
            else if(tokenList.get(0).getTokenNo() == 41){
                nodeID = "NDIV";
            }
            else if(tokenList.get(0).getTokenNo() == 42){
                nodeID = "NMOD";
            }
            


            StNode rec_term_ = rec_term(tokenList, sTable);
            if(rec_term_ == null){  
                rec_term.setLeft(fact);
                return rec_term;
            }else if(rec_term_.isNotEmptyContainsError()){
                rec_term.notEmptyContainsError();
                return rec_term;
            }
    
            if(!nodeID.equals("")){
                rec_term.setNodeID(nodeID);
            }
    
            rec_term.setLeft(fact);
            rec_term.setRight(rec_term_);
        }

        return null;
    }

    //
    //
    //<rel_> ::= <expr> opt_relop
    //opt_relop ::= <relop> <expr> | e

    public static StNode opt_relop(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_relop = new StNode();

        StNode relop = relop(tokenList, sTable);
        if(relop.isNUNDEF()){
            return null;
        }
        

        StNode expr = expr(tokenList, sTable);
        if(expr.isNotEmptyContainsError()){
            opt_relop.notEmptyContainsError();
            return opt_relop;
        }

        opt_relop.setLeft(relop);
        opt_relop.setRight(expr);

        return opt_relop;
    }

    public static StNode relop(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode relop = new StNode();
        
        if(tokenList.get(0).getTokenNo() == 50){
            relop.setNodeID("NEQL");
            tokenList.remove(0);
            return relop;
        }
        else if(tokenList.get(0).getTokenNo() == 49){
            relop.setNodeID("NNEQ");
            tokenList.remove(0);
            return relop;
        }
        else if(tokenList.get(0).getTokenNo() == 45){
            relop.setNodeID("NGRT");
            tokenList.remove(0);
            return relop;
        }
        else if(tokenList.get(0).getTokenNo() == 48){
            relop.setNodeID("NGEQ");
            tokenList.remove(0);
            return relop;
        }
        else if(tokenList.get(0).getTokenNo() == 47){
            relop.setNodeID("NLEQ");
            tokenList.remove(0);
            return relop;
        }
        else if(tokenList.get(0).getTokenNo() == 44){
            relop.setNodeID("NLSS");
            tokenList.remove(0);
            return relop;
        }
        else{
            return relop;
        }
    }
}
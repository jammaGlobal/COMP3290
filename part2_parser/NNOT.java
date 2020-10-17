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
        NNOTnode.setNodeID("NNOT");

        if(tokenList.get(0).getTokenNo() == 26){
            tokenList.remove(0);
        }

        StNode expr = expr(tokenList, sTable);
        NNOTnode.setLeft(expr);

        StNode opt_relop = opt_relop(tokenList, sTable);
        NNOTnode.setRight(opt_relop);

        return NNOTnode;
    }

    //
    //  <expr> ::= <term> rec_expr
    //  rec_expr ::= + <term> rec_expr | - <term> rec_expr | e
    //
    public static StNode expr(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode expr = new StNode();

        StNode term = term(tokenList, sTable);
        expr.setLeft(term);

        if(tokenList.get(0).getTokenNo() == 38){
            expr.setNodeID("NADD");
        }
        else if(tokenList.get(0).getTokenNo() == 39){
            expr.setNodeID("NSUB");
        }

        StNode rec_expr = rec_expr(tokenList,sTable);
        expr.setRight(rec_expr);
        
        return expr;
    }

    //has to return a node id for expr; either NADD or NSUB || currently rec_expr gets the node id
    public static StNode rec_expr(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_expr = new StNode();

        if(tokenList.get(0).getTokenNo() == 38 || tokenList.get(0).getTokenNo() == 39){
            tokenList.remove(0);
            
            StNode term = term(tokenList, sTable);
            rec_expr.setLeft(term);

            StNode rec_expr_ = rec_expr(tokenList, sTable);
            rec_expr.setRight(rec_expr_);
        }

        
        
        return rec_expr;
    }
    //
    //  <term> ::= <fact> rec_term
    //  rec_term ::= * <fact> rec_term | / <fact> rec_term | % <fact> rec_term | e
    //
    public static StNode term(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode term = new StNode();

        StNode fact = NPOW.fact(tokenList, sTable);
        term.setLeft(fact);

        if(tokenList.get(0).getTokenNo() == 40){
            term.setNodeID("NMUL");
        }
        else if(tokenList.get(0).getTokenNo() == 41){
            term.setNodeID("NDIV");
        }
        else if(tokenList.get(0).getTokenNo() == 42){
            term.setNodeID("NMOD");
        }

        StNode rec_term = rec_term(tokenList, sTable);
        term.setRight(rec_term);

        return term;
    }

    public static StNode rec_term(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_term = new StNode();

        if(tokenList.get(0).getTokenNo() == 40 || tokenList.get(0).getTokenNo() == 41 || tokenList.get(0).getTokenNo() == 42){
            tokenList.remove(0);

            StNode fact = NPOW.fact(tokenList, sTable);
            rec_term.setLeft(fact);

            StNode rec_term_ = rec_term(tokenList, sTable);
            rec_term.setRight(rec_term_);
        }

        return rec_term;
    }

    //
    //
    //<rel_> ::= <expr> opt_relop
    //opt_relop ::= <relop> <expr> | e

    public static StNode opt_relop(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_relop = new StNode();

        StNode relop = relop(tokenList, sTable);
        opt_relop.setLeft(relop);

        StNode expr = expr(tokenList, sTable);
        expr.setRight(expr);

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
            return null;
        }
    }
}
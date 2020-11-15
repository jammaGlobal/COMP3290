//        <bool> ::= <rel> rec_bool
//        rec_bool ::= <logop> <rel> rec_bool | e 

import java.util.ArrayList; 
public class NBOOL{
    public static StNode bool(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NBOOLnode = new StNode();
        

        StNode NNOTnode = NNOT.rel(tokenList,sTable);
        if(NNOTnode.isNUNDEF() && NNOTnode.isNotEmptyContainsError()){
            NBOOLnode.notEmptyContainsError();
            return NBOOLnode;
        }

        StNode rec_boolNode = rec_bool(tokenList,sTable);
        if(rec_boolNode == null && !NNOTnode.isNotEmptyContainsError()){  
            NBOOLnode.setLeft(NNOTnode);
            return NBOOLnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(rec_boolNode == null && NNOTnode.isNotEmptyContainsError()){
            return NBOOLnode;
        }
        // not null condition to differentiate from an empty or a node that hasnt been arrived at due to arrdecl
        // error recovery, node ID is not set as the error has occurred just after a successful comma find; within next arrr
        else if(rec_boolNode != null && (rec_boolNode.isNotEmptyContainsError() && rec_boolNode.isNUNDEF())){
            NBOOLnode.setLeft(NNOTnode);
            return NBOOLnode;
        }

        NBOOLnode.setNodeID("NBOOL");
        NBOOLnode.setLeft(NNOTnode);
        NBOOLnode.setRight(rec_boolNode);

        return NBOOLnode;
    }

    ////        rec_bool ::= <logop> <rel> rec_bool | e 
    public static StNode rec_bool(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_boolNode = new StNode();

        //check logop
        if(tokenList.get(0).getTokenNo() == 27 || tokenList.get(0).getTokenNo() == 28 || tokenList.get(0).getTokenNo() == 29){

            StNode logop = logop(tokenList,sTable);
            if(logop.isNUNDEF()){
                rec_boolNode.notEmptyContainsError();
                return rec_boolNode;
            }

            StNode NNOTnode = NNOT.rel(tokenList,sTable);
            if(NNOTnode.isNUNDEF() && NNOTnode.isNotEmptyContainsError()){
                rec_boolNode.notEmptyContainsError();
                return rec_boolNode;
            }
            

            StNode rec_boolNode_ = rec_bool(tokenList,sTable);
            if(rec_boolNode_.isNUNDEF() && NNOTnode.isNotEmptyContainsError()){
                rec_boolNode.notEmptyContainsError();
                return rec_boolNode;
            }

            rec_boolNode.setLeft(logop);
            rec_boolNode.setMiddle(NNOTnode);
            rec_boolNode.setRight(rec_boolNode_);
            rec_boolNode.setNodeID("NBOOL");


            return rec_boolNode;
        }

        return null;

        

    }

    public static StNode logop(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode logop = new StNode();

        if(tokenList.get(0).getTokenNo() == 27){
            tokenList.remove(0);
            logop.setNodeID("NAND");
            return logop;
        }
        else if(tokenList.get(0).getTokenNo() == 28){
            tokenList.remove(0);
            logop.setNodeID("NOR");
            return logop;
        }
        else if(tokenList.get(0).getTokenNo() == 29){
            tokenList.remove(0);
            logop.setNodeID("NXOR");
            return logop;
        }
        else{
            return logop;
        }
    }

}
//        <bool> ::= <rel> rec_bool
//        rec_bool ::= <logop> <rel> rec_bool | e 

import java.util.ArrayList; 
public class NBOOL{
    public static StNode bool(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NBOOLnode = new StNode();
        NBOOLnode.setNodeID("NBOOL");

        StNode NNOTnode = NNOT.rel(tokenList,sTable);

        StNode rec_boolNode = rec_bool(tokenList,sTable);

        NBOOLnode.setLeft(NNOTnode);
        NBOOLnode.setRight(rec_boolNode);

        return NBOOLnode;
    }

    ////        rec_bool ::= <logop> <rel> rec_bool | e 
    public static StNode rec_bool(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rec_boolNode = new StNode();

        
        //check logop
        if(tokenList.get(0).getTokenNo() == 27 || tokenList.get(0).getTokenNo() == 28 || tokenList.get(0).getTokenNo() == 29){
            StNode logop  = new StNode();
            rec_boolNode.setLeft(logop);

            StNode logop_node = logop(tokenList,sTable);
            logop.setLeft(logop_node);

            StNode NNOTnode = NNOT.rel(tokenList,sTable);
            rec_boolNode.setMiddle(NNOTnode);

            StNode rec_boolNode_ = rec_bool(tokenList,sTable);
            rec_boolNode.setLeft(rec_boolNode_);
        }
        else{
            rec_boolNode = null;
        }

        return rec_boolNode;

    }

    public static StNode logop(ArrayList<Token> tokenList, SymbolTable sTable){
        
        if(tokenList.get(0).getTokenNo() == 27){
            tokenList.remove(0);
            StNode NANDnode = new StNode();
            NANDnode.setNodeID("NAND");
            return NANDnode;
        }
        else if(tokenList.get(0).getTokenNo() == 28){
            tokenList.remove(0);
            StNode NORnode = new StNode();
            NORnode.setNodeID("NOR");
            return NORnode;
        }
        else if(tokenList.get(0).getTokenNo() == 29){
            tokenList.remove(0);
            StNode NXORnode = new StNode();
            NXORnode.setNodeID("NXOR");
            return NXORnode;
        }
        else{
            //error detection
            return null;
        }
    }

}
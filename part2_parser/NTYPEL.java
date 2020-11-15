/*
NTYPEL <typelist> ::= <type> <typelist>
Special <typelist> ::= <type>
   -left factoring-
       <typelist> ::= <type> opt_typelist
       opt_typelist ::= <typelist> | ε

NRTYPE <type> ::= <structid> is <fields> end
NATYPE <type> ::= <typeid> is array [ <expr> ] of <structid>
*/
import java.util.ArrayList; 
public class NTYPEL{
    public static StNode typelist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NTYPELnode = new StNode();
        


        StNode NTYPEnode = structOrTypeID(tokenList, sTable);
        if(NTYPEnode.isNUNDEF()){
            if(errorRecovery(tokenList, sTable)){

            }
            else{
                NTYPELnode.notEmptyContainsError();
                return NTYPELnode;
            }
        }
            
            

        StNode opt_typelist = opt_typelist(tokenList, sTable);
        //The case where there is only arrdecl, opt_arrdecls is empty, no node ID attributed
        if(opt_typelist == null && !NTYPEnode.isNUNDEF()){  
            NTYPELnode.setLeft(NTYPEnode);
            return NTYPELnode;
        }
        //there has been an error in the next arrdecl that is within the next arrdecls function
        else if(opt_typelist == null && NTYPEnode.isNUNDEF()){
            return NTYPELnode;
        }
        //there has been an error in the next typelist call and arrdecl is still good
        else if(opt_typelist != null && (opt_typelist.isNotEmptyContainsError() && opt_typelist.isNUNDEF())){
            NTYPELnode.setNodeID("NTYPEL");
            NTYPELnode.setLeft(NTYPEnode);
            return NTYPELnode;
        }


        NTYPELnode.setNodeID("NTYPEL");
        NTYPELnode.setLeft(NTYPEnode);
        NTYPELnode.setRight(opt_typelist);
       
        return NTYPELnode;
    }

    public static StNode opt_typelist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_typelist = new StNode();

        if(tokenList.get(0).getTokenNo() == 58){
            StNode typelist = typelist(tokenList, sTable);
            
            if(typelist.isNUNDEF() && typelist.isNotEmptyContainsError()){
                opt_typelist.notEmptyContainsError();
                return opt_typelist;
            }

            opt_typelist.setLeft(typelist);
            return opt_typelist;
        }
        else{
            return null;
        }
        
    }

    public static StNode structOrTypeID(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode structOrTypeID = new StNode();
        String nodeID = "";

        

        if(tokenList.get(0).getTokenNo() != 58){    
            String error = "Type is missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return structOrTypeID;
        }

        TableEntry entry = new TableEntry(tokenList.get(0));

        tokenList.remove(0);


        if(tokenList.get(0).getTokenNo() != 4){
            String error = "Type missing 'is' keyword";
            sTable.parseError(tokenList.get(0), error);
            //add detected error String to list somehow
            return structOrTypeID;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() == 9){
            nodeID = "NATYPE";
            
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 33){
                String error = "Type missing left bracket operator '['";
                sTable.parseError(tokenList.get(0), error);
                return structOrTypeID;
            }
            tokenList.remove(0);

            StNode expr = NNOT.expr(tokenList, sTable);
            if(expr.isNUNDEF() && expr.isNotEmptyContainsError()){
                return structOrTypeID;
            }

            if(tokenList.get(0).getTokenNo() != 34){
                String error = "Type missing right bracket operator ']'";
                sTable.parseError(tokenList.get(0), error);
                return structOrTypeID;
            }
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 10){
                String error = "Type missing 'of' keyword";
                sTable.parseError(tokenList.get(0), error);
                return structOrTypeID;
            }
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 58){
                String error = "Type is missing identifier";
                sTable.parseError(tokenList.get(0), error);
                return structOrTypeID;
            }
            tokenList.remove(0);

            structOrTypeID.setLeft(expr);
        }

        else if(tokenList.get(0).getTokenNo() == 58){
            nodeID = "NRTYPE";

            StNode NFLISTnode = NFLIST.fields(tokenList, sTable);
            if(NFLISTnode.isNUNDEF() && NFLISTnode.isNotEmptyContainsError()){
                return structOrTypeID;
            }
            

            if(tokenList.get(0).getTokenNo() != 8){
                String error = "Type is missing 'end' keyword";
                sTable.parseError(tokenList.get(0), error);
                return structOrTypeID;
            }
            tokenList.remove(0);

            structOrTypeID.setLeft(NFLISTnode);
        }
        structOrTypeID.setNodeID(nodeID);
        structOrTypeID.setSymbolTableReference(entry);
        sTable.setTableEntry(entry);

        return structOrTypeID;
    }

    public static boolean errorRecovery(ArrayList<Token> tokenList, SymbolTable sTable){
        int tokPos = -1;
        int tokNo = 0;

        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == 4 || tokenList.get(i).getTokenNo() == 9 ||
                tokenList.get(i).getTokenNo() == 11 || tokenList.get(i).getTokenNo() == 6){
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
            //Found "is" keyword before arrays, funcs, and main
            if(tokNo == 4){
                while(tokenList.get(1).getTokenNo() != 4){
                    tokenList.remove(0);
                    
                }
            }
            //Found "arrays" before funcs and main
            else if(tokNo == 9){
                while(tokenList.get(0).getTokenNo() != 9){
                    tokenList.remove(0);
                }
            }
            //Found "funcs" before main
            else if(tokNo == 11){
                while(tokenList.get(0).getTokenNo() != 11){
                    tokenList.remove(0);
                }
            }
            //Found main
            else if(tokNo == 6){
                while(tokenList.get(0).getTokenNo() != 6){
                    tokenList.remove(0);
                }
            }
            return true;
        }
        //check next is
        //check next array declaration 
        //check next function
        //check main
    }
}
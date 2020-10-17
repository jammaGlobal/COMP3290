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
        NTYPELnode.setNodeID("NTYPEL");

        
        if(tokenList.get(0).getTokenNo() == 58){
            TableEntry entry = new TableEntry(tokenList.get(0));
            sTable.setTableEntry(entry);

            tokenList.remove(0);

            StNode NTYPEnode = structOrTypeID(tokenList, sTable, entry);
            NTYPELnode.setLeft(NTYPEnode);

            StNode opt_typelist = opt_typelist(tokenList, sTable);
            NTYPELnode.setRight(opt_typelist);
        }
        else{
            //error
        }

        return NTYPELnode;
    }

    public static StNode opt_typelist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode opt_typelist = new StNode();

        StNode typelist = typelist(tokenList, sTable);
        opt_typelist.setLeft(typelist);

        return opt_typelist;
    }

    public static StNode structOrTypeID(ArrayList<Token> tokenList, SymbolTable sTable, TableEntry ent){
        StNode structOrTypeID = new StNode();

        structOrTypeID.setSymbolTableReference(ent);

        if(tokenList.get(0).getTokenNo() == 4){
            tokenList.remove(0);
            if(tokenList.get(0).getTokenNo() == 5){
                structOrTypeID.setNodeID("NATYPE");
                

                if(tokenList.get(0).getTokenNo() == 33){
                    tokenList.remove(0);
                    StNode expr = NNOT.expr(tokenList, sTable);
                    structOrTypeID.setLeft(expr);
                    
                }
            }
            else if(tokenList.get(0).getTokenNo() == 58){
                structOrTypeID.setNodeID("NRTYPE");

                StNode NFLISTnode = NFLIST.fields(tokenList, sTable);
                structOrTypeID.setLeft(NFLISTnode);

                if(tokenList.get(0).getTokenNo() == 8){
                    tokenList.remove(0);
                }
                else{
                    //error
                }
            }
        }
        else{
            //error
        }

        return structOrTypeID;
    }
}
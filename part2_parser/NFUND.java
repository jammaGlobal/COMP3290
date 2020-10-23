/*
NFUND <func> ::= func <id> ( <plist> ) : <rtype> <funcbody>
Special <rtype> ::= <stype> | void
Special <plist> ::= <params> | ε

Special <stype> ::= int | real | bool

Special <funcbody> ::= <locals> begin <stats> end
Special <locals> ::= <dlist> | ε

*/
import java.util.ArrayList; 
public class NFUND{

    public static StNode func(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NFUNDnode = new StNode();

        if(tokenList.get(0).getTokenNo() == 11){
            tokenList.remove(0);
            if(tokenList.get(0).getTokenNo() == 58){
                TableEntry entry = new TableEntry(tokenList.get(0));
                sTable.setTableEntry(entry);
                NFUNDnode.setSymbolTableReference(entry);
                NFUNDnode.setNodeID("NFUND");
                tokenList.remove(0);


            }
            else{
                //error
            }

            if(tokenList.get(0).getTokenNo() != 35){
                //error
            }
            tokenList.remove(0);

            StNode plist = plist(tokenList,sTable);
            NFUNDnode.setLeft(plist);

            if(tokenList.get(0).getTokenNo() != 36){
                //error
            }
            tokenList.remove(0);

            if(tokenList.get(0).getTokenNo() != 46){
                //error
            }
            tokenList.remove(0);

            StNode rtype = rtype(tokenList,sTable);
            NFUNDnode.setMiddle(rtype);

            StNode funcbody = funcbody(tokenList,sTable);
            NFUNDnode.setRight(funcbody);

        }
        else{
            //error
        }

        return NFUNDnode;
    }

    public static StNode plist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode plist = new StNode();

        if(tokenList.get(0).getTokenNo() != 36){
            StNode params = NPLIST.params(tokenList,sTable);
            //if params returns null then put to empty
            plist.setLeft(params);
        }


        return plist;
    }
    
    public static StNode rtype(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rtype = new StNode();

        if(tokenList.get(0).getTokenNo() == 14 || tokenList.get(0).getTokenNo() == 15 || tokenList.get(0).getTokenNo() == 16){
            StNode stype = stype(tokenList,sTable);
            rtype.setLeft(stype);
        }
        else if(tokenList.get(0).getTokenNo() == 12){
            //set type to void
            tokenList.remove(0);
        }
        else{
            //error
        }

        return rtype;
    }

    public static StNode funcbody(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode funcbody = new StNode();

        StNode locals = locals(tokenList,sTable);
        funcbody.setLeft(locals);
        
        if(tokenList.get(0).getTokenNo() != 7){
            //error
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList,sTable);
        funcbody.setRight(stats);

        if(tokenList.get(0).getTokenNo() != 8){
            //error
        }
        tokenList.remove(0);

        

        return funcbody;
    }

    public static StNode locals(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode locals = new StNode();

        StNode dlist = NDLIST.dlist(tokenList,sTable);
        locals.setLeft(dlist);
        //if return null error recovery

        return locals;
    }

    public static StNode stype(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode stype = new StNode();

        if(tokenList.get(0).getTokenNo() == 14){
            //attribute type to symboltablereference
        }
        else if(tokenList.get(0).getTokenNo() == 15){
            //attribute type to symboltablereference
        }
        else if(tokenList.get(0).getTokenNo() == 16){
            //attribute type to symboltablereference
        }
        else{
            //error
        }

        tokenList.remove(0);

        return stype;
    }
}
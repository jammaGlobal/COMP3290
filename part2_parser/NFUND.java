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
        boolean plistNull = false;

        //if there is no func keyword then we will consider funcs as an empty case by returning null
        if(tokenList.get(0).getTokenNo() != 11){
            return null;
        }
        tokenList.remove(0);
    
        if(tokenList.get(0).getTokenNo() != 58){
            String error = "Function is missing identifier";
            sTable.parseError(tokenList.get(0), error);
            return NFUNDnode;
        }
        TableEntry entry = new TableEntry(tokenList.get(0));
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 35){
            String error = "Function is missing left parenthesis '('";
            sTable.parseError(tokenList.get(0), error);
            return NFUNDnode;
        }
        tokenList.remove(0);

        StNode plist = plist(tokenList,sTable);
        if(plist != null && (plist.isNUNDEF() && plist.isNotEmptyContainsError())){
            return NFUNDnode;
        }
        else if(plist == null){
            plistNull = true;
        }

        if(tokenList.get(0).getTokenNo() != 36){
            String error = "Function is missing right parenthesis ')'";
            sTable.parseError(tokenList.get(0), error);
            return NFUNDnode;
        }
        tokenList.remove(0);

        if(tokenList.get(0).getTokenNo() != 46){
            String error = "Function is missing colon ':'";
            sTable.parseError(tokenList.get(0), error);
            return NFUNDnode;
        }
        tokenList.remove(0);

        StNode rtype = rtype(tokenList,sTable);
        if(rtype.isNUNDEF() && rtype.isNotEmptyContainsError()){
            return NFUNDnode;
        }
        

        StNode funcbody = funcbody(tokenList,sTable);
        if(funcbody.isNUNDEF() && funcbody.isNotEmptyContainsError()){
            return NFUNDnode;
        }

        if(plistNull){
            NFUNDnode.setLeft(rtype);
            NFUNDnode.setRight(funcbody);

            sTable.setTableEntry(entry);
            NFUNDnode.setSymbolTableReference(entry);
            NFUNDnode.setNodeID("NFUND");

            return NFUNDnode;
        }

        NFUNDnode.setLeft(plist);
        NFUNDnode.setMiddle(rtype);
        NFUNDnode.setRight(funcbody);


        sTable.setTableEntry(entry);
        NFUNDnode.setSymbolTableReference(entry);
        NFUNDnode.setNodeID("NFUND");

        
        return NFUNDnode;
    }

    public static StNode plist(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode plist = new StNode();

        if(tokenList.get(0).getTokenNo() != 36){
            StNode params = NPLIST.params(tokenList,sTable);
            if(params.isNUNDEF() && params.isNotEmptyContainsError()){
                plist.notEmptyContainsError();
                return plist;
            }
            plist.setLeft(params);

            return plist;
        }


        return null;
    }
    
    public static StNode rtype(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode rtype = new StNode();

        if(tokenList.get(0).getTokenNo() == 14 || tokenList.get(0).getTokenNo() == 15 || tokenList.get(0).getTokenNo() == 16){
            StNode stype = stype(tokenList,sTable);
            if(stype.isNUNDEF() && stype.isNotEmptyContainsError()){
                rtype.notEmptyContainsError();
                return rtype;
            }

            rtype.setLeft(stype);
        }
        else if(tokenList.get(0).getTokenNo() == 12){
            //set type to void
            tokenList.remove(0);
        }
        else{
            String error = "Function is missing return type";
            sTable.parseError(tokenList.get(0), error);
            rtype.notEmptyContainsError();
            return rtype;
        }

        return rtype;
    }

    public static StNode funcbody(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode funcbody = new StNode();

        StNode locals = locals(tokenList,sTable);
        if(locals != null && locals.isNotEmptyContainsError()){
            locals.notEmptyContainsError();
            return funcbody;
        }
       
        
        if(tokenList.get(0).getTokenNo() != 7){
            String error = "Function body is missing 'begin' keyword";
            sTable.parseError(tokenList.get(0), error);
            funcbody.notEmptyContainsError();
            return funcbody;
        }
        tokenList.remove(0);

        StNode stats = NSTATS.stats(tokenList,sTable);
        if(stats.isNUNDEF() && stats.isNotEmptyContainsError()){
            funcbody.notEmptyContainsError();
            return funcbody;
        }
       

        if(tokenList.get(0).getTokenNo() != 8){
            String error = "Function body is missing 'end' keyword";
            sTable.parseError(tokenList.get(0), error);
            funcbody.notEmptyContainsError();
            return funcbody;
        }
        tokenList.remove(0);

        if(locals != null){
            funcbody.setLeft(locals);
            funcbody.setRight(stats);
        }
        else{
            funcbody.setLeft(stats);
        }

        return funcbody;
    }

    public static StNode locals(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode locals = new StNode();

        if(tokenList.get(0).getTokenNo() == 58){
            StNode dlist = NDLIST.dlist(tokenList,sTable);

            if(dlist.isNUNDEF() && dlist.isNotEmptyContainsError()){
                locals.notEmptyContainsError();
                return locals;
            }

            locals.setLeft(dlist);
            return locals;
        }
        else{
            return null;
        }

       
        //if return null error recovery

        
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
            String error = "Function is missing return 'type'";
            sTable.parseError(tokenList.get(0), error);
            stype.notEmptyContainsError();
            return stype;
        }

        tokenList.remove(0);

        return stype;
    }
}
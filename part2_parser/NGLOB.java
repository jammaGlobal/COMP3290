import java.util.ArrayList; 
public class NGLOB{
    public static StNode globals(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NGLOBnode = new StNode();
        

        //Now in Special <consts>
        StNode consts = new StNode();
        if(tokenList.get(0).getTokenNo() == 2){
            tokenList.remove(0);

            StNode NILISTnode = NILIST.initlist(tokenList, sTable);
            if(!NILISTnode.isNotEmptyContainsError()){
                consts.setLeft(NILISTnode);
            }
            
            
        }


        StNode types = new StNode();
        if(tokenList.get(0).getTokenNo() == 3){
            tokenList.remove(0);

            StNode NTYPELnode = NTYPEL.typelist(tokenList, sTable);
            if(!NTYPELnode.isNotEmptyContainsError()){
                types.setLeft(NTYPELnode);
            }

            
        }

        StNode arrays = new StNode();
        if(tokenList.get(0).getTokenNo() == 5){
            tokenList.remove(0);

            StNode NALISTnode = NALIST.arrdecls(tokenList, sTable);
            if(!NALISTnode.isNotEmptyContainsError()){
                arrays.setLeft(NALISTnode);
            }
            
        }

        NGLOBnode.setLeft(consts);
        NGLOBnode.setMiddle(types);
        NGLOBnode.setRight(arrays);

        NGLOBnode.setNodeID("NGLOB");

        return NGLOBnode;
    }
}
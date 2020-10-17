import java.util.ArrayList; 
public class NGLOB{
    public static StNode globals(ArrayList<Token> tokenList, SymbolTable sTable){
        StNode NGLOBnode = new StNode();
        NGLOBnode.setNodeID("NGLOB");

        //Now in Special <consts>
        StNode consts = new StNode();
        if(tokenList.get(0).getTokenNo() == 2){
            tokenList.remove(0);

            StNode NILISTnode = NILIST.initlist(tokenList, sTable);
            consts.setLeft(NILISTnode);
        }


        StNode types = new StNode();
        if(tokenList.get(0).getTokenNo() == 3){
            tokenList.remove(0);

            StNode NTYPELnode = NTYPEL.typelist(tokenList, sTable);
            types.setLeft(NTYPELnode);
            
        }


        StNode arrays = new StNode();
        if(tokenList.get(0).getTokenNo() == 4){
            tokenList.remove(0);

            StNode NALISTnode = NALIST.arrdecls(tokenList, sTable);
            arrays.setLeft(NALISTnode);

        }

        NGLOBnode.setLeft(consts);
        NGLOBnode.setMiddle(types);
        NGLOBnode.setRight(arrays);

        return NGLOBnode;
    }
}
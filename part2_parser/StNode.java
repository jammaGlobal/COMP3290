//Abstract syntax tree node. The syntax tree is created through the recursive descent parsing tree and is able to be traversed
//through syntax tree nodes with a pointer
import java.util.HashMap;
import java.util.Map;
public class StNode {
    public enum NodeIdent{
        NUNDEF,
        NPROG, NGLOB, NILIST, NINIT, NFUNCS,
        NMAIN, NSDLST, NTYPEL, NRTYPE, NATYPE,
        NFLIST, NSDECL, NALIST, NARRD, NFUND,
        NPLIST, NSIMP, NARRP, NARRC, NDLIST,
        NSTATS, NFOR, NREPT, NASGNS, NIFTH,
        NIFTE, NASGN, NPLEQ, NMNEQ, NSTEQ, NDVEQ,
        NIPUT, NPRINT, NPRLN, NCALL, NRETN, NVLIST,
        NSIMV, NARRV, NEXPL, NBOOL, NNOT, NAND,
        NOR, NXOR, NEQL, NNEQ, NGRT, NGEQ, NLEQ,
        NLSS, NADD, NSUB, NMUL, NDIV, NMOD,
        NPOW, NILIT, NFLIT, NTRUE, NFALS, NFCALL,
        NPRLST, NSTRG
    }

    private NodeIdent nodeID;
    private StNode left;
    private StNode middle;
    private StNode right;
    private TableEntry symTabRef;
    private int noChildren;
    private HashMap<Token, String> errors;


    public StNode(){
        left = null;
        middle = null;
        right = null;
        noChildren = 0;

        nodeID = NodeIdent.NUNDEF;
        symTabRef = null;
        errors = new HashMap<Token, String>();

    }

    public StNode(String id){
        left = null;
        middle = null;
        right = null;

        nodeID = NodeIdent.valueOf(id);
        symTabRef = null;
    }

    public void setChildren(StNode left, StNode middle, StNode right){
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public void setLeft(StNode left){
        this.left = left;
        noChildren++;
    }

    public void setMiddle(StNode middle){
        this.middle = middle;
        noChildren++;
    }

    public void setRight(StNode right){
        this.right = right;
        noChildren++;
    }

    public int getNoChildren(){
        return noChildren;
    }

    public void setSymbolTableReference(TableEntry entry){
        symTabRef = entry;
    }

    public StNode getLeft(){
        return this.left;
    }

    public StNode getMiddle(){
        return this.middle;
    }

    public StNode getRight(){
        return this.right;
    }

    public void setNodeID(String id){
        this.nodeID = NodeIdent.valueOf(id);
    }

    public String output(){
        String out = "";
        if(this.nodeID != NodeIdent.NUNDEF){
            out = this.nodeID + "\t";
            if(this.symTabRef != null && this.symTabRef.getType() == 58){
                out += this.symTabRef.getName();

            }
        }


        return out;
 
    }

    public void addError(){
        //
    }



}
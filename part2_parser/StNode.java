//Abstract syntax tree node. The syntax tree is created through the recursive descent parsing tree and is able to be traversed
//through syntax tree nodes with a pointer
import java.util.HashMap;
import java.util.Map;
import java.io.*;
public class StNode {
    public enum NodeIdent{
        NUNDEF,
        NPROG, NGLOB, NILIST, NINIT, NFUNCS,
        NMAIN, NSDLST, NTYPEL, NRTYPE, NATYPE,
        NFLIST, NSDECL, NALIST, NARRD, NFUND,
        NPLIST, NSIMP, NARRP, NARRC, NDLIST,
        NSTATS, NFOR, NREPT, NASGNS, NIFTH,
        NIFTE, NASGN, NPLEQ, NMNEQ, NSTEQ, NDVEQ,
        NINPUT, NPRINT, NPRLN, NCALL, NRETN, NVLIST,
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
    private boolean notEmptyContainsError;
    private static int count = 0;


    public StNode(){
        left = null;
        middle = null;
        right = null;
        noChildren = 0;

        nodeID = NodeIdent.NUNDEF;
        symTabRef = null;
        errors = new HashMap<Token, String>();
        notEmptyContainsError = false;

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

    public TableEntry getSymbolTableReference(){
        return symTabRef;
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

    public String getNodeID(){
        return String.valueOf(this.nodeID);
    }

    public boolean isNUNDEF(){
        if(nodeID == NodeIdent.NUNDEF){
            return true;
        }
        else 
            return false;
    }

    public void notEmptyContainsError(){
        notEmptyContainsError = true;
    }

    public boolean isNotEmptyContainsError(){
        return notEmptyContainsError;
    }

    public String output(){
        String out = "";
        if(this.nodeID != NodeIdent.NUNDEF){
            out = this.nodeID + "\t";
            if(this.symTabRef != null && this.symTabRef.getType() == 58){
                out += this.symTabRef.getName() + "\t";

            }
        }


        return out;
 
    }

    private static BufferedWriter lstFileWriter;
    public static void setLstFileWriter(BufferedWriter fileWriter) { lstFileWriter = fileWriter;}

    private static BufferedWriter xmlFileWriter;
    public static void setXmlFileWriter (BufferedWriter fileWriter) {
        xmlFileWriter = fileWriter;
    }

    public static void printTreePreOrder(StNode tr, String childType) throws IOException{

        if (tr.getNodeID().equals("NUNDEF") && tr.getNoChildren() == 0) {
            return;
        }

        if(tr.getNodeID().equals("NDIV")){
            int p = 0;
        }

        if (tr.getNodeID().equals("NPROG")) {
            if (xmlFileWriter != null) {
                xmlFileWriter.append("<root>\n");
            }
            count = 0;
        }

        if (xmlFileWriter != null) {
            xmlFileWriter.append("<nodeType value=\"" + tr.getNodeID() + " \"/>\n");
        }

        if(!tr.isNUNDEF()){
            String nodeType = tr.getNodeID() + " ";
            nodeType = checkColumns(nodeType);

            System.out.print(nodeType);
            lstFileWriter.append(nodeType);
            count++;
        }
        

        if (count >= 10)  {
            count = 0;
            System.out.println();
            lstFileWriter.append("\n");
        }

        if (tr.getSymbolTableReference() != null) {
            String toPrint = tr.getSymbolTableReference().getName() + " ";

            if (xmlFileWriter != null) {
                String toPrintXml = toPrint.replace("\"", "");
                xmlFileWriter.append("<nodeSymbolValue value=\"" + toPrintXml + " \"/>\n");
            }

            toPrint = checkColumns(toPrint);
            System.out.print(toPrint);
            lstFileWriter.append(toPrint);

            int countIncreaseBy = (toPrint.length() / 7);
            count += countIncreaseBy;
            if (count >= 10) {
                count = 0;
                System.out.println();
                lstFileWriter.append("\n");
            };
        }


        // pre-order traversal of syntax tree
        if (tr.left   != null) {
            if (xmlFileWriter != null) {
                xmlFileWriter.append("<child which=\"left\">\n");
            }

            printTreePreOrder(tr.left, "left");

            if (xmlFileWriter != null) {
                xmlFileWriter.append("</child>\n");
            }
        }

        if (tr.middle != null) {
            if (xmlFileWriter != null) {
                xmlFileWriter.append("<child which=\"middle\">\n");
            }

            printTreePreOrder(tr.middle, "middle");

            if (xmlFileWriter != null) {
                xmlFileWriter.append("</child>\n");
            }
        }

        if (tr.right  != null) {
            if (xmlFileWriter != null) {
                xmlFileWriter.append("<child which=\"right\">\n");
            }

            printTreePreOrder(tr.right, "right");

            if (xmlFileWriter != null) {
                xmlFileWriter.append("</child>\n");
            }
        }

        if (tr.getNodeID().equals("NPROG")) {
            if (xmlFileWriter != null) {
                xmlFileWriter.append("</root>");
            }
        }
    }

    public static String checkColumns (String s) {
        if (s.length() % 7 == 0) {
            return s;
        }

        int paddingAmount = 7 - (s.length() % 7);
        for (int i = 0; i < paddingAmount; i++) {
            s += " ";
        }
        return s;
    }




}
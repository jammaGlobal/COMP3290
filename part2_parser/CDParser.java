import java.util.ArrayList;
import java.util.LinkedList; 
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

//Some errors will be derived from null returns, some returns are legitimate null returns like when an e option is available
//returns must be legit nodes, null, and error node

//MINOR CHANGE: Change single children nodes to left instead of middle, change double to left and right

//Perhaps errors will be null returns, empty node returns are allowed in e cases in that case we assess the node value which should be NUNDEF

public class CDParser{

    ArrayList<Token> tokenList;
    SymbolTable symTable;
    //there is a singleton class error list within the symbolTable which can be accessed
    StNode root;
    

    public CDParser(ArrayList<Token> totalTokens){
        tokenList = totalTokens;
        symTable = new SymbolTable();
        root = null;
    }

    public void startParse(){

        //Checks if token is CD20 token
        if(tokenList.get(0).getTokenNo() == 1){
            tokenList.remove(0);
            root = NPROG.program(tokenList, symTable);
            if(root.isNUNDEF()){
                System.out.println("Error(s) occur in positions that harm MAIN scope integrity/cannot be recovered from");
                System.out.println("No program listing will be produced");
            }
        }
        else{
            String error = "Program missing initial CD20 keyword";
            symTable.parseError(tokenList.get(0), error);
        }   
    }

    public void printProgramListing(){
        preorderTraversal(root);
        System.out.println("");
    }

    //For xml printing
    public void printProgramListing(String mode){
        System.out.println("");
        try {
            BufferedWriter lstFileWriter = new BufferedWriter(new FileWriter("output.lst", false));
            StNode.setLstFileWriter(lstFileWriter);
            lstFileWriter.write("");

            BufferedWriter xmlFileWriter = new BufferedWriter(new FileWriter("treeOutput.xml", false));
            StNode.setXmlFileWriter(xmlFileWriter);
            xmlFileWriter.write("");
            StNode.printTree(root, "");
            xmlFileWriter.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        // preorderTraversal(root);
        // System.out.println("");
    }

    public void printErrorListing(){
        System.out.println("");
        LinkedList<String> errorList = symTable.returnErrorList();
        for(int i = 0; i < errorList.size() ; i++){
            System.out.println(errorList.get(i));
        }
    }

    //has to traverse middle children as well
    public void preorderTraversal(StNode root){
        if(root != null){
            System.out.print(root.output());
            if(root.getNoChildren() == 1){
                preorderTraversal(root.getLeft());
            }
            else if(root.getNoChildren() == 2){
                preorderTraversal(root.getLeft()); 
                preorderTraversal(root.getRight());
            }
            else if(root.getNoChildren() == 3){
                preorderTraversal(root.getLeft());
                preorderTraversal(root.getMiddle());
                preorderTraversal(root.getRight());
            }
            else if(root.getNoChildren() == 0){
                //nothing
            }
            else{
                System.out.print("Node has too many children");
            }

        }
    }

}
import java.util.ArrayList; 

//Some errors will be derived from null returns, some returns are legitimate null returns like when an e option is available
//returns must be legit nodes, null, and error node

//MINOR CHANGE: Change single children nodes to left instead of middle, change double to left and right

//Perhaps errors will be null returns, empty node returns are allowed in e cases in that case we assess the node value which should be NUNDEF

public class CDParser{

    ArrayList<Token> tokenList;
    SymbolTable symTable;
    ArrayList<Token> errorListing;
    StNode root;

    public CDParser(ArrayList<Token> totalTokens){
        tokenList = totalTokens;
        symTable = new SymbolTable();
        errorListing = new ArrayList<Token>();
        root = null;
    }

    public void startParse(){

        //Checks if token is CD20 token
        if(tokenList.get(0).getTokenNo() == 1){
            tokenList.remove(0);
            root = NPROG.program(tokenList, symTable);
        }
        else{
            //syntax error, end parse
        }   
    }

    public void printProgramListing(){
        preorderTraversal(root);
    }

    public void printErrorListing(){
        //yeah
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
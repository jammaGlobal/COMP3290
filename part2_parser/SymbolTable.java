import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList; 
public class SymbolTable {
    private HashMap<Integer, TableEntry> symbolTab;
    CreateErrorList eList;


    public SymbolTable (){
        symbolTab = new HashMap<Integer, TableEntry>();
        eList = CreateErrorList.getInstance();
    }

    public void setTableEntry(TableEntry entry){
        symbolTab.put(entry.getName().hashCode(), entry);
    }

    public boolean contains(TableEntry possEntry) {
        boolean val = false;
        for(Map.Entry entry : symbolTab.entrySet()){
            if(possEntry.equals(entry)){
                val = true;
            }
        }
        return val;
    }

    public void parseError(Token token, String error){
        String errorString = "Error on Line: "+token.getLine()+ "\t("+error+")";
        eList.addErrorToList(errorString);
    }

    public int findToken(ArrayList<Token> tokenList, int tokenNo){
        int pos = -1;
        for(int i = 0; i < tokenList.size() ; i++){
            if(tokenList.get(i).getTokenNo() == tokenNo){
                pos = i;
                break;
            }
        }

        return pos;

    }

    public LinkedList<String> returnErrorList(){
        return eList.getErrorList();
    }

}
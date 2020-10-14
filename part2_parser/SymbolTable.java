import java.util.HashMap;
import java.util.Map;
public class SymbolTable {
    private HashMap<Integer, TableEntry> symbolTab;


    public SymbolTable (){
        symbolTab = new HashMap<Integer, TableEntry>();
    }

    public void setTableEntry(TableEntry entry){
//if(contains(entry)){
            //using hashcode of token lexeme as the key/the position in the table
//symbolTab.put(entry.getName().hashCode(), entry);
//}
       // else
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

}
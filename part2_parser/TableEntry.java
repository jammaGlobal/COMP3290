public class TableEntry{
    private int type;
    private String name;
    private int lineNo;
    private int colNo;

    //Symbol type will be introduced in the semantics && codegen
    public TableEntry(Token token){
        this.type = token.getTokenNo();
        this.name = token.getLexeme();
        this.lineNo = token.getLine();
        this.colNo = token.getColumn();

        //Name
        //Type : var, func, proc; if var what type (int, )
        //Location : line and column number
        //Scope: identifies the region of the program in which the current symbol definition is valid
        //Other attributes: array limits, record fields / parameters / return values of functions


    }
    
    public String getName(){
        return this.name;
    }

    public int getType(){
        return this.type;
    }

    //Comparative function that only compares type and name to determine if the symbol table contains a particular symbol
    //Location data is outside of this comparison as equivalent symbols can occur at different points of the source code 
    //but this information is important and thus still stored in symbols within table
    public boolean equals(Object object){

        if(object instanceof TableEntry){
            TableEntry otherEntry = (TableEntry) object;
            if(this.type == otherEntry.type && this.name.equals(otherEntry.name)){
                return true;
            }
        }
            return false;
    }
}
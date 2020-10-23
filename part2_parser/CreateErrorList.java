import java.util.LinkedList;
public class CreateErrorList{

    LinkedList<String> errorList;

    private CreateErrorList(){
        this.errorList = new LinkedList<String>();

    }

    public static CreateErrorList getInstance(){
        CreateErrorList eList = new CreateErrorList();
        return eList;
    }

    public void addErrorToList(String error){
        errorList.add(error);
    }

    public LinkedList<String> getErrorList(){
        return errorList;
    }


}
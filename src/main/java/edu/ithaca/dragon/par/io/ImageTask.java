package hello;




import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ImageTask{

    public JsonUtil jsonUtil;
    private String srcPath="/java/resources/QuestionToJSONFileTest1.txt";


    public List<Question> loadQuestions(){
        List<Question> taskQuestions = new ArrayList<>();
        jsonUtil= new JsonUtil();
        try {
            jsonUtil.toJsonFile(srcPath, taskQuestions);
        }catch(Exception e){}
        return taskQuestions;
    }



}

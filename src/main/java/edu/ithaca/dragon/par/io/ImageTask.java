package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.util.ArrayList;
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

package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;

import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class UserResponseSetTest {

    @Test
    public void getResponseSizeTest()throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        List<ResponsesPerQuestion> userResponse =new ArrayList<>();
        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userResponse.add(response);
        }
        respSet.addAllResponses(userResponse);
        assertEquals(respSet.getUserResponsesSize(),15);
      //  respSet.splitResponsesByType(userResponse);
        //respSet.knowledgeScoreByType();

    }

    @Test
    public void addToResponsesPerQuestionTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());

        ResponsesPerQuestion response1 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0),responsesFromFile.get(0).getResponseTexts().get(0));
        ResponsesPerQuestion response2 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(1),responsesFromFile.get(0).getResponseTexts().get(1));
        respSet.addResponse(response1);respSet.addResponse(response1);respSet.addResponse(response2);

        assertEquals(2,response1.allResponseTextSize());
        assertEquals(1,response2.allResponseTextSize());

    }


    @Test
    public void knowledgeCalcTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        //For empty list returns -1
        UserResponseSet responseSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        assertEquals(-1.000,responseSet.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);

        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            responseSet.addResponse(response);
        }       assertEquals(100.00,responseSet.knowledgeCalc(),DataUtil.OK_DOUBLE_MARGIN);

        for(int i=11;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),"wrongAnswer");
            responseSet.addResponse(response);
        }       assertEquals(86.66666666666667,responseSet.knowledgeCalc(),DataUtil.OK_DOUBLE_MARGIN);

        for(int i=6;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),"wrongAnswer");
            responseSet.addResponse(response);
        }       assertEquals(70.0,responseSet.knowledgeCalc(),DataUtil.OK_DOUBLE_MARGIN);

        for(int i=2;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),"wrongAnswer");
            responseSet.addResponse(response);
        }       assertEquals(56.666666666666664,responseSet.knowledgeCalc(),DataUtil.OK_DOUBLE_MARGIN);

        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),"wrongAnswer");
            responseSet.addResponse(response);
        }       assertEquals(50.0,responseSet.knowledgeCalc(),DataUtil.OK_DOUBLE_MARGIN);

        UserResponseSet responseSet1=new UserResponseSet("badStudent");
        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion("badStudent", questionsFromFile.get(i),"hi");
            responseSet1.addResponse(response);
        }   assertEquals(0.0,responseSet1.knowledgeCalc(),DataUtil.OK_DOUBLE_MARGIN);



    }

}

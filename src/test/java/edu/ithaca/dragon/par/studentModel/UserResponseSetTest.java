package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;

import edu.ithaca.dragon.par.io.ImageTaskResponse;
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



    public static double OK_DOUBLE_MARGIN = (double) 0.00001;
    @Test//TODO: GET RID OF OK_DOUBLE_MARGIN/REDO KNOWLEDGE TEST
    public void knowledgeCalcTest() throws IOException{
        //For empty list returns -1
        UserResponseSet repSet=new UserResponseSet("HI");
       // assertEquals(-1.000,repSet.knowledgeCalc(),OK_DOUBLE_MARGIN);

        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet2=new UserResponseSet(responsesFromFile.get(0).getUserId());
        List<ResponsesPerQuestion> userRespons =new ArrayList<>();
        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userRespons.add(response);
        }
        respSet2.addAllResponses(userRespons);
    //    assertEquals(100.00,respSet2.knowledgeCalc(),OK_DOUBLE_MARGIN);

        //decrement
        ResponsesPerQuestion response1 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0),"hi");
        respSet2.addResponse(response1);

       // assertEquals(75.00,respSet2.knowledgeCalc(),OK_DOUBLE_MARGIN);
        respSet2.addResponse(response1);
      //  assertEquals(60.00,respSet2.knowledgeCalc(),OK_DOUBLE_MARGIN);

        // 15/16 = 93.75
      //  assertEquals(93.75,respSet2.calcScore(),OK_DOUBLE_MARGIN);
        respSet2.addResponse(response1);
        // 15/17
       // assertEquals(88.23529411764707,respSet2.calcScore(),OK_DOUBLE_MARGIN);


        //increment
        ResponsesPerQuestion response2 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0),responsesFromFile.get(0).getResponseTexts().get(0));
        respSet2.addResponse(response2);

      //  assertEquals(66.666666,respSet2.knowledgeCalc(),OK_DOUBLE_MARGIN);

        // 16/18
      //  assertEquals(88.88888888888889,respSet2.calcScore(),OK_DOUBLE_MARGIN);



    }

}

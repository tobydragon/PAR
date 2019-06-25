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
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        List<UserResponse> userResponses=new ArrayList<>();
        for(int i=0;i<questionsFromFile.size();i++){
            UserResponse response = new UserResponse(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userResponses.add(response);
        }
        respSet.addAllResponses(userResponses);
        assertEquals(respSet.getUserResponsesSize(),3);
    }



    public static double OK_DOUBLE_MARGIN = (double) 0.00001;
    @Test
    public void calcScoreTest() throws IOException{
        //For empty list returns -1
        UserResponseSet repSet=new UserResponseSet("HI");
        assertEquals(-1.000,repSet.calcScore(),OK_DOUBLE_MARGIN);

        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet2=new UserResponseSet(responsesFromFile.get(0).getUserId());
        List<UserResponse> userResponses=new ArrayList<>();
        for(int i=0;i<questionsFromFile.size();i++){
            UserResponse response = new UserResponse(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userResponses.add(response);
        }
        respSet2.addAllResponses(userResponses);
        assertEquals(100.00,respSet2.calcScore(),OK_DOUBLE_MARGIN);

        //decrement
        UserResponse response1 = new UserResponse(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0),"hi");
        respSet2.addResponse(response1);
        assertEquals(75.00,respSet2.calcScore(),OK_DOUBLE_MARGIN);
        respSet2.addResponse(response1);
        assertEquals(60.00,respSet2.calcScore(),OK_DOUBLE_MARGIN);

        //increment
        UserResponse response2 = new UserResponse(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0),responsesFromFile.get(0).getResponseTexts().get(0));
        respSet2.addResponse(response2);
        assertEquals(66.666666,respSet2.calcScore(),OK_DOUBLE_MARGIN);


    }

}

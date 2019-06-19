package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;

import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void getScoreTest() throws IOException{
        //all correct-100
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        for(int i=0;i<questionsFromFile.size();i++){
            UserResponse response = new UserResponse(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            respSet.addResponse(response);
        }
        assertEquals(100.0,respSet.CalcScore(),OK_DOUBLE_MARGIN);

        //only gets one right-33.33
        UserResponseSet respSet2=new UserResponseSet(responsesFromFile.get(0).getUserId());
        for(int i=0;i<questionsFromFile.size();i++){
            UserResponse response2 = new UserResponse(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(2));
            respSet2.addResponse(response2);
        }
        assertEquals(33.33333333,respSet2.CalcScore(),OK_DOUBLE_MARGIN);
    }
}

package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTaskResponseImp1;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResponsesPerQuestionTest {

    @Test
    public void knowledgeCalcTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponseImp1> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponseImp1.class);

        ResponsesPerQuestion responsesPerQuestion = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0), responsesFromFile.get(0).getResponseTexts().get(0));
        assertEquals(100.0, responsesPerQuestion.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);
        responsesPerQuestion.addNewResponse("hello");
        assertEquals(0, responsesPerQuestion.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);

        ResponsesPerQuestion responsesPerQuestion2 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0), "hello");
        assertEquals(0.0, responsesPerQuestion2.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);
        responsesPerQuestion2.addNewResponse("Lateral");//should not improve score
        assertEquals(0, responsesPerQuestion2.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);
        responsesPerQuestion2.getAllResponses().get(1).setMillSeconds(responsesPerQuestion2.getAllResponses().get(1).getMillSeconds()+30000);//adding 30 seconds to second answer so it counts as a correct answer-100
        assertEquals(100.0, responsesPerQuestion2.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);

    }

    @Test
    public void allResponseTextSizeAndCheckTimeStampTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponseImp1> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponseImp1.class);
        ResponsesPerQuestion responsesPerQuestion = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0), responsesFromFile.get(0).getResponseTexts().get(0));
        assertEquals(1, responsesPerQuestion.allResponsesSize());
        responsesPerQuestion.addNewResponse("answer2");
        assertEquals(2, responsesPerQuestion.allResponsesSize());//timestamps should not affect adding of responses
        //timestamps are only taken into consideration when calculating the knowledge score
    }

    /*
    @Test
    public void checkTimeStampDifferenceTest(){
        ResponsesPerQuestion responsesPerQuestion=new ResponsesPerQuestion();
        Date date=new Date();
        long time2=date.getTime();
        long time1=time2+30000;
        long time3=time2+29000;
        assertEquals(false,responsesPerQuestion.checkTimeStampDifference(time1,time1));

        assertEquals(true,responsesPerQuestion.checkTimeStampDifference(time1,time2));
        assertEquals(false,responsesPerQuestion.checkTimeStampDifference(time3,time2));
    }

     */


}

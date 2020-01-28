package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResponsesPerQuestionTest {

    @Test
    public void knowledgeCalcTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponseOOP> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponseOOP.class);

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
        List<ImageTaskResponseOOP> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponseOOP.class);
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

    @Test
    public void staticKnowledgeCalcTest(){
        //create a static knowledgeCalc function
        //original knowledgeCalc will call the static function
        Question question1 = new Question("testQuestionId",  "test?", EquineQuestionTypes.structure.toString(), "Correct answer", Arrays.asList("Correct Answer", "Incorrect Answer"), "imageUrl");
        QuestionResponse qr1 = new QuestionResponse("Correct answer");
        qr1.setMillSeconds(1);
        QuestionResponse qr2 = new QuestionResponse("Correct answer");
        qr2.setMillSeconds(1);
        ArrayList<QuestionResponse> questionResponseArraylist = new ArrayList<>(Arrays.asList(qr1, qr2));

        //response has same capitalization, responses are within 30 sec
        assertEquals(100.0, ResponsesPerQuestion.knowledgeCalc(questionResponseArraylist, question1), 1);

        //response has different capitalization, responses are within 30 sec
        qr2.setResponseText("correct Answer");
        assertEquals(100.0, ResponsesPerQuestion.knowledgeCalc(questionResponseArraylist, question1), 1);

        //responses are over 30 seconds apart
        Date date=new Date();
        long newMillSeconds=date.getTime();
        qr2.setMillSeconds(newMillSeconds + 40000);
        assertEquals(100.0, ResponsesPerQuestion.knowledgeCalc(questionResponseArraylist, question1), 1);
    }

    @Test
    public void checkIfAnswerIsCorrectTest(){
        //compare strings
    }


}

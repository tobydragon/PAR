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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponsesPerQuestionTest {

    @Test
    public void knowledgeCalcTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/SampleQuestionPool.json", Question.class);
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
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/SampleQuestionPool.json", Question.class);
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
        qr1.setMillSeconds(1000);
        QuestionResponse qr2 = new QuestionResponse("Correct answer");
        qr2.setMillSeconds(2000);
        QuestionResponse qr3 = new QuestionResponse("Incorect answer");
        qr3.setMillSeconds(2000);
        QuestionResponse qr4 = new QuestionResponse("correct answer");
        qr4.setMillSeconds(102000);
        QuestionResponse qr5 = new QuestionResponse("Incorect answer");
        qr5.setMillSeconds(102000);
        QuestionResponse qr6 = new QuestionResponse("correct Answer");
        qr6.setMillSeconds(2000);

        //response has same capitalization, responses are within 30 sec
        ArrayList<QuestionResponse> questionResponseArraylist = new ArrayList<>(Arrays.asList(qr2, qr1));
        assertEquals(100.0, ResponsesPerQuestion.knowledgeCalc(questionResponseArraylist, question1), 1);

        //response has different capitalization, responses are within 30 sec
        ArrayList<QuestionResponse> questionResponseArraylist2 = new ArrayList<>(Arrays.asList(qr6, qr1));
        assertEquals(100.0, ResponsesPerQuestion.knowledgeCalc(questionResponseArraylist2, question1), 1);

        //incorrect responses are over 30 seconds
        ArrayList<QuestionResponse> questionResponseArraylist3 = new ArrayList<>(Arrays.asList(qr5, qr2, qr1));
        assertEquals(100.0, ResponsesPerQuestion.knowledgeCalc(questionResponseArraylist3, question1), 1);

        //incorrect answer submitted within 30 seconds
        ArrayList<QuestionResponse> questionResponseArraylist4 = new ArrayList<>(Arrays.asList(qr3, qr2, qr1));
        assertEquals(0.0, ResponsesPerQuestion.knowledgeCalc(questionResponseArraylist4, question1), 1);
    }


    @Test
    public void questionsWithinTimeFromRecentResponseTest(){
        QuestionResponse qr1 = new QuestionResponse("response");
        qr1.setMillSeconds(1000);
        QuestionResponse qr2 = new QuestionResponse("response");
        qr2.setMillSeconds(2000);
        QuestionResponse qr3 = new QuestionResponse("response");
        qr3.setMillSeconds(30999);
        QuestionResponse qr4 = new QuestionResponse("response");
        qr4.setMillSeconds(31001);
        QuestionResponse qr5 = new QuestionResponse("response");
        qr5.setMillSeconds(50000);

        //qr4 and qr5 should not be added to the new arraylist
        ArrayList<QuestionResponse> questionResponseArrayList1 = new ArrayList<>(Arrays.asList(qr5, qr4, qr3, qr2, qr1));
        List<QuestionResponse> result = ResponsesPerQuestion.questionsWithinTimeFromRecentResponse(questionResponseArrayList1);
        assertEquals(3, result.size());
    }

    @Test
    public void checkIfAnswerIsCorrectTest() {
        assertTrue(ResponsesPerQuestion.checkIfAnswerIsCorrect("Correct Answer", " Correct Answer "));
        assertTrue(ResponsesPerQuestion.checkIfAnswerIsCorrect("Correct Answer", "correct Answer"));
        assertTrue(ResponsesPerQuestion.checkIfAnswerIsCorrect("Correct Answer", "CORRECT ANSWER"));
        assertTrue(ResponsesPerQuestion.checkIfAnswerIsCorrect("Correct Answer", "correct answer"));
        assertFalse(ResponsesPerQuestion.checkIfAnswerIsCorrect("Correct Answer", "Incorrect Answer"));
    }


    @Test
    public void checkIfAnswersAreCorrectTest() {
        Question question = new Question("q1", "q text", EquineQuestionTypes.structure.toString(), "correct answer", null, "url");
        QuestionResponse qr1 = new QuestionResponse("correct Answer");
        QuestionResponse qr2 = new QuestionResponse("Correct answer");
        QuestionResponse qr3 = new QuestionResponse("correct answer");
        QuestionResponse qr4 = new QuestionResponse("Incorrect answer");
        ArrayList<QuestionResponse> qrAL = new ArrayList<>(Arrays.asList(qr1, qr2, qr3));

        //all responses are correct
        assertTrue(ResponsesPerQuestion.checkIfAnswersAreCorrect(qrAL, question.getCorrectAnswer()));

        //add incorrect response
        qrAL.add(qr4);
        assertFalse(ResponsesPerQuestion.checkIfAnswersAreCorrect(qrAL, question.getCorrectAnswer()));

    }

}

package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionPoolTest {
    @Test
    public void getQuestionsFromUrlTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        List<Question> questionsFromUrl = qp.getQuestionsFromUrl("./images/demoEquine04.jpg");
        assertTrue(questionsFromUrl.size() == 3);

        List<Question> questionsFromUrl2 = qp.getQuestionsFromUrl("./images/demoEquine02.jpg");
        assertTrue(questionsFromUrl2.size() == 2);

        List<Question> questionsFromUrl3 = qp.getQuestionsFromUrl("notAGoodUrl");
        assertTrue(questionsFromUrl3.size() == 0);
    }

    @Test
    public void getQuestionFromId() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        Question questionFromId = qp.getQuestionFromId("PlaneQ3");
        assertTrue(questionFromId.getQuestionText().equals("On which plane is the ultrasound taken?"));
        assertTrue(questionFromId.getCorrectAnswer().equals("Lateral"));

        Question questionFromId2 = qp.getQuestionFromId("StructureQ4");
        assertTrue(questionFromId2.getQuestionText().equals("What structure is in the near field?"));
        assertTrue(questionFromId2.getCorrectAnswer().equals("bone"));

        //throw exception when id is invalid
        try{
            Question questionFromId3 = qp.getQuestionFromId("NotAnId");
            //qp should throw a RunTimeException
            fail();
        }catch(RuntimeException ee){

        }
    }

    @Test
    public void getQuestionsFromIdsTest() throws IOException {

        //all question ids are good
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        List<String> questionIds1 = Arrays.asList("StructureQ3","StructureQ4","ZoneQ1");
        List<Question> questionList1 = qp.getQuestionsFromIds(questionIds1);
        assertEquals(questionList1.size(), 3);

        //some question ids are invalid
        //TODO: is this try catch structure correct for this test?
        try{
            QuestionPool qp2 = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
            List<String> questionIds2 = Arrays.asList("StructureQ3","StructureQ4","NotAnId", "AlsoNotAnId");
            List<Question> questionList2 = qp2.getQuestionsFromIds(questionIds2);
            assertEquals(questionList2.size(), 2);
        } catch(RuntimeException ee){

        }

        //the order of ids does not match the order of the questions in the Json file
        QuestionPool qp3 = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        List<String> questionIds3 = Arrays.asList("ZoneQ4","StructureQ4","ZoneQ1", "PlaneQ1");
        List<Question> questionList3 = qp3.getQuestionsFromIds(questionIds3);
        assertEquals(questionList3.size(), 4);

        //no Ids in list
        try{
            QuestionPool qp4 = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionsEmpty.json"));
            List<String> questionIds4 = Arrays.asList("IsThisAnId?");
            List<Question> questionList4 = qp4.getQuestionsFromIds(questionIds4);
            assertEquals(questionList4.size(), 0);
        } catch(RuntimeException ee){

        }

    }

    @Test
    public void checkWindowSizeTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        //This qp has 5 of each question

        assertFalse(qp.checkWindowSize(0));
        assertTrue(qp.checkWindowSize(1));
        assertTrue(qp.checkWindowSize(5));
        assertFalse(qp.checkWindowSize(6));
        assertFalse(qp.checkWindowSize(10));

        QuestionPool qp2 = new QuestionPool(new JsonDatastore("src/test/resources/author/DemoQuestionPool.json"));
        //qp2 has 10 plane, 27 struct, and 10 zone

        assertFalse(qp2.checkWindowSize(0));
        assertTrue(qp2.checkWindowSize(5));
        assertTrue(qp2.checkWindowSize(10));
        assertFalse(qp2.checkWindowSize(27));
        assertFalse(qp2.checkWindowSize(100));
    }
}

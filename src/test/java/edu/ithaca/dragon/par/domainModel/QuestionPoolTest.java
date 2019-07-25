package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionPoolTest {

    @Test
    public void createQuestionPoolTest() throws IOException{
        //create QuestionPool with JsonStudentModelDatastore
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        //get all questions and check them
        List<Question> myQPList = myQP.getAllQuestions();
        Assert.assertTrue(myQPList.size() == 15);
    }

    @Test
    public void getQuestionsFromUrlTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        List<Question> questionsFromUrl = qp.getQuestionsFromUrl("./images/demoEquine04.jpg");
        assertTrue(questionsFromUrl.size() == 3);

        List<Question> questionsFromUrl2 = qp.getQuestionsFromUrl("./images/demoEquine02.jpg");
        assertTrue(questionsFromUrl2.size() == 2);

        List<Question> questionsFromUrl3 = qp.getQuestionsFromUrl("notAGoodUrl");
        assertTrue(questionsFromUrl3.size() == 0);
    }

    @Test
    public void getQuestionFromId() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
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

        //Test for follow up Questions
        QuestionPool qp2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        Question followupQuestionFromId = qp2.getQuestionFromId("AttachQ1");
        assertEquals("attachment", followupQuestionFromId.getType());
    }

    @Test
    public void getQuestionsFromIdsTest() throws IOException {

        //all question ids are good
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        List<String> questionIds1 = Arrays.asList("StructureQ3","StructureQ4","ZoneQ1");
        List<Question> questionList1 = qp.getQuestionsFromIds(questionIds1);
        assertEquals(questionList1.size(), 3);

        //some question ids are invalid
        //TODO: is this try catch structure correct for this test?
        try{
            QuestionPool qp2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
            List<String> questionIds2 = Arrays.asList("StructureQ3","StructureQ4","NotAnId", "AlsoNotAnId");
            List<Question> questionList2 = qp2.getQuestionsFromIds(questionIds2);
            assertEquals(questionList2.size(), 2);
        } catch(RuntimeException ee){

        }

        //the order of ids does not match the order of the questions in the Json file
        QuestionPool qp3 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        List<String> questionIds3 = Arrays.asList("ZoneQ4","StructureQ4","ZoneQ1", "PlaneQ1");
        List<Question> questionList3 = qp3.getQuestionsFromIds(questionIds3);
        assertEquals(questionList3.size(), 4);

        //no Ids in list
        try{
            QuestionPool qp4 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionsEmpty.json").getAllQuestions());
            List<String> questionIds4 = Arrays.asList("IsThisAnId?");
            List<Question> questionList4 = qp4.getQuestionsFromIds(questionIds4);
            assertEquals(questionList4.size(), 0);
        } catch(RuntimeException ee){

        }

    }

    @Test
    public void checkWindowSizeTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool3.json").getAllQuestions());
        //This qp has 5 of each question

        assertTrue(qp.checkWindowSize(0));
        assertTrue(qp.checkWindowSize(1));
        assertTrue(qp.checkWindowSize(5));
        assertFalse(qp.checkWindowSize(6));
        assertFalse(qp.checkWindowSize(10));

        QuestionPool qp2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPool2.json").getAllQuestions());
        //qp2 has 10 plane, 27 struct, and 10 zone

        assertTrue(qp2.checkWindowSize(0));
        assertTrue(qp2.checkWindowSize(5));
        assertFalse(qp2.checkWindowSize(10));//TODO:look over
        assertFalse(qp2.checkWindowSize(27));
        assertFalse(qp2.checkWindowSize(100));
    }

    @Test
    public void removeQuestionFromIdTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        qp.removeQuestionById("plane./images/demoEquine14.jpg");
        assertEquals(46, qp.getAllQuestions().size());

        qp.removeQuestionById("AttachQ1");
        assertEquals(46, qp.getAllQuestions().size());
        assertEquals(2, qp.getAllQuestions().get(0).getFollowupQuestions().size());

        qp.removeQuestionById("plane./images/demoEquine14.jpg");
        assertEquals(46, qp.getAllQuestions().size());

        qp.removeQuestionById("structure2./images/demoEquine14.jpg");
        assertEquals(45, qp.getAllQuestions().size());

    }

    @Test
    public void getTopLevelQuestionByIdTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        Question firstQuestion = qp.getQuestionFromId("plane./images/demoEquine14.jpg");
        assertEquals(qp.getAllQuestions().get(0), firstQuestion);

        //repeat
        Question secondQuestion = qp.getTopLevelQuestionById("plane./images/demoEquine14.jpg");
        assertEquals(qp.getAllQuestions().get(0), secondQuestion);

        Question thirdQuestion = qp.getTopLevelQuestionById("structure0./images/demoEquine14.jpg");
        assertEquals(qp.getAllQuestions().get(1), thirdQuestion);

        assertNull(qp.getTopLevelQuestionById("AttachQ1"));
        assertNull(qp.getTopLevelQuestionById("badId"));
    }
}

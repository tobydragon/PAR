package edu.ithaca.dragon.par.deprecated.domainModel;

import edu.ithaca.dragon.par.deprecated.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.domain.Question;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class QuestionPoolTest {

    @Test
    public void createQuestionPoolTest() throws IOException{
        //create QuestionPool with JsonStudentModelDatastore
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());
        //get all questions and check them
        List<Question> myQPList = myQP.getAllQuestions();
        Assert.assertTrue(myQPList.size() == 15);
    }

    @Test
    public void getQuestionsFromUrlTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());
        List<Question> questionsFromUrl = qp.getQuestionsFromUrl("./images/demoEquine04.jpg");
        assertTrue(questionsFromUrl.size() == 3);

        List<Question> questionsFromUrl2 = qp.getQuestionsFromUrl("./images/demoEquine02.jpg");
        assertTrue(questionsFromUrl2.size() == 2);

        List<Question> questionsFromUrl3 = qp.getQuestionsFromUrl("notAGoodUrl");
        assertTrue(questionsFromUrl3.size() == 0);
    }

    @Test
    public void getQuestionFromId() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());
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
        QuestionPool qp2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json").getAllQuestions());
        Question followupQuestionFromId = qp2.getQuestionFromId("AttachQ1");
        assertEquals("attachment", followupQuestionFromId.getType());
    }

    @Test
    public void removeQuestionFromIdTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json").getAllQuestions());
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
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json").getAllQuestions());
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

    @Test
    public void buildQuestionListWithSameUrlImp2Test()throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/DemoQuestionPool.json").getAllQuestions());
        //first url
        Question q = questionPool.getAllQuestions().get(0);
        List<Question> qs = QuestionPool.getTopLevelQuestionsFromUrl(questionPool.getAllQuestions(), q.getImageUrl());
        assertEquals(5, qs.size());

        //second url
        q = questionPool.getAllQuestions().get(5);
        qs = QuestionPool.getTopLevelQuestionsFromUrl(questionPool.getAllQuestions(), q.getImageUrl());
        assertEquals(6, qs.size());

        //third url
        q = questionPool.getAllQuestions().get(11);
        qs = QuestionPool.getTopLevelQuestionsFromUrl(questionPool.getAllQuestions(), q.getImageUrl());
        assertEquals(4, qs.size());

        //fourth url
        q = questionPool.getAllQuestions().get(15);
        qs = QuestionPool.getTopLevelQuestionsFromUrl(questionPool.getAllQuestions(), q.getImageUrl());
        assertEquals(3, qs.size());

        //last question
        q = questionPool.getAllQuestions().get(questionPool.getAllQuestions().size()-1);
        qs = QuestionPool.getTopLevelQuestionsFromUrl(questionPool.getAllQuestions(), q.getImageUrl());
        assertEquals(6, qs.size());
    }
}

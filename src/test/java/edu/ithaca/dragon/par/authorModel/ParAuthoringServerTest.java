package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParAuthoringServerTest {

    private Datastore datastore;
    private Datastore templateDatastore;
    private ParAuthoringServer pas;


    @BeforeEach
    public void setUp() throws IOException {
        Datastore datastore = new JsonDatastore("src/test/resources/author/SampleQuestionsEmpty.json");
        Datastore templateDatastore = new JsonDatastore("src/test/resources/author/DemoQuestionPoolTemplate.json");
        ParAuthoringServer pas = new ParAuthoringServer(datastore, templateDatastore);
    }

    @Test
    public void convertQuestionTemplateToQuestionTest(){
        assertEquals(47, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(0, pas.getQuestionPool().getAllQuestions().size());
        pas.convertQuestionTemplateToQuestion("plane./images/demoEquine14.jpg", "longitudinal");
        assertEquals(46, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(1, pas.getQuestionPool().getAllQuestions().size());

        pas.convertQuestionTemplateToQuestion("structure2./images/demoEquine14.jpg", "Superficial digital flexor tendon");
        assertEquals(45, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(2, pas.getQuestionPool().getAllQuestions().size());

        pas.convertQuestionTemplateToQuestion("zone./images/demoEquine14.jpg", "2");
        assertEquals(44, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(3, pas.getQuestionPool().getAllQuestions().size());

        pas.convertQuestionTemplateToQuestion("AttachQ1", "Type1");
        assertEquals(43, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(4, pas.getQuestionPool().getAllQuestions().size());

        assertThrows(RuntimeException.class, ()->{pas.convertQuestionTemplateToQuestion("badId", "2");});
        assertThrows(RuntimeException.class, ()->{pas.convertQuestionTemplateToQuestion("zone./images/demoEquine14.jpg", "2");});
        assertThrows(RuntimeException.class, ()-> {pas.convertQuestionTemplateToQuestion("zone./images/demoEquine14.jpg", "BadAnswer");});

    }

    @Test
    public void checkIfAnswerIsValidTest(){
        Question question = pas.getQuestionPoolTemplate().getAllQuestions().get(0);
        boolean answerValid = ParAuthoringServer.checkIfAnswerIsValid(question, "longitudinal");
        assertTrue(answerValid);

        answerValid = ParAuthoringServer.checkIfAnswerIsValid(question, "transverse");
        assertTrue(answerValid);

        answerValid = ParAuthoringServer.checkIfAnswerIsValid(question, "badAnswer");
        assertFalse(answerValid);

    }

    @Test
    public void getValidFollowUpQuestionsTest(){
        Question question = pas.getQuestionPoolTemplate().getAllQuestions().get(1);
        List<Question> validFollowUps = ParAuthoringServer.getValidFollowUpQuestions(question);
        assertNull(validFollowUps);
        question.getFollowupQuestions().get(0).setCorrectAnswer("Type1");
        validFollowUps = ParAuthoringServer.getValidFollowUpQuestions(question);
        assertEquals(1, validFollowUps.size());

        question.getFollowupQuestions().get(1).setCorrectAnswer("Type3");
        validFollowUps = ParAuthoringServer.getValidFollowUpQuestions(question);
        assertEquals(2, validFollowUps.size());

        question.getFollowupQuestions().get(0).setCorrectAnswer("badAnswer");
        validFollowUps = ParAuthoringServer.getValidFollowUpQuestions(question);
        assertEquals(1, validFollowUps.size());


    }


    @Test
    public void getOrCreateAuthorModelTest() throws IOException{
        AuthorModel authorModel1 = ParAuthoringServer.getOrCreateAuthorModel();

    }
}

package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParAuthoringServerTest {

    private Datastore datastore;
    private Datastore templateDatastore;
    private ParAuthoringServer pas;


    @BeforeEach
    public void setUp() throws IOException {

        datastore = new JsonDatastore("src/test/resources/author/SampleQuestionsEmpty.json");
        templateDatastore = new JsonDatastore("src/test/resources/author/DemoQuestionPoolTemplate.json");
        pas = new ParAuthoringServer(datastore, templateDatastore);
    }

    @Test
    public void imageTaskResponseSubmittedTest() throws IOException{

        assertEquals(0, pas.getQuestionPool().getAllQuestions().size());
        assertEquals(47, pas.getQuestionPoolTemplate().getAllQuestions().size());

        ImageTaskResponse imageTaskResponse1 = new ImageTaskResponse("User1", Arrays.asList("plane./images/demoEquine14.jpg", "structure0./images/demoEquine14.jpg", "AttachQ1", "AttachQ2"), Arrays.asList("longitudinal", "Superficial digital flexor tendon", "AttachType1", "AttachType2"));
        pas.imageTaskResponseSubmitted(imageTaskResponse1);

        assertEquals(45, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(2, pas.getQuestionPool().getAllQuestions().size());
        assertEquals(2, pas.getQuestionPool().getQuestionFromId("structure0./images/demoEquine14.jpg").getFollowupQuestions().size());
        assertEquals("longitudinal", pas.getQuestionPool().getQuestionFromId("plane./images/demoEquine14.jpg").getCorrectAnswer());

        ImageTaskResponse imageTaskResponse2 = new ImageTaskResponse("User1", Arrays.asList("structure2./images/demoEquine14.jpg", "structure3./images/demoEquine14.jpg", "zone./images/demoEquine14.jpg"), Arrays.asList("Metacarple Bone 3", "Superficial digital flexor tendon", "3a"));
        pas.imageTaskResponseSubmitted(imageTaskResponse2);

        assertEquals(42, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(5, pas.getQuestionPool().getAllQuestions().size());

        pas.imageTaskResponseSubmitted(imageTaskResponse1);
        assertEquals(42, pas.getQuestionPoolTemplate().getAllQuestions().size());
        assertEquals(5, pas.getQuestionPool().getAllQuestions().size());


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
        assertEquals(new ArrayList<>(), validFollowUps);
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
    public void nextImageTaskTemplate(){
        ImageTask imageTask = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());

        ImageTask imageTask2 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask2.getTaskQuestions().size());

        ImageTask imageTask3 = pas.nextImageTaskTemplate();
        assertEquals(4, imageTask3.getTaskQuestions().size());

        ImageTask imageTask4 = pas.nextImageTaskTemplate();
        assertEquals(3, imageTask4.getTaskQuestions().size());

        ImageTask imageTask5 = pas.nextImageTaskTemplate();
        System.out.println(imageTask5.getImageUrl());
    }
}

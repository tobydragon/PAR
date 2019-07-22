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
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        ImageTask imageTask2 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask2.getTaskQuestions().size());
        assertEquals("./images/demoEquine02.jpg", imageTask2.getImageUrl());

        ImageTask imageTask3 = pas.nextImageTaskTemplate();
        assertEquals(4, imageTask3.getTaskQuestions().size());
        assertEquals("./images/demoEquine13.jpg", imageTask3.getImageUrl());

        ImageTask imageTask4 = pas.nextImageTaskTemplate();
        assertEquals(3, imageTask4.getTaskQuestions().size());
        assertEquals("./images/demoEquine04.jpg", imageTask4.getImageUrl());

        ImageTask imageTask5 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask5.getTaskQuestions().size());
        assertEquals("./images/demoEquine10.jpg", imageTask5.getImageUrl());

        ImageTask imageTask6 = pas.nextImageTaskTemplate();
        assertEquals(4, imageTask6.getTaskQuestions().size());
        assertEquals("./images/demoEquine11.jpg", imageTask6.getImageUrl());

        ImageTask imageTask7 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask7.getTaskQuestions().size());
        assertEquals("./images/demoEquine05.jpg", imageTask7.getImageUrl());

        ImageTask imageTask8 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask8.getTaskQuestions().size());
        assertEquals("./images/demoEquine09.jpg", imageTask8.getImageUrl());

        ImageTask imageTask9 = pas.nextImageTaskTemplate();
        assertEquals(3, imageTask9.getTaskQuestions().size());
        assertEquals("./images/demoEquine37.jpg", imageTask9.getImageUrl());

        ImageTask imageTask10 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask10.getTaskQuestions().size());
        assertEquals("./images/demoEquine32.jpg", imageTask10.getImageUrl());

        //At this point, every single question has been seen. The system successfully loops.

        ImageTask imageTask11 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask11.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask11.getImageUrl());

        ImageTask imageTask12 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask12.getTaskQuestions().size());
        assertEquals("./images/demoEquine02.jpg", imageTask12.getImageUrl());
    }

    @Test
    public void endOfTasksTest() throws IOException{
        datastore = new JsonDatastore("src/test/resources/author/SampleQuestionsEmpty.json");
        templateDatastore = new JsonDatastore("src/test/resources/author/DemoQuestionPoolTemplateSmall.json");
        pas = new ParAuthoringServer(datastore, templateDatastore);

        ImageTask imageTask = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());
        assertEquals(5, imageTask.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponse("Author1", Arrays.asList("plane./images/demoEquine14.jpg", "structure0./images/demoEquine14.jpg", "AttachQ1", "AttachQ2"), Arrays.asList("longitudinal", "Superficial digital flexor endon", "Type1", "Type2")));

        ImageTask imageTask2 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine04.jpg", imageTask2.getImageUrl());
        assertEquals(3, imageTask2.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponse("Author1", Arrays.asList("plane./images/demoEquine04.jpg", "structure0./images/demoEquine04.jpg", "AttachQ5", "zone./images/demoEquine04.jpg"), Arrays.asList("longitudinal", "Superficial digital flexor endon", "Type1", "3c")));

        ImageTask imageTask3 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask3.getImageUrl());
        assertEquals(3, imageTask3.getTaskQuestions().size());

        ImageTask imageTask4 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask4.getImageUrl());
        assertEquals(3, imageTask3.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponse("Author1", Arrays.asList("structure2./images/demoEquine14.jpg","structure3./images/demoEquine14.jpg", "zone./images/demoEquine14.jpg"), Arrays.asList("Suspensory ligament (branches)", "Proximal sesamoid bones", "1A")));

        //no more template questions
        ImageTask imageTask5 = pas.nextImageTaskTemplate();
        assertEquals("noMoreQuestions", imageTask5.getImageUrl());
        assertEquals(0, imageTask5.getTaskQuestions().size());
    }

}

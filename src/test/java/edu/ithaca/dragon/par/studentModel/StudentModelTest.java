package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentModelTest {

    private StudentModel studentModel;
    private QuestionPool questionPool;
    private List<ImageTaskResponse> responsesFromFile;

    @BeforeEach
    public void setUp() throws IOException{
        questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions.json"));
        studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);

    }

    @AfterEach
    public void tearDown() throws IOException{
        studentModel = null;
        questionPool = null;
    }

    @Test
    public void constructorTest() throws IOException {
        assertEquals("TestUser1", studentModel.getUserId());
        assertEquals(3, studentModel.getUnseenQuestionCount());
        assertEquals(0, studentModel.getSeenQuestionCount());
        assertEquals(0, studentModel.getResponseCount());
    }

    @Test//make a card TODO:CHANGE TEST NAME AND STUDENT MODEL .getResponseCount
    public void testPickingQuestionsAndReceivingResponses(){
        studentModel.givenQuestion("PlaneQ1");
        studentModel.givenQuestion("PlaneQ1");
        studentModel.givenQuestion("ZoneQ1");
        assertEquals(2, studentModel.getSeenQuestionCount());
        assertEquals(1, studentModel.getUnseenQuestionCount());

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);

        assertEquals(3, studentModel.getResponseCount());
    }

    @Test
    public void imageTaskResponseSubmittedTest() throws IOException{
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);
        assertEquals(studentModel.getResponseCount(), 3);
    }
}

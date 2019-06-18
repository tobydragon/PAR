package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentModelTest {

    StudentModel studentModel;
    @Test
    public void imageTaskResponseSubmittedTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions.json"));
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),qp);
        assertEquals(studentModel.getResponseCount(), 3);
    }
    @BeforeEach
    public void setUp() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        studentModel = new StudentModel("TestUser1", questionsFromFile);
    }

    @AfterEach
    public void tearDown() throws IOException{
        studentModel = null;
    }

    @Test
    public void constructorTest() throws IOException {
        assertEquals("TestUser1", studentModel.getUserId());
        assertEquals(3, studentModel.getUnseenQuestionCount());
        assertEquals(0, studentModel.getSeenQuestionCount());
        assertEquals(0, studentModel.getResponseCount());
    }
}

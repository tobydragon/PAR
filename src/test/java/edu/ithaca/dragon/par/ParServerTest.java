package edu.ithaca.dragon.par;


import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParServerTest {

    @Test
    public void getOrCreateStudentModelTest() throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions.json"));
        StudentModel sm1 = new StudentModel("s1", questionPool.getAllQuestions().subList(0, 1));
        StudentModel sm2 = new StudentModel("s2", questionPool.getAllQuestions().subList(0, 2));
        StudentModel sm3 = new StudentModel("s3", questionPool.getAllQuestions());

        Map<String, StudentModel> smMap = new HashMap<>();
        smMap.put("s1", sm1);
        smMap.put("s2", sm2);
        smMap.put("s3", sm3);

        StudentModel studentModel = ParServer.getOrCreateStudentModel(smMap, "s3", questionPool);
        assertEquals("s3", studentModel.getUserId());
        assertEquals(3, studentModel.getUnseenQuestionCount());

        studentModel = ParServer.getOrCreateStudentModel(smMap, "s1", questionPool);
        assertEquals("s1", studentModel.getUserId());
        assertEquals(1, studentModel.getUnseenQuestionCount());

        //check adding one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s4", questionPool);
        assertEquals("s4", studentModel.getUserId());
        assertEquals(3, studentModel.getUnseenQuestionCount());
        assertEquals(4, smMap.size());

        //check an old one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s2", questionPool);
        assertEquals("s2", studentModel.getUserId());
        assertEquals(2, studentModel.getUnseenQuestionCount());

        //check getting the added one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s4", questionPool);
        assertEquals("s4", studentModel.getUserId());
        assertEquals(3, studentModel.getUnseenQuestionCount());
        assertEquals(4, smMap.size());

        //check adding another one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s5", questionPool);
        assertEquals("s5", studentModel.getUserId());
        assertEquals(3, studentModel.getUnseenQuestionCount());
        assertEquals(5, smMap.size());
    }

    @Test
    public void nextImageTaskTest(){
        fail("not implemented yet");
    }

    @Test
    public void imageTaskResponseSubmittedTest(){
        fail("not implemented yet");
    }


}

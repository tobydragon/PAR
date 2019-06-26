package edu.ithaca.dragon.par;


import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParServerTest {

    @Test
    public void getOrCreateStudentModelTest() throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        StudentModel sm1 = new StudentModel("s1", questionPool.getAllQuestions().subList(0, 1));
        StudentModel sm2 = new StudentModel("s2", questionPool.getAllQuestions().subList(0, 2));
        StudentModel sm3 = new StudentModel("s3", questionPool.getAllQuestions());

        Map<String, StudentModel> smMap = new HashMap<>();
        smMap.put("s1", sm1);
        smMap.put("s2", sm2);
        smMap.put("s3", sm3);

        StudentModel studentModel = ParServer.getOrCreateStudentModel(smMap, "s3", questionPool);
        assertEquals("s3", studentModel.getUserId());
        assertEquals(15, studentModel.getUnseenQuestionCount());

        studentModel = ParServer.getOrCreateStudentModel(smMap, "s1", questionPool);
        assertEquals("s1", studentModel.getUserId());
        assertEquals(1, studentModel.getUnseenQuestionCount());

        //check adding one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s4", questionPool);
        assertEquals("s4", studentModel.getUserId());
        assertEquals(15, studentModel.getUnseenQuestionCount());
        assertEquals(4, smMap.size());

        //check an old one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s2", questionPool);
        assertEquals("s2", studentModel.getUserId());
        assertEquals(2, studentModel.getUnseenQuestionCount());

        //check getting the added one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s4", questionPool);
        assertEquals("s4", studentModel.getUserId());
        assertEquals(15, studentModel.getUnseenQuestionCount());
        assertEquals(4, smMap.size());

        //check adding another one
        studentModel = ParServer.getOrCreateStudentModel(smMap, "s5", questionPool);
        assertEquals("s5", studentModel.getUserId());
        assertEquals(15, studentModel.getUnseenQuestionCount());
        assertEquals(5, smMap.size());
    }

    @Test
    public void nextImageTaskSingleTest() throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionsSameDifficulty.json"));
        ParServer parServer = new ParServer(questionPool);
        ImageTask nextTask = parServer.nextImageTaskSingle("s1");
        ImageTask intendedFirstTask = JsonUtil.fromJsonFile("src/test/resources/author/SampleImageTaskSingleQuestion.json", ImageTask.class);
        assertEquals(intendedFirstTask, nextTask);

        nextTask = parServer.nextImageTaskSingle("s2");
        assertEquals(intendedFirstTask, nextTask);

        nextTask = parServer.nextImageTaskSingle("s1");
        assertNotNull(nextTask);
        nextTask = parServer.nextImageTaskSingle("s1");
        ImageTask intendedLastTask = JsonUtil.fromJsonFile("src/test/resources/author/SampleImageTaskSingleQuestion3.json", ImageTask.class);
        assertEquals(intendedLastTask, nextTask);

        nextTask = parServer.nextImageTaskSingle("s2");
        assertNotNull(nextTask);

        nextTask = parServer.nextImageTaskSingle("s2");
        assertEquals(intendedLastTask, nextTask);
    }

    @Test
    public void nextImageTaskTest() throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        ParServer parServer = new ParServer(questionPool);
        ImageTask nextTask = parServer.nextImageTask("s1");
        ImageTask intendedFirstTask = JsonUtil.fromJsonFile("src/test/resources/author/nextImageTaskTest1.json", ImageTask.class);
        assertEquals(intendedFirstTask, nextTask);

        nextTask = parServer.nextImageTask("s2");
        assertEquals(intendedFirstTask, nextTask);

        nextTask = parServer.nextImageTask("s1");
        assertNotNull(nextTask);
        ImageTask intendedLastTask = JsonUtil.fromJsonFile("src/test/resources/author/nextImageTaskTest2.json", ImageTask.class);
        assertEquals(intendedLastTask, nextTask);

        nextTask = parServer.nextImageTask("s2");
        assertNotNull(nextTask);
        assertEquals(intendedLastTask, nextTask);

    }

    @Test
    public void imageTaskResponseSubmittedAndCalcScoreTest() throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        ParServer parServer = new ParServer(questionPool);
        ImageTaskResponse responseSet2=new ImageTaskResponse("response1", Arrays.asList("PlaneQ1", "PlaneQ2", "PlaneQ3", "PlaneQ4", "PlaneQ5", "StructureQ1", "StructureQ2", "StructureQ3", "StructureQ4", "StructureQ5", "ZoneQ1", "ZoneQ2", "ZoneQ3", "ZoneQ4", "ZoneQ5"),Arrays.asList("Latera", "Transvers", "Latera", "Latera", "Transvers", "bone", "ligament", "tendon", "bone", "tumor", "3c", "1b", "3c", "2a", "2b"));
        ImageTaskResponse responseSet3=new ImageTaskResponse("response1", Arrays.asList("PlaneQ1", "PlaneQ2", "PlaneQ3", "PlaneQ4", "PlaneQ5", "StructureQ1", "StructureQ2", "StructureQ3", "StructureQ4", "StructureQ5", "ZoneQ1", "ZoneQ2", "ZoneQ3", "ZoneQ4", "ZoneQ5"),Arrays.asList("I'm","bad","student","I'm","bad","student","I'm","bad","student","I'm","bad","student","I'm","bad","student"));
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);


        //star student
        for(int i=0;i<5;i++){
            parServer.imageTaskResponseSubmitted(responsesFromFile.get(0),"s1");
        }   assertEquals(100.0,parServer.calcScore("s1"), DataUtil.OK_DOUBLE_MARGIN);


        //great student
        parServer.imageTaskResponseSubmitted(responseSet2,"s2");
        assertEquals(66.666666,parServer.calcScore("s2"),DataUtil.OK_DOUBLE_MARGIN);
        for(int i=0;i<4;i++){
            parServer.imageTaskResponseSubmitted(responsesFromFile.get(0),"s2");

        }   assertEquals(93.3333333,parServer.calcScore("s2"), DataUtil.OK_DOUBLE_MARGIN);


        //terrible student
        parServer.imageTaskResponseSubmitted(responsesFromFile.get(0),"s3");
        parServer.imageTaskResponseSubmitted(responsesFromFile.get(0),"s3");
        assertEquals(100.0,parServer.calcScore("s3"), DataUtil.OK_DOUBLE_MARGIN);
        for(int i=0;i<3;i++){
            parServer.imageTaskResponseSubmitted(responseSet3,"s3");
        }    assertEquals(39.999999,parServer.calcScore("s3"), DataUtil.OK_DOUBLE_MARGIN);


    }
}

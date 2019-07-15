package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class StudentModelTest {

    private StudentModel studentModel;
    private QuestionPool questionPool;
    private List<ImageTaskResponse> responsesFromFile;

    @BeforeEach
    public void setUp() throws IOException{
        questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
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
        assertEquals(15, studentModel.getUnseenQuestionCount());
        assertEquals(0, studentModel.getSeenQuestionCount());
        assertEquals(0, studentModel.getResponseCount());
    }

    @Test
    public void testPickingQuestionsAndReceivingResponses(){
        studentModel.givenQuestion("PlaneQ1");
        studentModel.givenQuestion("PlaneQ1");
        studentModel.givenQuestion("ZoneQ1");
        assertEquals(2, studentModel.getSeenQuestionCount());
        assertEquals(13, studentModel.getUnseenQuestionCount());

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);

        assertEquals(15, studentModel.getResponseCount());
    }

    @Test
    public void knowledgeScoreByTypeTest(){
        Map<String,Double> resp=studentModel.knowledgeScoreByType();
        assertEquals(0,resp.size());

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(1),questionPool);

        Map<String,Double> resp1=studentModel.knowledgeScoreByType();
        assertEquals(3,resp1.size());

        assertEquals(50.0,resp1.get(EquineQuestionTypes.plane.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(100.0,resp1.get(EquineQuestionTypes.structure.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(0,resp1.get(EquineQuestionTypes.zone.toString()),DataUtil.OK_DOUBLE_MARGIN);


    }

    @Test
    public void imageTaskResponseSubmittedTest() throws IOException{
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);
        assertEquals(studentModel.getResponseCount(), 15);
    }
/*
    @Test
    public void calcLevelTest() {
        //throws exception when the types are invalid
        Map<String, Double> m1 = new HashMap<>();
        m1.put("Plane", 1.1);
        m1.put("NotAValidKey", -1.0);


        //Student doesn't know about anything
        Map<String, Double> m2 = new HashMap<>();
        m2.put("Plane", 0.0);
        m2.put("Structure", -1.0);
        m2.put("Attachment", -1.0);
        m2.put("Zone", -1.0);
        assertEquals(1, StudentModel.calcLevel(m2));

        //Student knows alot about upper level topics, still should be level 1
        //if this happens somethings broken/throw exception
        Map<String, Double> m3 = new HashMap<>();
        m3.put("Plane", 0.0);
        m3.put("Structure", 0.0);
        m3.put("Attachment", 90.0);
        m3.put("Zone", 100.0);
        assertEquals(1, StudentModel.calcLevel(m3));

        //Student knows Plane but nothing else
        Map<String, Double> m4 = new HashMap<>();
        m4.put("Plane", 75.0);
        m4.put("Structure", 20.0);
        m4.put("Attachment", -1.0);
        m4.put("Zone", -1.0);
        assertEquals(2, StudentModel.calcLevel(m4));

        //Student knows Plane and Structure
        Map<String, Double> m5 = new HashMap<>();
        m5.put("Plane", 90.0);
        m5.put("Structure", 20.0);
        m5.put("Attachment", 30.0);
        m5.put("Zone", 10.0);
        assertEquals(2, StudentModel.calcLevel(m5));

        //Student didnt answer any questions
        Map<String, Double> m6 = new HashMap<>();
        m6.put("Plane", -1.0);
        m6.put("Structure", -1.0);
        m6.put("Attachment", -1.0);
        m6.put("Zone", -1.0);
        assertEquals(1, StudentModel.calcLevel(m6));

        Map<String, Double> m7 = new HashMap<>();
        m7.put("Plane", 75.0);
        m7.put("Structure", 75.0);
        m7.put("Attachment", -1.0);
        m7.put("Zone", -1.0);
        assertEquals(3, StudentModel.calcLevel(m5));
    }*/

    @Test
    public void calcLevelTest2() {
        //throws exception when the types are invalid
        try{
            Map<String, Double> m1 = new HashMap<>();
            // m1.put(EquineQuestionTypes.plane.toString(), 1.1);
            m1.put("NotValidKey", -1.0);
        }
        catch(RuntimeException ee){
        }

        //Student doesn't know about anything
        Map<String, Double> m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 0.0);
        m2.put(EquineQuestionTypes.structure.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(1, StudentModel.calcLevel(m2));

        //Student knows a lot about upper level topics, still should be level 1
        Map<String, Double> m3 = new HashMap<>();
        m3.put(EquineQuestionTypes.plane.toString(), 0.0);
        m3.put(EquineQuestionTypes.structure.toString(), 0.0);
        m3.put(EquineQuestionTypes.zone.toString(), 100.0);
        assertEquals(1, StudentModel.calcLevel(m3));

        Map<String, Double> m4 = new HashMap<>();
        m4.put(EquineQuestionTypes.plane.toString(), 75.0);
        m4.put(EquineQuestionTypes.structure.toString(), 20.0);
        m4.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(2, StudentModel.calcLevel(m4));

        Map<String, Double> m5 = new HashMap<>();
        m5.put(EquineQuestionTypes.plane.toString(), 75.0);
        m5.put(EquineQuestionTypes.structure.toString(), 20.0);
        m5.put(EquineQuestionTypes.zone.toString(), .0);
        assertEquals(2, StudentModel.calcLevel(m5));

        //Student didnt answer any questions
        Map<String, Double> m6 = new HashMap<>();
        m6.put(EquineQuestionTypes.plane.toString(), -1.0);
        m6.put(EquineQuestionTypes.structure.toString(), -1.0);
        m6.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(1, StudentModel.calcLevel(m6));

        Map<String, Double> m7 = new HashMap<>();
        m7.put(EquineQuestionTypes.plane.toString(), 100.0);
        m7.put(EquineQuestionTypes.structure.toString(), 74.0);
        m7.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(3, StudentModel.calcLevel(m7));

        Map<String, Double> m8 = new HashMap<>();
        m8.put(EquineQuestionTypes.plane.toString(), 100.0);
        m8.put(EquineQuestionTypes.structure.toString(), 100.0);
        m8.put(EquineQuestionTypes.zone.toString(), 75.0);
        assertEquals(6, StudentModel.calcLevel(m8));

        Map<String, Double> m9 = new HashMap<>();
        m9.put(EquineQuestionTypes.plane.toString(), 100.0);
        m9.put(EquineQuestionTypes.structure.toString(), 100.0);
        m9.put(EquineQuestionTypes.zone.toString(), 100.0);
        assertEquals(6, StudentModel.calcLevel(m9));

        Map<String, Double> m10 = new HashMap<>();
        m10.put(EquineQuestionTypes.plane.toString(), 100.0);
        m10.put(EquineQuestionTypes.structure.toString(), 20.0);
        m10.put(EquineQuestionTypes.zone.toString(),-1.0);
        assertEquals(3, StudentModel.calcLevel(m10));

        Map<String, Double> m11 = new HashMap<>();
        m11.put(EquineQuestionTypes.plane.toString(), 100.0);
        m11.put(EquineQuestionTypes.structure.toString(),100.0);
        m11.put(EquineQuestionTypes.zone.toString(),-1.0);
        assertEquals(6, StudentModel.calcLevel(m11));

    }
}

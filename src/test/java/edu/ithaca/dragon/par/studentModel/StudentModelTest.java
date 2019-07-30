package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentModelTest {

    private StudentModel studentModel;
    private QuestionPool questionPool;
    private List<ImageTaskResponse> responsesFromFile;

    @BeforeEach
    public void setUp() throws IOException{
        questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
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
        Map<String,Double> resp=studentModel.knowledgeScoreByType(4);
        assertEquals(4,resp.size());

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(1),questionPool);

        Map<String,Double> resp1=studentModel.knowledgeScoreByType(4);
        assertEquals(4,resp1.size());

        assertEquals(50.0,resp1.get(EquineQuestionTypes.plane.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(100.0,resp1.get(EquineQuestionTypes.structure.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(0,resp1.get(EquineQuestionTypes.zone.toString()),DataUtil.OK_DOUBLE_MARGIN);

    }

    @Test
    public void imageTaskResponseSubmittedTest() throws IOException{
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);
        assertEquals(studentModel.getResponseCount(), 15);
    }

    @Test
    public void calcLevelTest() {
        //throws exception when the types are invalid
        try{
            Map<String, Double> m1 = new HashMap<>();
            // m1.put(EquineQuestionTypes.plane.toString(), 1.1);
            m1.put("NotValidKey", -1.0);
        }
        catch(RuntimeException ee){
        }
        
        Map<String, Double> m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 0.0);
        m2.put(EquineQuestionTypes.structure.toString(), 0.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 90.0);
        m2.put(EquineQuestionTypes.zone.toString(), 100.0);
        assertEquals(1, StudentModel.calcLevel(m2));

        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), -1.0);
        m2.put(EquineQuestionTypes.structure.toString(), -1.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(1, StudentModel.calcLevel(m2));
        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 75.0);
        m2.put(EquineQuestionTypes.structure.toString(), 20.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(2, StudentModel.calcLevel(m2));


        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 75.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 30.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(2, StudentModel.calcLevel(m2));


        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 75.0);
        m2.put(EquineQuestionTypes.structure.toString(), 75.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(2, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 74.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(3, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 75.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(4, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 75.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 75.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(4, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(5, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 34.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(5, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 75.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(6, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 75.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(6, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 99.0);
        m2.put(EquineQuestionTypes.zone.toString(), 23.0);
        assertEquals(6, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 100.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(7, StudentModel.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 100.0);
        m2.put(EquineQuestionTypes.zone.toString(), 75.0);
        assertEquals(7, StudentModel.calcLevel(m2));

    }

    @Test
    public void addQuestionTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        StudentModel studentModel = new StudentModel("TestUser1", questions);
        Question q = new Question("Question1", "What is this question?", "Good", "A very good one", Arrays.asList("A very good one", "A great one", ":("), "/images/AnImage");
        studentModel.addQuestion(q);
        assertEquals(48, studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().size());
        assertEquals(q, studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().get(47));


        Question q2 = new Question("Question2", "What is a question?", "Question", "Something important", Arrays.asList("Something important", ":)", ">:/"), "/images/aBetterImage");
        studentModel.addQuestion(q2);
        assertEquals(49, studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().size());
        assertEquals(q2, studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().get(48));

        //TODO: see implementation for comment
        //assertThrows(RuntimeException.class, ()->{studentModel.addQuestion(q2);});

    }
}

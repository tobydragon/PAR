package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentModelTest {

    private StudentModel studentModel;
    private QuestionPool questionPool;
    private List<ImageTaskResponseOOP> responsesFromFile;

    @BeforeEach
    public void setUp() throws IOException{
        questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponseOOP.class);
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
        studentModel.increaseTimesSeen("PlaneQ1");
        studentModel.increaseTimesSeen("PlaneQ1");
        studentModel.increaseTimesSeen("ZoneQ1");
        assertEquals(2, studentModel.getSeenQuestionCount());
        assertEquals(13, studentModel.getUnseenQuestionCount());

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool, 4);
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool, 4);

        assertEquals(15, studentModel.getResponseCount());
    }

    @Test
    public void knowledgeScoreByTypeTest(){
        Map<String,Double> resp=studentModel.calcKnowledgeEstimateByType(4);
        assertEquals(4,resp.size());

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(1),questionPool, 4);

        Map<String,Double> resp1=studentModel.calcKnowledgeEstimateByType(4);
        assertEquals(4,resp1.size());

        assertEquals(50.0,resp1.get(EquineQuestionTypes.plane.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(100.0,resp1.get(EquineQuestionTypes.structure.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(0,resp1.get(EquineQuestionTypes.zone.toString()),DataUtil.OK_DOUBLE_MARGIN);

    }

    @Test
    public void imageTaskResponseSubmittedTest() throws IOException{
        // submit responses to 15 questions
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool, 4);
        assertEquals(15, studentModel.getResponseCount());
        assertEquals(15, studentModel.countTotalResponsesFromUserResponseSet());
        assertEquals( 1, studentModel.getPreviousLevel());
        assertEquals( 5, studentModel.getCurrentLevel());

        // submit 15 new responses to the 15 questions that have already been responded to
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(1),questionPool, 4);
        assertEquals(15, studentModel.getResponseCount());
        assertEquals(30, studentModel.countTotalResponsesFromUserResponseSet());
        assertEquals(5, studentModel.getPreviousLevel());
        assertEquals( 1, studentModel.getCurrentLevel());
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

    @Test
    public void getAndSetPreviousLevelTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        StudentModel studentModel = new StudentModel("TestUser", questions);
        assertEquals(1, studentModel.getPreviousLevel());

        //set once
        studentModel.setPreviousLevel(5);
        assertEquals(5, studentModel.getPreviousLevel());

        //invalid set- too high
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setPreviousLevel(19));
        assertEquals(5, studentModel.getPreviousLevel());

        //invalid set- too high (border)
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setPreviousLevel(8));
        assertEquals(5, studentModel.getPreviousLevel());

        //invalid set- too low
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setPreviousLevel(-3));
        assertEquals(5, studentModel.getPreviousLevel());

        //invalid set- too low (border)
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setPreviousLevel(0));
        assertEquals(5, studentModel.getPreviousLevel());

        //set again
        studentModel.setPreviousLevel(6);
        assertEquals(6, studentModel.getPreviousLevel());

        //new studentModel
        StudentModel studentModel2 = new StudentModel("TestUser2", questions);
        studentModel2.setPreviousLevel(3);
        assertEquals(3, studentModel2.getPreviousLevel());
        assertEquals(6, studentModel.getPreviousLevel());

        //set old one again
        studentModel.setPreviousLevel(1);
        assertEquals(3, studentModel2.getPreviousLevel());
        assertEquals(1, studentModel.getPreviousLevel());
    }

    @Test
    public void getAndSetCurrentLevelTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        StudentModel studentModel = new StudentModel("TestUser", questions);
        assertEquals(1, studentModel.getCurrentLevel());

        //set once
        studentModel.setCurrentLevel(5);
        assertEquals(5, studentModel.getCurrentLevel());

        //invalid set- too high
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setCurrentLevel(19));
        assertEquals(5, studentModel.getCurrentLevel());

        //invalid set- too high (border)
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setCurrentLevel(8));
        assertEquals(5, studentModel.getCurrentLevel());

        //invalid set- too low
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setCurrentLevel(-3));
        assertEquals(5, studentModel.getCurrentLevel());

        //invalid set- too low (border)
        assertThrows(IllegalArgumentException.class, ()-> studentModel.setCurrentLevel(0));
        assertEquals(5, studentModel.getCurrentLevel());

        //set again
        studentModel.setCurrentLevel(6);
        assertEquals(6, studentModel.getCurrentLevel());

        //new studentModel
        StudentModel studentModel2 = new StudentModel("TestUser2", questions);
        studentModel2.setCurrentLevel(3);
        assertEquals(3, studentModel2.getCurrentLevel());
        assertEquals(6, studentModel.getCurrentLevel());

        //set old one again
        studentModel.setCurrentLevel(1);
        assertEquals(3, studentModel2.getCurrentLevel());
        assertEquals(1, studentModel.getCurrentLevel());
    }
}

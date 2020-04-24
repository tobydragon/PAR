package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.StudentData;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);

        assertEquals(15, studentModel.getResponseCount());
    }

    @Test
    public void knowledgeScoreByTypeTest(){
        Map<String,Double> resp=studentModel.calcKnowledgeEstimateByType(4);
        assertEquals(4,resp.size());

        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(1),questionPool);

        Map<String,Double> resp1=studentModel.calcKnowledgeEstimateByType(4);
        assertEquals(4,resp1.size());

        assertEquals(50.0,resp1.get(EquineQuestionTypes.plane.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(100.0,resp1.get(EquineQuestionTypes.structure.toString()),DataUtil.OK_DOUBLE_MARGIN);
        assertEquals(0,resp1.get(EquineQuestionTypes.zone.toString()),DataUtil.OK_DOUBLE_MARGIN);

    }

    @Test
    public void imageTaskResponseSubmittedTest() throws IOException{
        // submit responses to 15 questions
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(0),questionPool);
        assertEquals(15, studentModel.getResponseCount());
        assertEquals(15, studentModel.countTotalResponsesFromUserResponseSet());

        // submit 15 new responses to the 15 questions that have already been responded to
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(1),questionPool);
        assertEquals(15, studentModel.getResponseCount());
        assertEquals(30, studentModel.countTotalResponsesFromUserResponseSet());
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
    public void calcPercentAnswersCorrectTest() throws IOException{
        //brand new student
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        assertEquals("student", newStudent.getStudentId());
        assertTrue(newStudent.getPercentAnswersCorrect() < 0.0);


        //mastered student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getStudentId());
        assertTrue(91.9 < masteredStudent.getPercentAnswersCorrect() && masteredStudent.getPercentAnswersCorrect() < 92.1);

        //level 4 student
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        assertEquals("level4Student", level4StudentData.getStudentId());
        assertTrue(99.9 < level4StudentData.getPercentAnswersCorrect() && level4StudentData.getPercentAnswersCorrect() < 101.1);

    }
}

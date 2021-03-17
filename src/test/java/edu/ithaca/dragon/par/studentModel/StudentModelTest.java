package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.StudentData;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentModelTest {

    private StudentModel studentModel;
    private QuestionPool questionPool;
    private List<ImageTaskResponseOOP> responsesFromFile;

    @BeforeEach
    public void setUp() throws IOException{
        questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());
        studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/responses/SampleResponses.json", ImageTaskResponseOOP.class);
    }

    @AfterEach
    public void tearDown() throws IOException{
        studentModel = null;
        questionPool = null;
    }

    @Test
    public void constructorTest() throws IOException {
        assertEquals("TestUser1", studentModel.getUserId());
        assertEquals(15, studentModel.getUnattemptedQuestionCount());
        assertEquals(0, studentModel.getAttemptedQuestionCount());
        assertEquals(0, studentModel.getResponseCount());
    }

    @Test
    public void testPickingQuestionsAndReceivingResponses(){
        studentModel.increaseTimesAttempted("PlaneQ1");
        studentModel.increaseTimesAttempted("PlaneQ1");
        studentModel.increaseTimesAttempted("ZoneQ1");
        assertEquals(2, studentModel.getAttemptedQuestionCount());
        assertEquals(13, studentModel.getUnattemptedQuestionCount());

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
        studentModel.setCurrentLevel(LevelTaskGenerator.calcLevel(studentModel.calcKnowledgeEstimateByType(4)));
        assertEquals( 4, studentModel.getCurrentLevel());

        // submit 15 new responses to the 15 questions that have already been responded to
        studentModel.imageTaskResponseSubmitted(responsesFromFile.get(1),questionPool, 4);
        assertEquals(15, studentModel.getResponseCount());
        assertEquals(30, studentModel.countTotalResponsesFromUserResponseSet());
        assertEquals(4, studentModel.getPreviousLevel());
        studentModel.setCurrentLevel(LevelTaskGenerator.calcLevel(studentModel.calcKnowledgeEstimateByType(4)));
        assertEquals( 1, studentModel.getCurrentLevel());
    }
    @Test
    public void addQuestionTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        StudentModel studentModel = new StudentModel("TestUser1", questions);
        Question q = new Question("Question1", "What is this question?", "Good", "A very good one", Arrays.asList("A very good one", "A great one", ":("), "/images/AnImage");
        studentModel.addQuestion(q);
        assertEquals(48, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().size());
        assertEquals(q, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().get(47));


        Question q2 = new Question("Question2", "What is a question?", "Question", "Something important", Arrays.asList("Something important", ":)", ">:/"), "/images/aBetterImage");
        studentModel.addQuestion(q2);
        assertEquals(49, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().size());
        assertEquals(q2, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().get(48));

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
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getStudentId());
        assertTrue(88.4 < masteredStudent.getPercentAnswersCorrect() && masteredStudent.getPercentAnswersCorrect() < 88.5);

        //level 4 student
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        assertEquals("level4Student", level4StudentData.getStudentId());
        assertTrue(99.9 < level4StudentData.getPercentAnswersCorrect() && level4StudentData.getPercentAnswersCorrect() < 101.1);

    }


    @Test
    public void calcQuestionsWrongTest() throws IOException{
        //mastered student creation
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        List<QuestionCount> qc =  masteredStudentModel.calcQuestionsWrong();
        assertEquals(3, qc.size());


        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/incorrectStudent.json", StudentModelRecord.class);
        StudentModel insm = smr2.buildStudentModel(myQP);

        qc = insm.calcQuestionsWrong();
        assertEquals(5, qc.size());
    }

    @Test
    public void getAndSetPreviousLevelTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        StudentModel studentModel = new StudentModel("TestUser", questions);
        assertEquals(1, studentModel.getPreviousLevel());

        //set once
        studentModel.setPreviousLevel(5);
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
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        StudentModel studentModel = new StudentModel("TestUser", questions);
        assertEquals(1, studentModel.getCurrentLevel());

        //set once
        studentModel.setCurrentLevel(5);
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

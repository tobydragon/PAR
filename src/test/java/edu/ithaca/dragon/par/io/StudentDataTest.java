package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDataTest {

    @Test
    public void constructorTest() throws IOException {
        //brand new student
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        assertEquals("student", newStudent.getStudentId());
        assertEquals(1, newStudent.getLevel());
        assertEquals(0, newStudent.getTotalAnswersGiven());
        assertTrue(newStudent.getPercentAnswersCorrect() < 0.0);
        assertEquals(-1.0, newStudent.getPercentWrongFirstTime());


        //mastered student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getStudentId());
        assertEquals(7, masteredStudent.getLevel());
        assertEquals(26, masteredStudent.getTotalAnswersGiven());
        assertTrue(88.4 < masteredStudent.getPercentAnswersCorrect() && masteredStudent.getPercentAnswersCorrect() <88.5);
        assertEquals(17.65, masteredStudent.getPercentWrongFirstTime());
        assertEquals(33.33, masteredStudent.getPercentRightAfterWrongFirstTime());

        //level 4 student
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        assertEquals("level4Student", level4StudentData.getStudentId());
        assertEquals(4, level4StudentData.getLevel());
        assertEquals(11, level4StudentData.getTotalAnswersGiven());
        assertTrue(99.9 < level4StudentData.getPercentAnswersCorrect() && level4StudentData.getPercentAnswersCorrect() < 101.1);
        assertEquals(0.0, level4StudentData.getPercentWrongFirstTime());
        assertEquals(0, level4StudentData.getPercentRightAfterWrongFirstTime());
    }

    @Test
    public void updateDataTest() throws IOException{
        //mastered student creation
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getStudentId());
        assertEquals(7, masteredStudent.getLevel());
        assertEquals(26, masteredStudent.getTotalAnswersGiven());

        //add responses
        ResponsesPerQuestion r1 = masteredStudentModel.getUserResponseSet().getResponsesPerQuestionList().get(4);
        r1.addNewResponse("uhhhhh");
        r1.addNewResponse("idk the answer");
        r1.addNewResponse("totally clueless!");

        //change level and answersGiven
        masteredStudent.updateData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getStudentId());
        //assertEquals(6, masteredStudent.getLevel());
        assertEquals(29, masteredStudent.getTotalAnswersGiven());

        //wrong student model, IllegalArgumentException
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        assertThrows(IllegalArgumentException.class, ()-> masteredStudent.updateData(student));
    }

    @Test
    public void calcWrongQuestionsTest() throws IOException{
        //mastered student creation
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getStudentId());
        assertEquals(7, masteredStudent.getLevel());
        assertEquals(26, masteredStudent.getTotalAnswersGiven());

        assertEquals(1, masteredStudent.getQuestionsWrong().size());


        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/incorrectStudent.json", StudentModelRecord.class);
        StudentModel insm = smr2.buildStudentModel(myQP);
        StudentData insmsd = new StudentData(insm);

        assertEquals(6, insmsd.getQuestionsWrong().size());

    }
}

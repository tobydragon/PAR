package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonIoUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentDataTest {

    @Test
    public void constructorTest() throws IOException {
        //brand new student
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        assertEquals("student", newStudent.getUserId());
        assertEquals(1, newStudent.getLevel());
        assertEquals(0, newStudent.getNumQuestionsAnswered());


        //mastered student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getUserId());
        assertEquals(7, masteredStudent.getLevel());
        assertEquals(25, masteredStudent.getNumQuestionsAnswered());

        //level 3 student
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        assertEquals("level4Student", level4StudentData.getUserId());
        assertEquals(4, level4StudentData.getLevel());
        assertEquals(11, level4StudentData.getNumQuestionsAnswered());
    }
}

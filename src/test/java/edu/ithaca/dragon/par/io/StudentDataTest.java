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
        StudentModel masteredStudentModelRecord = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModelRecord);
        assertEquals("masteredStudent", masteredStudent.getUserId());
        assertEquals(7, newStudent.getLevel());
        assertEquals(25, newStudent.getNumQuestionsAnswered());

        //level 3 student
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/student.json", StudentModelRecord.class);
        StudentModel level3Student = smr2.buildStudentModel(myQP);
        StudentData level3StudentData = new StudentData(level3Student);
        assertEquals("averageStudent", level3StudentData.getUserId());
        assertEquals(3, level3StudentData.getLevel());
        assertEquals(11, level3StudentData.getNumQuestionsAnswered());
    }
}

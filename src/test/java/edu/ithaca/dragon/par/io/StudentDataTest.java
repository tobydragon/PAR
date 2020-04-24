package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonIoUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentDataTest {

    @Test
    public void constructorTest() throws IOException {
        //brand new student
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        assertEquals("student", newStudent.getUserId());
        assertEquals(1, newStudent.getLevel());
        assertEquals(0, newStudent.getTotalAnswersGiven());


        //mastered student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getUserId());
        assertEquals(7, masteredStudent.getLevel());
        assertEquals(25, masteredStudent.getTotalAnswersGiven());

        //level 3 student
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        assertEquals("level4Student", level4StudentData.getUserId());
        assertEquals(4, level4StudentData.getLevel());
        assertEquals(11, level4StudentData.getTotalAnswersGiven());
    }

    @Test
    public void updateDataTest() throws IOException{
        //mastered student creation
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getUserId());
        assertEquals(7, masteredStudent.getLevel());
        assertEquals(25, masteredStudent.getTotalAnswersGiven());

        //add responses
        ResponsesPerQuestion r1 = new ResponsesPerQuestion("masteredStudent", myQP.getQuestionFromId("491-zone-./images/metacarpal37.jpg"), "491-zone-./images/metacarpal37.jpg");
        masteredStudentModel.getUserResponseSet().addResponse(r1);
        r1.addNewResponse("uhhhhh");
        r1.addNewResponse("idk the answer");
        r1.addNewResponse("totally clueless!");

        //change level and answersGiven
        masteredStudent.updateData(masteredStudentModel);
        assertEquals("masteredStudent", masteredStudent.getUserId());
        assertEquals(6, masteredStudent.getLevel());
        assertEquals(28, masteredStudent.getTotalAnswersGiven());

        //wrong student model, IllegalArgumentException
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        assertThrows(IllegalArgumentException.class, ()-> masteredStudent.updateData(student));
    }
}

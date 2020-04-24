package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudentDataAnalyzerTest {

    @Test
    public void constructorTest()throws IOException {
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertNotNull(sda);
        assertEquals(0, sda.getStudentDataList());

        //1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        List<StudentData> sdList = new ArrayList<StudentData>();
        sdList.add(masteredStudent);

        sda = new StudentDataAnalyzer(sdList);
        assertNotNull(sda);
        assertEquals(1, sda.getStudentDataList());

        //2 students
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sdList.add(level4StudentData);

        sda = new StudentDataAnalyzer(sdList);
        assertNotNull(sda);
        assertEquals(2, sda.getStudentDataList());
    }
}

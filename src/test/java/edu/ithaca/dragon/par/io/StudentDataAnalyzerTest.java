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

public class StudentDataAnalyzerTest {

    @Test
    public void constructorTest()throws IOException {
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertNotNull(sda);
        assertEquals(0, sda.getStudentDataList().size());

        //1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        List<StudentData> sdList = new ArrayList<StudentData>();
        sdList.add(masteredStudent);

        sda = new StudentDataAnalyzer(sdList);
        assertNotNull(sda);
        assertEquals(1, sda.getStudentDataList().size());

        //2 students
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sdList.add(level4StudentData);

        sda = new StudentDataAnalyzer(sdList);
        assertNotNull(sda);
        assertEquals(2, sda.getStudentDataList().size());
    }

    @Test
    public void addStudentDataTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertNotNull(sda);
        assertEquals(0, sda.getStudentDataList().size());

        //1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);
        assertNotNull(sda);
        assertEquals(1, sda.getStudentDataList().size());

        //2 students
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);

        sda.addStudentData(level4StudentData);
        assertNotNull(sda);
        assertEquals(2, sda.getStudentDataList().size());

        //null, IllegalArgumentException
        assertThrows(IllegalArgumentException.class, ()-> sda.addStudentData(null));
    }

    @Test
    public void removeStudentDataTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);
        assertNotNull(sda);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);



        assertEquals(2, sda.getStudentDataList().size());

        //remove student
        sda.removeStudentData("level4Student");
        assertEquals(1, sda.getStudentDataList().size());

        //remove student no longer in list, IllegalArgumentException. Same number of students after
        assertThrows(IllegalArgumentException.class, ()-> sda.removeStudentData("level4Student"));
        assertEquals(1, sda.getStudentDataList().size());

        //remove student never in the list, IllegalArgumentException. Same number of students after
        assertThrows(IllegalArgumentException.class, ()-> sda.removeStudentData("level1Student"));
        assertEquals(1, sda.getStudentDataList().size());

        //remove last student
        sda.removeStudentData("masteredStudent");
        assertEquals(0, sda.getStudentDataList().size());

        //remove student not in list, IllegalArgumentException. Still 0 students after
        assertThrows(IllegalArgumentException.class, ()-> sda.removeStudentData("masteredStudent"));
        assertEquals(0, sda.getStudentDataList().size());
    }

    @Test
    public void getStudentData() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);
        assertNotNull(sda);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //get students
        assertEquals(masteredStudent.getStudentId(), sda.getStudentData("masteredStudent").getStudentId());
        assertEquals(masteredStudent.getTotalAnswersGiven(), sda.getStudentData("masteredStudent").getTotalAnswersGiven());
        assertEquals(masteredStudent.getLevel(), sda.getStudentData("masteredStudent").getLevel());

        assertEquals(level4StudentData.getStudentId(), sda.getStudentData("level4Student").getStudentId());
        assertEquals(level4StudentData.getTotalAnswersGiven(), sda.getStudentData("level4Student").getTotalAnswersGiven());
        assertEquals(level4StudentData.getLevel(), sda.getStudentData("level4Student").getLevel());


        //not real student, throws IllegalArgumentException

        assertThrows(IllegalArgumentException.class, ()-> sda.getStudentData("notRealStudent"));
    }

    @Test
    public void calcAverageLevelTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAverageLevel());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);
        assertNotNull(sda);

        assertEquals(7.0, sda.calcAverageLevel());

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        assertEquals(5.5, sda.calcAverageLevel());

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        assertEquals(4, sda.calcAverageLevel());
    }

    @Test
    public void calcAverageTotalAnswersTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAverageTotalAnswers());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        assertEquals(26.0, sda.calcAverageTotalAnswers());

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        assertEquals(18.5, sda.calcAverageTotalAnswers());

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        assertTrue(12.3 < sda.calcAverageTotalAnswers() && sda.calcAverageTotalAnswers() < 12.4);
    }

    @Test
    public void getListGivenLevelTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, ()-> sda.getListGivenLevel(4));

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.getListGivenLevel(4));
        //level present
        assertEquals(1, sda.getListGivenLevel(7).size());

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.getListGivenLevel(1));
        //level present
        assertEquals(1, sda.getListGivenLevel(4).size());

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.getListGivenLevel(2));
        //level present
        assertEquals(1, sda.getListGivenLevel(1).size());

        //2 students level 7
        StudentModelRecord  smrlvl7 = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel2 = smrlvl7.buildStudentModel(myQP);
        StudentData masteredStudent2 = new StudentData(masteredStudentModel2);
        sda.addStudentData(masteredStudent2);


        assertEquals(2, sda.getListGivenLevel(7).size());


        //3 students level 7
        StudentModelRecord  smrlevel7 = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel3 = smrlevel7.buildStudentModel(myQP);
        StudentData masteredStudent3 = new StudentData(masteredStudentModel3);
        sda.addStudentData(masteredStudent3);

        assertEquals(3, sda.getListGivenLevel(7).size());
    }

    @Test
    public void calcAverageTotalAnswersGivenLevelTest() throws IOException {
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(4));

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(4));
        //level present
        assertTrue(25.9 < sda.calcAverageTotalAnswersGivenLevel(7) && sda.calcAverageTotalAnswersGivenLevel(7) < 26.1);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(1));
        //level present
        assertEquals(11, sda.calcAverageTotalAnswersGivenLevel(4));
        assertTrue(10.9 < sda.calcAverageTotalAnswersGivenLevel(4) && sda.calcAverageTotalAnswersGivenLevel(4) < 11.1);

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(2));
        //level present
        assertTrue(-.9 < sda.calcAverageTotalAnswersGivenLevel(1) && sda.calcAverageTotalAnswersGivenLevel(1) < 0.1);

        //2 students level 7
        StudentModelRecord  smrlvl7 = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel2 = smrlvl7.buildStudentModel(myQP);
        StudentData masteredStudent2 = new StudentData(masteredStudentModel2);
        sda.addStudentData(masteredStudent2);

        assertTrue(25.9 < sda.calcAverageTotalAnswersGivenLevel(7) && sda.calcAverageTotalAnswersGivenLevel(7) < 26.1);
        //add correct answers
        ResponsesPerQuestion r1 = masteredStudentModel2.getUserResponseSet().getResponsesPerQuestionList().get(4);
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");

        masteredStudent2.updateData(masteredStudentModel2);
        assertTrue(28.4 < sda.calcAverageTotalAnswersGivenLevel(7) && sda.calcAverageTotalAnswersGivenLevel(7) < 28.6);



        //3 students level 7
        StudentModelRecord  smrlevel7 = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel3 = smrlevel7.buildStudentModel(myQP);
        StudentData masteredStudent3 = new StudentData(masteredStudentModel3);
        assertTrue(28.4 < sda.calcAverageTotalAnswersGivenLevel(7) && sda.calcAverageTotalAnswersGivenLevel(7) < 28.6);
        sda.addStudentData(masteredStudent3);

        assertTrue(27.6 < sda.calcAverageTotalAnswersGivenLevel(7) && sda.calcAverageTotalAnswersGivenLevel(7) < 27.7);
    }


    @Test
    public void writeStudentFileTest() throws IOException{
//empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        String fileName = "studentDataYay.csv";
        sda.writeStudentDataFile(fileName);
    }
}

package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.studentModel.QuestionCount;
import edu.ithaca.dragon.par.deprecated.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.deprecated.studentModel.StudentModel;
import edu.ithaca.dragon.par.domain.Question;
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
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());

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
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());

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
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
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
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
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
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);
        assertNotNull(sda);

        assertEquals(6.0, sda.calcAverageLevel());

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        assertEquals(4.5, sda.calcAverageLevel());

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        assertTrue(3.4 > sda.calcAverageLevel() && 3.2 < sda.calcAverageLevel());
    }

    @Test
    public void calcAverageTotalAnswersTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAverageTotalAnswers());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
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
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.getListGivenLevel(4));
        //level present
        assertEquals(1, sda.getListGivenLevel(6).size());

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.getListGivenLevel(1));
        //level present
        assertEquals(1, sda.getListGivenLevel(3).size());

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


        assertEquals(2, sda.getListGivenLevel(6).size());


        //3 students level 7
        StudentModelRecord  smrlevel7 = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel3 = smrlevel7.buildStudentModel(myQP);
        StudentData masteredStudent3 = new StudentData(masteredStudentModel3);
        sda.addStudentData(masteredStudent3);

        assertEquals(3, sda.getListGivenLevel(6).size());
    }

    @Test
    public void calcAverageTotalAnswersGivenLevelTest() throws IOException {
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(4));

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(4));
        //level present
        assertTrue(25.9 < sda.calcAverageTotalAnswersGivenLevel(6) && sda.calcAverageTotalAnswersGivenLevel(6) < 26.1);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(1));
        //level present
        assertEquals(11, sda.calcAverageTotalAnswersGivenLevel(3));
        assertTrue(10.9 < sda.calcAverageTotalAnswersGivenLevel(3) && sda.calcAverageTotalAnswersGivenLevel(3) < 11.1);

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        //level not present
        assertThrows(IllegalArgumentException.class, ()-> sda.calcAverageTotalAnswersGivenLevel(2));
        //level present
        assertTrue(-.9 < sda.calcAverageTotalAnswersGivenLevel(1) && sda.calcAverageTotalAnswersGivenLevel(1) < 0.1);

        //2 students level 6
        StudentModelRecord  smrlvl7 = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel2 = smrlvl7.buildStudentModel(myQP);
        StudentData masteredStudent2 = new StudentData(masteredStudentModel2);
        sda.addStudentData(masteredStudent2);

        assertTrue(25.9 < sda.calcAverageTotalAnswersGivenLevel(6) && sda.calcAverageTotalAnswersGivenLevel(6) < 26.1);
        //add correct answers
        ResponsesPerQuestion r1 = masteredStudentModel2.getUserResponseSet().getResponsesPerQuestionList().get(4);
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");
        r1.addNewResponse("superficial digital flexor tendon");

        masteredStudent2.updateData(masteredStudentModel2);
        assertTrue(28.4 < sda.calcAverageTotalAnswersGivenLevel(6) && sda.calcAverageTotalAnswersGivenLevel(6) < 28.6);

        //3 students level 7
        StudentModelRecord  smrlevel7 = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel3 = smrlevel7.buildStudentModel(myQP);
        StudentData masteredStudent3 = new StudentData(masteredStudentModel3);
        assertTrue(28.4 < sda.calcAverageTotalAnswersGivenLevel(6) && sda.calcAverageTotalAnswersGivenLevel(6) < 28.6);
        sda.addStudentData(masteredStudent3);

        assertTrue(27.6 < sda.calcAverageTotalAnswersGivenLevel(6) && sda.calcAverageTotalAnswersGivenLevel(6) < 27.7);
    }

    @Test
    public void calcPercentCorrectResponsesTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAveragePercentCorrectResponses());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        assertTrue(88.4 < sda.calcAveragePercentCorrectResponses() && sda.calcAveragePercentCorrectResponses() < 88.5);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        assertTrue(94.2 < sda.calcAveragePercentCorrectResponses() && sda.calcAveragePercentCorrectResponses() < 94.3);

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);
        assertTrue(62.8 < sda.calcAveragePercentCorrectResponses() && sda.calcAveragePercentCorrectResponses() < 62.9);
    }

    @Test
    public void calcAveragePercentWrongFirstTimeTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAveragePercentWrongFirstTime());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        assertTrue(17.6 < sda.calcAveragePercentWrongFirstTime() && sda.calcAveragePercentWrongFirstTime() < 17.7);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        assertTrue(8.8 < sda.calcAveragePercentWrongFirstTime() && sda.calcAveragePercentWrongFirstTime() < 8.9);

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        assertTrue(5.8 < sda.calcAveragePercentWrongFirstTime() && sda.calcAveragePercentWrongFirstTime() < 5.9);
    }

    @Test
    public void calcAveragePercentRightAfterWrongFirstTimeTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        assertThrows(ArithmeticException.class, ()-> sda.calcAveragePercentRightAfterWrongFirstTime());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);

        assertTrue(33.3 < sda.calcAveragePercentRightAfterWrongFirstTime() && sda.calcAveragePercentRightAfterWrongFirstTime() < 33.4);

        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);


        assertTrue(16.6 < sda.calcAveragePercentRightAfterWrongFirstTime() && sda.calcAveragePercentRightAfterWrongFirstTime() < 16.7);

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);


        assertTrue(11.1 < sda.calcAveragePercentRightAfterWrongFirstTime() && sda.calcAveragePercentRightAfterWrongFirstTime() < 11.2);
    }

    @Test
    public void writeStudentFileTest() throws IOException{
//empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        sda.addStudentData(masteredStudent);

        //student2
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //student3
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        //student4
        StudentModelRecord  smr3 = JsonUtil.fromJsonFile("src/test/resources/author/students/incorrectStudent.json", StudentModelRecord.class);
        StudentModel stud = smr3.buildStudentModel(myQP);
        StudentData studData = new StudentData(stud);
        sda.addStudentData(studData);

        //student5
        StudentModelRecord  smr4 = JsonUtil.fromJsonFile("src/test/resources/author/students/notMasteredStudent.json", StudentModelRecord.class);
        StudentModel stud2 = smr4.buildStudentModel(myQP);
        StudentData studData2 = new StudentData(stud2);
        sda.addStudentData(studData2);

//        String fileName = "studentDataYay.csv";
//        sda.writeStudentDataFile(fileName);
    }

    @Test
    public void findMostIncorrectQuestionsTest() throws IOException{
        //empty StudentDataAnalyzer
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());
        List<QuestionCount> incorrect = sda.findMostIncorrectQuestions(1);
        assertEquals(0, incorrect.size());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);

        sda.addStudentData(masteredStudent);


        incorrect = sda.findMostIncorrectQuestions(1);
        assertEquals("491-zone-./images/metacarpal37.jpg", incorrect.get(0).getQuestion().getId());


        //invalid numbers
        assertThrows(IllegalArgumentException.class, ()-> sda.findMostIncorrectQuestions(-3));
        assertThrows(IllegalArgumentException.class, ()-> sda.findMostIncorrectQuestions(-1));



        //add another student (2 total)
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        incorrect = sda.findMostIncorrectQuestions(3);

        assertEquals("327-structure0-./images/metacarpal56.jpg", incorrect.get(0).getQuestion().getId());

        assertEquals("477-zone-./images/metacarpal19.jpg", incorrect.get(1).getQuestion().getId());

        assertEquals("491-zone-./images/metacarpal37.jpg", incorrect.get(2).getQuestion().getId());

        //add another student (3 total)
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        incorrect = sda.findMostIncorrectQuestions(3);
        assertEquals("327-structure0-./images/metacarpal56.jpg", incorrect.get(0).getQuestion().getId());
        assertEquals("477-zone-./images/metacarpal19.jpg", incorrect.get(1).getQuestion().getId());
        assertEquals("491-zone-./images/metacarpal37.jpg", incorrect.get(2).getQuestion().getId());

    }


    @Test
    public void calcStatisticsTest() throws IOException{
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());

        //add 1 student
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        StudentData masteredStudent = new StudentData(masteredStudentModel);
        sda.addStudentData(masteredStudent);

        //student2
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        StudentData level4StudentData = new StudentData(level4Student);
        sda.addStudentData(level4StudentData);

        //student3
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        StudentData newStudent = new StudentData(student);
        sda.addStudentData(newStudent);

        //student4
        StudentModelRecord  smr3 = JsonUtil.fromJsonFile("src/test/resources/author/students/incorrectStudent.json", StudentModelRecord.class);
        StudentModel stud = smr3.buildStudentModel(myQP);
        StudentData studData = new StudentData(stud);
        sda.addStudentData(studData);

        //student5
        StudentModelRecord  smr4 = JsonUtil.fromJsonFile("src/test/resources/author/students/notMasteredStudent.json", StudentModelRecord.class);
        StudentModel stud2 = smr4.buildStudentModel(myQP);
        StudentData studData2 = new StudentData(stud2);
        sda.addStudentData(studData2);

        sda.calcStatistics();
        assertTrue(2.75 < sda.averageLevel && 2.85 > sda.averageLevel);
        assertTrue(19.75 < sda.averageTotalAnswers && 19.85 > sda.averageTotalAnswers);
        assertTrue( 62.6 < sda.averagePercentCorrectResponses && 62.7 > sda.averagePercentCorrectResponses);
        assertTrue(19.6 < sda.averagePercentWrongFirstTime && 19.7 > sda.averagePercentWrongFirstTime);
        assertTrue(31.6 < sda.averagePercentRightAfterWrongFirstTime && 31.7 > sda.averagePercentRightAfterWrongFirstTime);
        assertEquals("491-zone-./images/metacarpal37.jpg", sda.mostIncorrectQuestions.get(0).getQuestion().getId());

    }

}

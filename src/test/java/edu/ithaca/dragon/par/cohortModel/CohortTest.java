package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CohortTest {

    @Test
    public void constructorTest() throws IOException {
        //task generator for both types
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());


        //instantiate list of students
        List<String> studentList = new ArrayList<>();
        studentList.add("testStudent1");
        studentList.add("testStudent2");
        studentList.add("testStudent3");

        //construct a cohort with studentIDs when created
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        Cohort cohort = new Cohort(taskGenerator, studentList, new LevelMessageGenerator(), questionPool);

        assertEquals(taskGenerator, cohort.getTaskGenerator());
        assertEquals(studentList, cohort.getStudentIDs());
        assertEquals(questionPool, cohort.getQuestionPool());
    }

    @Test
    public void addStudentTest() throws IOException {
        //add studentID to new cohort with studentIDs
        List<String> studentList = new ArrayList<>();
        studentList.add("testStudent1");
        studentList.add("testStudent2");
        studentList.add("testStudent3");

        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        Cohort testCohort2 = new Cohort(new RandomTaskGenerator(), studentList, new SilentMessageGenerator(), questionPool);
        testCohort2.addStudent("testStudent4");

        //add studentID to new cohort with studentIDs that already exists
        testCohort2.addStudent("testStudent2");

        List<String> idsFromCohort = testCohort2.getStudentIDs();
        assertTrue(idsFromCohort.contains("testStudent2"));
        assertTrue(idsFromCohort.contains("testStudent4"));
        assertEquals(idsFromCohort.size(), 4);
    }
}

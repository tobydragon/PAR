package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CohortDatastoreTest {

    @Test
    public void constructorTest(){
        CohortDatastore cohortMap = new CohortDatastore();
        //test size of map
        assertEquals(cohortMap.getSize(), 0);

        //verify keys(students) are empty
        assertEquals(cohortMap.getAllStudents().size(), 0);

        //verify values(cohorts) are empty
        assertEquals(cohortMap.getAllCohorts().size(), 0);
    }


    @Test
    public void putStudentTest() throws IOException {
        CohortDatastore cohortMap = new CohortDatastore();

        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        StudentModel studentModel2 = new StudentModel("TestUser2", questionPool.getAllQuestions());
        StudentModel studentModel3 = new StudentModel("TestUser3", questionPool.getAllQuestions());

        Cohort cohort = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());
        Cohort cohort2 = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());

        //put into empty map
        cohortMap.putStudent(studentModel.getUserId(), cohort);
        assertEquals(cohortMap.getSize(), 1);

        //put into hashmap with multiple existing entries
        cohortMap.putStudent(studentModel2.getUserId(), cohort2);
        assertEquals(cohortMap.getSize(), 2);

        cohortMap.putStudent(studentModel3.getUserId(), cohort);
        assertEquals(cohortMap.getSize(), 3);

        //put with studentId-cohort pair that already exists
        cohortMap.putStudent(studentModel.getUserId(), cohort);
        assertEquals(cohortMap.getSize(), 3);

        //put with studentId that already exists in different cohort ERROR
        //TODO is this right??
        assertThrows(IllegalArgumentException.class,()->{cohortMap.putStudent(studentModel.getUserId(), cohort2);} );

    }

    @Test
    public void getCohortFromStudentIDTest() throws IOException {
        CohortDatastore cohortMap = new CohortDatastore();

        //get cohort from empty map
        assertThrows(IllegalArgumentException.class, ()->{ cohortMap.getTaskGeneratorFromStudentID("TestUser1");});

        //get cohort from map with multiple entries
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        StudentModel studentModel2 = new StudentModel("TestUser2", questionPool.getAllQuestions());
        StudentModel studentModel3 = new StudentModel("TestUser3", questionPool.getAllQuestions());

        Cohort cohort = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());
        Cohort cohort2 = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());

        cohortMap.putStudent(studentModel.getUserId(), cohort);
        cohortMap.putStudent(studentModel2.getUserId(), cohort2);
        cohortMap.putStudent(studentModel3.getUserId(), cohort);

        assertEquals(cohortMap.getTaskGeneratorFromStudentID("TestUser1"), cohort);
        assertEquals(cohortMap.getTaskGeneratorFromStudentID("TestUser2"), cohort2);
        assertEquals(cohortMap.getTaskGeneratorFromStudentID("TestUser3"), cohort);

        //get cohort from ID that does not exist
        assertThrows(IllegalArgumentException.class, ()->{cohortMap.getTaskGeneratorFromStudentID("TestUser4");});
    }

    @Test
    public void getAllStudentsTest(){
        CohortDatastore cohortMap = new CohortDatastore();

        assertEquals(cohortMap.getAllStudents().size(), 0);
    }

}

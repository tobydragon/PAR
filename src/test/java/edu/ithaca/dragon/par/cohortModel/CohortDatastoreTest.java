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

    //TODO where is the cohort stored? should this Map be the cohort object or an ID type string?
        //pretty sure this should be IDs
    //TODO addStudentToCohort tests and method: just one or multiple or both?
    //TODO: should student be able to be in more than one cohort? i think not
    @Test
    public void constructorTest(){
        CohortDatastore cohortDatastore = new CohortDatastore();
        //test size of map
        assertEquals(cohortDatastore.getSize(), 0);

        //verify keys(students) are empty
        assertEquals(cohortDatastore.getAllStudents().size(), 0);

        //verify values(cohorts) are empty
        assertEquals(cohortDatastore.getAllCohorts().size(), 0);
    }


    @Test
    public void putStudentTest() throws IOException {
        CohortDatastore cohortDatastore = new CohortDatastore();

        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        StudentModel studentModel2 = new StudentModel("TestUser2", questionPool.getAllQuestions());
        StudentModel studentModel3 = new StudentModel("TestUser3", questionPool.getAllQuestions());

        Cohort cohort = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());
        Cohort cohort2 = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());

        //put into empty map
        cohortDatastore.putStudent(studentModel.getUserId(), cohort);
        assertEquals(cohortDatastore.getSize(), 1);

        //put into hashmap with multiple existing entries
        cohortDatastore.putStudent(studentModel2.getUserId(), cohort2);
        assertEquals(cohortDatastore.getSize(), 2);

        cohortDatastore.putStudent(studentModel3.getUserId(), cohort);
        assertEquals(cohortDatastore.getSize(), 3);

        //put with studentId-cohort pair that already exists
        cohortDatastore.putStudent(studentModel.getUserId(), cohort);
        assertEquals(cohortDatastore.getSize(), 3);

        //put with studentId that already exists in different cohort ERROR
        //TODO is this right??
        assertThrows(IllegalArgumentException.class,()->{cohortDatastore.putStudent(studentModel.getUserId(), cohort2);} );

    }

    @Test
    public void getCohortFromStudentIDTest() throws IOException {
        CohortDatastore cohortDatastore = new CohortDatastore();

        //get cohort from empty map
        assertThrows(IllegalArgumentException.class, ()->{ cohortDatastore.getCohortFromStudentID("TestUser1");});

        //get cohort from map with multiple entries
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        StudentModel studentModel2 = new StudentModel("TestUser2", questionPool.getAllQuestions());
        StudentModel studentModel3 = new StudentModel("TestUser3", questionPool.getAllQuestions());

        Cohort cohort = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());
        Cohort cohort2 = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>());

        cohortDatastore.putStudent(studentModel.getUserId(), cohort);
        cohortDatastore.putStudent(studentModel2.getUserId(), cohort2);
        cohortDatastore.putStudent(studentModel3.getUserId(), cohort);

        assertEquals(cohortDatastore.getCohortFromStudentID("TestUser1"), cohort);
        assertEquals(cohortDatastore.getCohortFromStudentID("TestUser2"), cohort2);
        assertEquals(cohortDatastore.getCohortFromStudentID("TestUser3"), cohort);

        //get cohort from ID that does not exist
        assertThrows(IllegalArgumentException.class, ()->{cohortDatastore.getCohortFromStudentID("TestUser4");});
    }

    @Test
    public void getAllStudentsTest(){
        CohortDatastore cohortDatastore = new CohortDatastore();

        assertEquals(cohortDatastore.getAllStudents().size(), 0);
    }

}

package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.RandomTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CohortTest {

    @Test
    public void constructorTest() {
        //task generator for both types
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());


        //instantiate list of students
        List<String> studentList = new ArrayList<>();
        studentList.add("testStudent1");
        studentList.add("testStudent2");
        studentList.add("testStudent3");

        //construct a cohort with studentIDs when created
        Cohort cohort = new Cohort(taskGenerator, studentList);

        assertEquals(cohort.getTaskGenerator(), taskGenerator);
        assertEquals(cohort.getStudentIDs(), studentList);

        //construct an empty cohort
        Cohort cohort1 = new Cohort(taskGenerator);
        assertEquals(cohort1.getTaskGenerator(), taskGenerator);
        assertEquals(cohort1.getStudentIDs().size(), 0);
    }

    @Test
    public void addStudentTest(){
        //add studentID to empty cohort
        Cohort testCohort = new Cohort(new RandomTaskGenerator());
        testCohort.addStudent("testStudent1");

        //add studentID to cohort with existing studentIDs
        testCohort.addStudent("testStudent2");

        //add studentID that already exists
        testCohort.addStudent("testStudent1");

        List<String> idsFromCohort = testCohort.getStudentIDs();
        assertTrue(idsFromCohort.contains("testStudent1"));
        assertTrue(idsFromCohort.contains("testStudent2"));
        assertEquals(idsFromCohort.size(), 2);

        //add studentID to new cohort with studentIDs
        List<String> studentList = new ArrayList<>();
        studentList.add("testStudent1");
        studentList.add("testStudent2");
        studentList.add("testStudent3");

        Cohort testCohort2 = new Cohort(new RandomTaskGenerator(), studentList);
        testCohort2.addStudent("testStudent4");

        //add studentID to new cohort with studentIDs that already exists
        testCohort2.addStudent("testStudent2");

        idsFromCohort = testCohort2.getStudentIDs();
        assertTrue(idsFromCohort.contains("testStudent2"));
        assertTrue(idsFromCohort.contains("testStudent4"));
        assertEquals(idsFromCohort.size(), 4);
    }

    @Test
    public void removeStudentTest(){
        Cohort testCohort = new Cohort(new RandomTaskGenerator());
        assertEquals(testCohort.getStudentIDs().size(), 0);

        //remove from empty cohort
        testCohort.removeStudent("testStudent1");
        assertEquals(testCohort.getStudentIDs().size(), 0);

        //remove id that doesnt exist from cohort w 1 student
        testCohort.addStudent("testStudent1");
        testCohort.removeStudent("testStudent2");
        assertEquals(testCohort.getStudentIDs().size(), 1);

        //remove id from cohort w 1 student
        testCohort.removeStudent("testStudent1");
        assertEquals(testCohort.getStudentIDs().size(), 0);

        //remove id from cohort with multiple students
        List<String> studentList = new ArrayList<>();
        studentList.add("testStudent3");
        studentList.add("testStudent4");
        studentList.add("testStudent5");

        testCohort = new Cohort(new RandomTaskGenerator(), studentList);
        testCohort.removeStudent("testStudent3");
        assertEquals(testCohort.getStudentIDs().size(), 2);

        //remove id that doesnt exist from cohort with multiple students
        testCohort.removeStudent("testStudent2");
        assertEquals(testCohort.getStudentIDs().size(), 2);
    }
}

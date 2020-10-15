package edu.ithaca.dragon.par.cohortModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CohortDatastoreTest {

    @Test
    public void constructorTest(){

    }

    @Test
    public void getTaskGeneratorFromStudentIDTest() {
        CohortDatastore cohortMap = new CohortDatastore();

        //get cohort from empty map
        assertNull(cohortMap.getTaskGeneratorFromStudentID("TestUser1"));

        //get cohort from map with multiple entries


        //get cohort from ID that does not exist
        assertNull(cohortMap.getTaskGeneratorFromStudentID("TestUser4"));
    }

}

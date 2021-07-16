package edu.ithaca.dragon.par.cohort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.ithaca.dragon.util.JsonIoHelperDefault;

public class CohortDatasourceJsonTest {
    @Test
    public void addStudentToCohortTest(@TempDir Path tempDir) throws IOException{

        CohortDatasourceJson sample1 = new CohortDatasourceJson(
            "allCohorts",
            tempDir.toString() + "/sampleCohorts.json",
            "src/test/resources/rewrite/SampleCohorts.json",
            new JsonIoHelperDefault()
        );

        assertEquals(3, sample1.getStudentIdsForCohort("random").size());
        sample1.addStudentToCohort("random", "s1");
        sample1.addStudentToCohort("random", "s2");
        assertThrows(IllegalArgumentException.class, ()-> sample1.addStudentToCohort("random", "s1"));
        assertEquals(5, sample1.getStudentIdsForCohort("random").size());
        assertTrue(sample1.getStudentIdsForCohort("random").contains("s1"));
        assertTrue(sample1.getStudentIdsForCohort("random").contains("s2"));

        assertTrue(sample1.getStudentIdsForCohort("random").contains("r1"));
        assertTrue(sample1.getStudentIdsForCohort("inOrder").contains("o1"));


        CohortDatasourceJson sample2 = new CohortDatasourceJson(
            "allCohorts",
            tempDir.toString() + "/sampleCohorts.json",
            null,
            new JsonIoHelperDefault()
        );

        assertEquals(5, sample2.getStudentIdsForCohort("random").size());
        assertTrue(sample2.getStudentIdsForCohort("random").contains("s1"));
        sample1.addStudentToCohort("inOrder", "s1");
        sample1.addStudentToCohort("inOrder", "s2");

        assertThrows(IllegalArgumentException.class, ()-> sample1.addStudentToCohort("inOrder", "s1"));
        assertThrows(IllegalArgumentException.class, ()-> sample1.addStudentToCohort("random", "s2"));
        assertThrows(IllegalArgumentException.class, ()-> sample1.addStudentToCohort("inOrder", "o1"));

        assertTrue(sample1.getStudentIdsForCohort("inOrder").contains("s1"));




    }
    
}

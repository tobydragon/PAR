package edu.ithaca.dragon.par.cohort;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            "src/test/resources/rewrite/sampleCohorts.json",
            new JsonIoHelperDefault()
        );

        assertEquals(3, sample1.getStudentIdsForCohort("random").size());
        sample1.addStudentToCohort("random", "s1");
        assertEquals(4, sample1.getStudentIdsForCohort("random").size());
        assertTrue(sample1.getStudentIdsForCohort("random").contains("s1"));


        CohortDatasourceJson sample2 = new CohortDatasourceJson(
            "allCohorts",
            tempDir.toString() + "/sampleCohorts.json",
            null,
            new JsonIoHelperDefault()
        );

        assertEquals(4, sample2.getStudentIdsForCohort("random").size());
        assertTrue(sample2.getStudentIdsForCohort("random").contains("s1"));

    }
    
}

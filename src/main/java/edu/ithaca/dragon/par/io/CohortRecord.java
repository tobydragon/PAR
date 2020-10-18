package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;

public class CohortRecord {
    private String taskGeneratorType;
    private List<String> studentIDs;

    public String getTaskGeneratorType() {
        return taskGeneratorType;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    public static CohortRecord makeCohortRecordFromCohort(Cohort cohortIn){
        return null;
    }

    public static Cohort makeCohortFromCohortRecord(CohortRecord cohortRecordIn){
        return null;
    }
}

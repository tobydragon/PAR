package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;
import java.util.Map;

public class CohortDatastore {
    private Map cohortMap;
    private List<Cohort> masterCohortList;

    public CohortDatastore(){

    }

    public int getSize() {
        return -1;
    }

    public void addStudentToCohort(String studentIDIn, Cohort cohortIn){

    }

    public void removeStudentFromCohort(String studentIDIn, Cohort cohortIn){

    }

    public TaskGenerator getTaskGeneratorFromStudentID(String studentIDIn){
        return null;
    }
}

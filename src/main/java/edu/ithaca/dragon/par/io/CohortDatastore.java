package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CohortDatastore {
    private final Map<String, Cohort> cohortMap;
    private final List<Cohort> masterCohortList;

    public CohortDatastore(){
        this.cohortMap = new HashMap<>();
        this.masterCohortList = new ArrayList<>();
    }

    public int getNumberCohorts() {
        return masterCohortList.size();
    }

    public int getTotalNumberStudents(){ return cohortMap.size();}

    public void addCohort(TaskGenerator taskGenerator, List<String> studentIDs){
        Cohort toAdd = new Cohort(taskGenerator, studentIDs);
        masterCohortList.add(toAdd);

        for (String studentID : studentIDs) {
            cohortMap.put(studentID, masterCohortList.get(masterCohortList.size() - 1));
        }
    }

    public TaskGenerator getTaskGeneratorFromStudentID(String studentIDIn){
        if (!cohortMap.containsKey(studentIDIn)){
            return null;
        } else {
            Cohort cohortOfStudent = cohortMap.get(studentIDIn);
            return cohortOfStudent.getTaskGenerator();
        }
    }

    public static CohortDatastore makeCohortDatastoreFromCohortRecords(List<CohortRecord> cohortRecordsList){
        return null;
    }
}

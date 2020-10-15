package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.studentModel.StudentModel;

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

    public List<StudentModel> getAllStudents(){
        return null;
    }

    public List<Cohort> getAllCohorts(){
        return null;
    }

    public void putStudent(String studentIDIn, Cohort cohortIn){

    }

    public void removeStudentFromCohort(String studentIDIn, Cohort cohortIn){

    }

    public Cohort getTaskGeneratorFromStudentID(String studentIDIn){
        return null;
    }
}

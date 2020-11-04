package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.pedagogicalModel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONCohortDatastore implements CohortDatastore {
    private final Map<String, Cohort> cohortMap;
    private final List<Cohort> masterCohortList;
    private final Cohort defaultCohort;
    private final String cohortDatastoreFilename;

    public JSONCohortDatastore(String filenameIn){
        this.cohortMap = new HashMap<>();
        this.masterCohortList = new ArrayList<>();
        this.defaultCohort = getDefaultCohort();
        this.cohortDatastoreFilename = filenameIn;
    }

    public Cohort getDefaultCohort() {
        if (masterCohortList.size() == 0){
            return null;
        }
        return masterCohortList.get(0);
    }

    @Override
    public int getNumberCohorts() {
        return masterCohortList.size();
    }

    @Override
    public int getTotalNumberStudents(){ return cohortMap.size();}

    @Override
    public List<Cohort> getMasterCohortList(){
        return masterCohortList;
    }

    public String getCohortDatastoreFilename() {
        return cohortDatastoreFilename;
    }

    public void addCohort(TaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator, QuestionPool questionPool){
        Cohort toAdd = new Cohort(taskGenerator, studentIDs, messageGenerator, questionPool);
        masterCohortList.add(toAdd);

        for (String studentID : studentIDs) {
            cohortMap.put(studentID, masterCohortList.get(masterCohortList.size() - 1));
        }
    }

    public void addCohort(OrderedTaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator, QuestionPool questionPool, String questionOrderedInfoFilename){
        Cohort toAdd = new Cohort(taskGenerator, studentIDs, messageGenerator, questionPool, questionOrderedInfoFilename);
        masterCohortList.add(toAdd);

        for (String studentID : studentIDs) {
            cohortMap.put(studentID, masterCohortList.get(masterCohortList.size() - 1));
        }
    }

    @Override
    public TaskGenerator getTaskGeneratorFromStudentID(String studentIDIn){
        if (!cohortMap.containsKey(studentIDIn)){
            return null;
        } else {
            Cohort cohortOfStudent = cohortMap.get(studentIDIn);
            return cohortOfStudent.getTaskGenerator();
        }
    }

    @Override
    public MessageGenerator getMessageGeneratorFromStudentID(String studentIDIn) {
        if (!cohortMap.containsKey(studentIDIn)){
            return null;
        } else {
            Cohort cohortOfStudent = cohortMap.get(studentIDIn);
            return cohortOfStudent.getMessageGenerator();
        }
    }
}

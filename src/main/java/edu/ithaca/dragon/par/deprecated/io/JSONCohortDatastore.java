package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.deprecated.cohortModel.Cohort;
import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.pedagogicalModel.*;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONCohortDatastore implements CohortDatastore {
    private final Map<String, Cohort> cohortMap;
    private final List<Cohort> masterCohortList;
    private final Cohort defaultCohort;
    private final String cohortDatastoreFilename;
    private JsonIoUtil jsonIoUtil;

    public JSONCohortDatastore(String filenameIn, Cohort defaultCohort, JsonIoHelper jsonIoHelper){
        this.cohortMap = new HashMap<>();
        this.masterCohortList = new ArrayList<>();
        this.defaultCohort = defaultCohort;
        this.cohortDatastoreFilename = filenameIn;
        this.jsonIoUtil = new JsonIoUtil(jsonIoHelper);

        this.masterCohortList.add(this.defaultCohort);
        for(int i = 0; i < this.defaultCohort.getStudentIDs().size(); i++){
            this.cohortMap.put(this.defaultCohort.getStudentIDs().get(i), this.defaultCohort);
        }
    }

    public Cohort getDefaultCohort() {
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

    @Override
    public String getCohortDatastoreFilename() {
        return cohortDatastoreFilename;
    }

    @Override
    public Cohort getCohortById(String cohortIdIn) {
        for (Cohort cohort: masterCohortList){
            if (cohortIdIn.equals(cohort.getCohortId())){
                return cohort;
            }
        }

        //TODO: is this the right thing to do??
        return null;
    }

    public boolean isStudentIDInDatastore(String studentID){ return this.cohortMap.containsKey(studentID);}

    public void addCohort(String cohortIDIn, TaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator, QuestionPool questionPool){
        Cohort toAdd = new Cohort(cohortIDIn, taskGenerator, studentIDs, messageGenerator, questionPool);
        masterCohortList.add(toAdd);

        for (String studentID : studentIDs) {
            cohortMap.put(studentID, masterCohortList.get(masterCohortList.size() - 1));
        }
    }

    public void addCohort(String cohortIDIn, OrderedTaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator, QuestionPool questionPool, String questionOrderedInfoFilename){
        Cohort toAdd = new Cohort(cohortIDIn, taskGenerator, studentIDs, messageGenerator, questionPool, questionOrderedInfoFilename);
        masterCohortList.add(toAdd);

        for (String studentID : studentIDs) {
            cohortMap.put(studentID, masterCohortList.get(masterCohortList.size() - 1));
        }
    }

    @Override
    public TaskGenerator getTaskGeneratorFromStudentID(String studentIDIn) throws IOException {
        if (!cohortMap.containsKey(studentIDIn)){
            this.defaultCohort.addStudent(studentIDIn);
            this.cohortMap.put(studentIDIn, this.defaultCohort);
            overwriteCohortDatastoreFile();
            return this.defaultCohort.getTaskGenerator();
        } else {
            Cohort cohortOfStudent = cohortMap.get(studentIDIn);
            return cohortOfStudent.getTaskGenerator();
        }
    }

    @Override
    public MessageGenerator getMessageGeneratorFromStudentID(String studentIDIn) throws IOException {
        if (!cohortMap.containsKey(studentIDIn)){
            this.defaultCohort.addStudent(studentIDIn);
            this.cohortMap.put(studentIDIn, this.defaultCohort);
            overwriteCohortDatastoreFile();
            return this.defaultCohort.getMessageGenerator();
        } else {
            Cohort cohortOfStudent = cohortMap.get(studentIDIn);
            return cohortOfStudent.getMessageGenerator();
        }
    }

    public void overwriteCohortDatastoreFile() throws IOException {
        List<CohortRecord> toWrite = makeCohortRecords();
        jsonIoUtil.toFile(cohortDatastoreFilename, toWrite);
    }

    public List<CohortRecord> makeCohortRecords() {
        List<CohortRecord> toReturn = new ArrayList<>();
        List<Cohort> masterCohortList = getMasterCohortList();
        for (Cohort cohort : masterCohortList) {
            toReturn.add(CohortRecord.makeCohortRecordFromCohort(cohort));
        }
        return toReturn;
    }
}

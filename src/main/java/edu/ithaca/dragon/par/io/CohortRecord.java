package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.pedagogicalModel.*;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CohortRecord {
    private final TaskGeneratorType taskGeneratorType;
    private final List<String> studentIDs;
    private final MessageGeneratorType messageGeneratorType;
    private final QuestionPool questionPool;
    private final String questionOrderedInfoFilename;

    public CohortRecord() {
        this.taskGeneratorType = null;
        this.studentIDs = null;
        this.messageGeneratorType = null;
        this.questionPool = null;
        this.questionOrderedInfoFilename = null;
    }

    public CohortRecord(TaskGeneratorType taskGeneratorType, List<String> studentIDs, MessageGeneratorType messageGeneratorType, QuestionPool questionPool){
        this.taskGeneratorType = taskGeneratorType;
        this.studentIDs = studentIDs;
        this.messageGeneratorType = messageGeneratorType;
        this.questionPool = questionPool;
        this.questionOrderedInfoFilename = null;
    }

    //this constructor should only be used for OrderedTaskGenerator; questionOrderedInfoFilename is not used by other TaskGenerators
    public CohortRecord(TaskGeneratorType taskGeneratorType, List<String> studentIDs, MessageGeneratorType messageGeneratorType, QuestionPool questionPool, String questionOrderedInfoFilename){
        this.taskGeneratorType = taskGeneratorType;
        this.studentIDs = studentIDs;
        this.messageGeneratorType = messageGeneratorType;
        this.questionPool = questionPool;
        this.questionOrderedInfoFilename = questionOrderedInfoFilename;
    }

    public TaskGeneratorType getTaskGeneratorType() {
        return taskGeneratorType;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }
    public MessageGeneratorType getMessageGeneratorType(){ return messageGeneratorType; }
    public QuestionPool getQuestionPool(){
        return questionPool;
    }
    public String getQuestionOrderedInfoFilename() {
        return questionOrderedInfoFilename;
    }

    @Override
    public boolean equals(Object otherObj){
        if (otherObj == null){
            return false;
        }
        if(!CohortRecord.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        CohortRecord other = (CohortRecord) otherObj;
        assert this.messageGeneratorType != null;
        if (!this.messageGeneratorType.equals(other.messageGeneratorType)) return false;
        assert this.taskGeneratorType != null;
        if (!this.taskGeneratorType.equals(other.taskGeneratorType)) return false;
        assert this.questionPool != null;
        if (!this.questionPool.equals(other.questionPool)) return false;
        if (this.questionOrderedInfoFilename != null){
            if (!this.questionOrderedInfoFilename.equals(other.questionOrderedInfoFilename)){
                return false;
            }
        }
        assert this.studentIDs != null;
        return this.studentIDs.equals(other.studentIDs);
    }

    public static CohortRecord makeCohortRecordFromCohort(Cohort cohortIn){
        TaskGenerator taskGenerator = cohortIn.getTaskGenerator();
        MessageGenerator messageGenerator = cohortIn.getMessageGenerator();


        if(taskGenerator != null){

            if (taskGenerator instanceof RandomTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.randomTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.randomTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(TaskGeneratorType.randomTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
                }
            } else if (taskGenerator instanceof OrderedTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.orderedTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool(), cohortIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.orderedTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool(), cohortIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(TaskGeneratorType.orderedTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
                }
            } else if (taskGenerator instanceof LevelTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.levelTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.levelTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(TaskGeneratorType.levelTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
                }
            }
            else if (taskGenerator instanceof LevelTaskGeneratorAttachment) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.levelTaskGeneratorAttachment, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(TaskGeneratorType.levelTaskGeneratorAttachment, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(TaskGeneratorType.levelTaskGeneratorAttachment, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
                }
            }
        }
        return null;
    }

    public static Cohort makeCohortFromCohortRecord(CohortRecord cohortRecordIn) {
        TaskGeneratorType taskGeneratorType = cohortRecordIn.getTaskGeneratorType();
        MessageGeneratorType messageGeneratorType = cohortRecordIn.getMessageGeneratorType();

        switch (taskGeneratorType) {
            case randomTaskGenerator:
                if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                    return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }

                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
            case orderedTaskGenerator:
                List<QuestionOrderedInfo> forOTG = createQuestionOrderedInfoList(cohortRecordIn.getQuestionOrderedInfoFilename(), cohortRecordIn.getQuestionPool());
                if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                    return new Cohort(new OrderedTaskGenerator(cohortRecordIn.getQuestionPool(), forOTG), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool(), cohortRecordIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(new OrderedTaskGenerator(cohortRecordIn.getQuestionPool(), forOTG), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool(), cohortRecordIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(new OrderedTaskGenerator(cohortRecordIn.getQuestionPool(), forOTG), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
            case levelTaskGenerator:
                if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                    return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
            case levelTaskGeneratorAttachment:
                if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                    return new Cohort(new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
        }
        return null;
    }

    public static JSONCohortDatastore makeCohortDatastoreFromCohortRecords(List<CohortRecord> cohortRecordsList, String cohortDatastoreFilename) {
        JSONCohortDatastore toReturn = new JSONCohortDatastore(cohortDatastoreFilename, CohortRecord.makeCohortFromCohortRecord(cohortRecordsList.get(0)));

        for (int i = 1; i < cohortRecordsList.size(); i++) {
            CohortRecord cohortRecord = cohortRecordsList.get(i);
            TaskGeneratorType taskGeneratorType = cohortRecord.getTaskGeneratorType();
            MessageGeneratorType messageGeneratorType = cohortRecord.getMessageGeneratorType();
            List<String> studentIDs = cohortRecord.getStudentIDs();

            switch (taskGeneratorType) {
                case randomTaskGenerator:
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
                case orderedTaskGenerator:
                    List<QuestionOrderedInfo> forOTG = createQuestionOrderedInfoList(cohortRecord.getQuestionOrderedInfoFilename(), cohortRecord.getQuestionPool());
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(new OrderedTaskGenerator(cohortRecord.getQuestionPool(), forOTG), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool(), cohortRecord.getQuestionOrderedInfoFilename());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(new OrderedTaskGenerator(cohortRecord.getQuestionPool(), forOTG), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool(), cohortRecord.getQuestionOrderedInfoFilename());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(new OrderedTaskGenerator(cohortRecord.getQuestionPool(), forOTG), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
                case levelTaskGenerator:
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
                case levelTaskGeneratorAttachment:
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
            }
        }
        return toReturn;
    }

    public static List<CohortRecord> makeCohortRecordsFromCohortDatastore(JSONCohortDatastore cohortDatastoreIn) {
        List<CohortRecord> toReturn = new ArrayList<>();
        List<Cohort> masterCohortList = cohortDatastoreIn.getMasterCohortList();
        for (Cohort cohort : masterCohortList) {
            toReturn.add(CohortRecord.makeCohortRecordFromCohort(cohort));
        }
        return toReturn;
    }

    public static List<QuestionOrderedInfo> createQuestionOrderedInfoList(String questionOrderedListFilename, QuestionPool questionPool){
        // if filename exists, create from file
        try {
            JsonIoUtil jsonIoUtil = new JsonIoUtil(new JsonIoHelperDefault());
            List<QuestionOrderedInfo> toCheck = jsonIoUtil.listFromFile(questionOrderedListFilename, QuestionOrderedInfo.class);
            for (QuestionOrderedInfo questionOrderedInfo : toCheck) {
                if (!questionPool.isIdTaken(questionOrderedInfo.getQuestionID())) {
                    System.out.println("Filename passed into OrderedTaskGenerator contains questions not in QuestionPool. Please check again. Creating a default QuestionOrderedInfoList");
                    return OrderedTaskGenerator.createDefaultQuestionOrderedInfoList(questionPool, true);
                }
            }
            return toCheck;
        } catch (IOException | NullPointerException e) {
            //else go to default method
            System.out.println("Filename passed into OrderedTaskGenerator not found. Please check again. Creating a default QuestionOrderedInfoList");
            return OrderedTaskGenerator.createDefaultQuestionOrderedInfoList(questionPool, true);
        }
    }

    public static void overwriteCohortDatastoreFile(JSONCohortDatastore cohortDatastore) throws IOException {
        JsonIoUtil jsonIoUtil = new JsonIoUtil(new JsonIoHelperDefault());
        List<CohortRecord> toWrite = CohortRecord.makeCohortRecordsFromCohortDatastore(cohortDatastore);
        jsonIoUtil.toFile(cohortDatastore.getCohortDatastoreFilename(), toWrite);
    }
}

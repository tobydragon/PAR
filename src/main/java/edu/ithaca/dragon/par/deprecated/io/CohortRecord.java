package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.deprecated.cohortModel.Cohort;
import edu.ithaca.dragon.par.deprecated.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.deprecated.pedagogicalModel.*;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.IOException;
import java.util.List;

public class CohortRecord {
    private final String cohortId;
    private final TaskGeneratorType taskGeneratorType;
    private final List<String> studentIDs;
    private final MessageGeneratorType messageGeneratorType;
    private final QuestionPool questionPool;
    private final String questionOrderedInfoFilename;

    public CohortRecord() {
        this.cohortId = null;
        this.taskGeneratorType = null;
        this.studentIDs = null;
        this.messageGeneratorType = null;
        this.questionPool = null;
        this.questionOrderedInfoFilename = null;
    }

    public CohortRecord(String cohortIdIn, TaskGeneratorType taskGeneratorType, List<String> studentIDs, MessageGeneratorType messageGeneratorType, QuestionPool questionPool){
        this.cohortId = cohortIdIn;
        this.taskGeneratorType = taskGeneratorType;
        this.studentIDs = studentIDs;
        this.messageGeneratorType = messageGeneratorType;
        this.questionPool = questionPool;
        this.questionOrderedInfoFilename = null;
    }

    //this constructor should only be used for OrderedTaskGenerator; questionOrderedInfoFilename is not used by other TaskGenerators
    public CohortRecord(String cohortIdIn, TaskGeneratorType taskGeneratorType, List<String> studentIDs, MessageGeneratorType messageGeneratorType, QuestionPool questionPool, String questionOrderedInfoFilename){
        this.cohortId = cohortIdIn;
        this.taskGeneratorType = taskGeneratorType;
        this.studentIDs = studentIDs;
        this.messageGeneratorType = messageGeneratorType;
        this.questionPool = questionPool;
        this.questionOrderedInfoFilename = questionOrderedInfoFilename;
    }

    public String getCohortId(){ return cohortId;}
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
        if (!this.cohortId.equals(((CohortRecord) otherObj).cohortId)) return false;
        assert this.studentIDs != null;
        return this.studentIDs.equals(other.studentIDs);
    }

    public static CohortRecord makeCohortRecordFromCohort(Cohort cohortIn){
        TaskGenerator taskGenerator = cohortIn.getTaskGenerator();
        MessageGenerator messageGenerator = cohortIn.getMessageGenerator();


        if(taskGenerator != null){

            if (taskGenerator instanceof RandomTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.randomTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.randomTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.randomTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
                }
            } else if (taskGenerator instanceof OrderedTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.orderedTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool(), cohortIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.orderedTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool(), cohortIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.orderedTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
                }
            } else if (taskGenerator instanceof LevelTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.levelTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.levelTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.levelTaskGenerator, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
                }
            }
            else if (taskGenerator instanceof LevelTaskGeneratorAttachment) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.levelTaskGeneratorAttachment, cohortIn.getStudentIDs(), MessageGeneratorType.silentMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.levelTaskGeneratorAttachment, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGenerator, cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGeneratorAttachment){
                    return new CohortRecord(cohortIn.getCohortId(), TaskGeneratorType.levelTaskGeneratorAttachment, cohortIn.getStudentIDs(), MessageGeneratorType.levelMessageGeneratorAttachment, cohortIn.getQuestionPool());
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
                    return new Cohort(cohortRecordIn.getCohortId(), new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(cohortRecordIn.getCohortId(), new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }

                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(cohortRecordIn.getCohortId(), new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
            case orderedTaskGenerator:
                List<QuestionOrderedInfo> forOTG = createQuestionOrderedInfoList(cohortRecordIn.getQuestionOrderedInfoFilename(), cohortRecordIn.getQuestionPool());
                if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                    return new Cohort(cohortRecordIn.getCohortId(), new OrderedTaskGenerator(cohortRecordIn.getQuestionPool(), forOTG), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool(), cohortRecordIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(cohortRecordIn.getCohortId(), new OrderedTaskGenerator(cohortRecordIn.getQuestionPool(), forOTG), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool(), cohortRecordIn.getQuestionOrderedInfoFilename());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(cohortRecordIn.getCohortId(), new OrderedTaskGenerator(cohortRecordIn.getQuestionPool(), forOTG), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
            case levelTaskGenerator:
                if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                    return new Cohort(cohortRecordIn.getCohortId(), new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(cohortRecordIn.getCohortId(), new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(cohortRecordIn.getCohortId(), new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
            case levelTaskGeneratorAttachment:
                if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                    return new Cohort(cohortRecordIn.getCohortId(), new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                    return new Cohort(cohortRecordIn.getCohortId(), new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                    return new Cohort(cohortRecordIn.getCohortId(), new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGeneratorAttachment(), cohortRecordIn.getQuestionPool());
                }
                break;
        }
        return null;
    }

    public static JSONCohortDatastore makeCohortDatastoreFromCohortRecords(String cohortRecordFilepath, String defaultCohortRecordReadOnlyFilepath, JsonIoHelper jsonIoHelper) throws IOException{
        JsonIoUtil jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        List<CohortRecord> cohortRecordList = jsonIoUtil.listFromFileOrCopyFromReadOnlyFile(cohortRecordFilepath, defaultCohortRecordReadOnlyFilepath, CohortRecord.class);
        return makeCohortDatastoreFromCohortRecords(cohortRecordList, cohortRecordFilepath, jsonIoHelper);
    }

    public static JSONCohortDatastore makeCohortDatastoreFromCohortRecords(List<CohortRecord> cohortRecordsList, String cohortDatastoreFilename, JsonIoHelper jsonIoHelper) {
        JSONCohortDatastore toReturn = new JSONCohortDatastore(cohortDatastoreFilename, CohortRecord.makeCohortFromCohortRecord(cohortRecordsList.get(0)), jsonIoHelper);

        for (int i = 1; i < cohortRecordsList.size(); i++) {
            CohortRecord cohortRecord = cohortRecordsList.get(i);
            String cohortId = cohortRecord.getCohortId();
            TaskGeneratorType taskGeneratorType = cohortRecord.getTaskGeneratorType();
            MessageGeneratorType messageGeneratorType = cohortRecord.getMessageGeneratorType();
            List<String> studentIDs = cohortRecord.getStudentIDs();

            switch (taskGeneratorType) {
                case randomTaskGenerator:
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(cohortId, new RandomTaskGenerator(), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(cohortId, new RandomTaskGenerator(), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(cohortId, new RandomTaskGenerator(), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
                case orderedTaskGenerator:
                    List<QuestionOrderedInfo> forOTG = createQuestionOrderedInfoList(cohortRecord.getQuestionOrderedInfoFilename(), cohortRecord.getQuestionPool());
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(cohortId, new OrderedTaskGenerator(cohortRecord.getQuestionPool(), forOTG), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool(), cohortRecord.getQuestionOrderedInfoFilename());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(cohortId, new OrderedTaskGenerator(cohortRecord.getQuestionPool(), forOTG), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool(), cohortRecord.getQuestionOrderedInfoFilename());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(cohortId, new OrderedTaskGenerator(cohortRecord.getQuestionPool(), forOTG), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
                case levelTaskGenerator:
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(cohortId, new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(cohortId, new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(cohortId, new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
                case levelTaskGeneratorAttachment:
                    if (messageGeneratorType.equals(MessageGeneratorType.silentMessageGenerator)){
                        toReturn.addCohort(cohortId, new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGenerator)){
                        toReturn.addCohort(cohortId, new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals(MessageGeneratorType.levelMessageGeneratorAttachment)){
                        toReturn.addCohort(cohortId, new LevelTaskGeneratorAttachment(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGeneratorAttachment(), cohortRecord.getQuestionPool());
                    }
                    break;
            }
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
            System.out.println("File likely not found: " + questionOrderedListFilename + ". Creating a default QuestionOrderedInfoList");
            e.printStackTrace();
            return OrderedTaskGenerator.createDefaultQuestionOrderedInfoList(questionPool, true);
        }
    }
}

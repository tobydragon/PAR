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
    private final String taskGeneratorType;
    private final List<String> studentIDs;
    private final String messageGeneratorType;
    private final QuestionPool questionPool;
    //TODO add QuestionOrderedInfo filename
        //TODO if cant find file, create a default and print a warning

    public CohortRecord() {
        this.taskGeneratorType = null;
        this.studentIDs = null;
        this.messageGeneratorType = null;
        this.questionPool = null;
    }

    public CohortRecord(String taskGeneratorType, List<String> studentIDs, String messageGeneratorType, QuestionPool questionPool){
        this.taskGeneratorType = taskGeneratorType;
        this.studentIDs = studentIDs;
        this.messageGeneratorType = messageGeneratorType;
        this.questionPool = questionPool;
    }

    public String getTaskGeneratorType() {
        return taskGeneratorType;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }
    public String getMessageGeneratorType(){ return messageGeneratorType; }
    public QuestionPool getQuestionPool(){
        return questionPool;
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
        assert this.studentIDs != null;
        return this.studentIDs.equals(other.studentIDs);
    }

    public static CohortRecord makeCohortRecordFromCohort(Cohort cohortIn){
        TaskGenerator taskGenerator = cohortIn.getTaskGenerator();
        MessageGenerator messageGenerator = cohortIn.getMessageGenerator();


        if(taskGenerator != null){

            if (taskGenerator instanceof RandomTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord("RandomTaskGenerator", cohortIn.getStudentIDs(), "SilentMessageGenerator", cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord("RandomTaskGenerator", cohortIn.getStudentIDs(), "LevelMessageGenerator", cohortIn.getQuestionPool());
                }
            } else if (taskGenerator instanceof OrderedTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord("OrderedTaskGenerator", cohortIn.getStudentIDs(), "SilentMessageGenerator", cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord("OrderedTaskGenerator", cohortIn.getStudentIDs(), "LevelMessageGenerator", cohortIn.getQuestionPool());
                }
            } else if (taskGenerator instanceof LevelTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord("LevelTaskGenerator", cohortIn.getStudentIDs(), "SilentMessageGenerator", cohortIn.getQuestionPool());
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord("LevelTaskGenerator", cohortIn.getStudentIDs(), "LevelMessageGenerator", cohortIn.getQuestionPool());
                }
            }
        }
        return null;
    }

    public static Cohort makeCohortFromCohortRecord(CohortRecord cohortRecordIn) throws IOException {
        String taskGeneratorType = cohortRecordIn.getTaskGeneratorType();
        String messageGeneratorType = cohortRecordIn.getMessageGeneratorType();

        switch (taskGeneratorType) {
            case "RandomTaskGenerator":
                if (messageGeneratorType.equals("SilentMessageGenerator")){
                    return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals("LevelMessageGenerator")){
                    return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                break;
            case "OrderedTaskGenerator":
                JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
                //TODO this should not reference resource files, should be questionPool
                QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
                List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
                if (messageGeneratorType.equals("SilentMessageGenerator")){
                    return new Cohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals("LevelMessageGenerator")){
                    return new Cohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                break;
            case "LevelTaskGenerator":
                if (messageGeneratorType.equals("SilentMessageGenerator")){
                    return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                else if (messageGeneratorType.equals("LevelMessageGenerator")){
                    return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator(), cohortRecordIn.getQuestionPool());
                }
                break;
        }
        return null;
    }

    public static JSONCohortDatastore makeCohortDatastoreFromCohortRecords(List<CohortRecord> cohortRecordsList) throws IOException {
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        //TODO the next two lines change if test or production
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/main/resources/author/defaultQuestionPool.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/main/resources/author/orderedQuestionInfo/currentOrderedQuestionInfoList.json", QuestionOrderedInfo.class);
//        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
//        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);

        JSONCohortDatastore toReturn = new JSONCohortDatastore();

        for (CohortRecord cohortRecord : cohortRecordsList) {
            String taskGeneratorType = cohortRecord.getTaskGeneratorType();
            String messageGeneratorType = cohortRecord.getMessageGeneratorType();
            List<String> studentIDs = cohortRecord.getStudentIDs();

            switch (taskGeneratorType) {
                case "RandomTaskGenerator":
                    if (messageGeneratorType.equals("SilentMessageGenerator")){
                        toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals("LevelMessageGenerator")){
                        toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    break;
                case "OrderedTaskGenerator":
                    if (messageGeneratorType.equals("SilentMessageGenerator")){
                        toReturn.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals("LevelMessageGenerator")){
                        toReturn.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    break;
                case "LevelTaskGenerator":
                    if (messageGeneratorType.equals("SilentMessageGenerator")){
                        toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new SilentMessageGenerator(), cohortRecord.getQuestionPool());
                    }
                    else if (messageGeneratorType.equals("LevelMessageGenerator")){
                        toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator(), cohortRecord.getQuestionPool());
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
}

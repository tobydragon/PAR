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

    public CohortRecord() {
        this.taskGeneratorType = null;
        this.studentIDs = null;
        this.messageGeneratorType = null;
    }

    public CohortRecord(String taskGeneratorType, List<String> studentIDs, String messageGeneratorType){
        this.taskGeneratorType = taskGeneratorType;
        this.studentIDs = studentIDs;
        this.messageGeneratorType = messageGeneratorType;
    }

    public String getTaskGeneratorType() {
        return taskGeneratorType;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }
    public String getMessageGeneratorType(){ return messageGeneratorType; }

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
        if (!this.messageGeneratorType.equals(other.getMessageGeneratorType())) return false;
        assert this.taskGeneratorType != null;
        return this.taskGeneratorType.equals(other.taskGeneratorType) && this.studentIDs.equals(other.studentIDs);
    }

    public static CohortRecord makeCohortRecordFromCohort(Cohort cohortIn){
        TaskGenerator taskGenerator = cohortIn.getTaskGenerator();
        MessageGenerator messageGenerator = cohortIn.getMessageGenerator();


        if(taskGenerator != null){

            if (taskGenerator instanceof RandomTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord("RandomTaskGenerator", cohortIn.getStudentIDs(), "SilentMessageGenerator");
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord("RandomTaskGenerator", cohortIn.getStudentIDs(), "LevelMessageGenerator");
                }
            } else if (taskGenerator instanceof OrderedTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord("OrderedTaskGenerator", cohortIn.getStudentIDs(), "SilentMessageGenerator");
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord("OrderedTaskGenerator", cohortIn.getStudentIDs(), "LevelMessageGenerator");
                }
            } else if (taskGenerator instanceof LevelTaskGenerator) {
                if (messageGenerator instanceof SilentMessageGenerator){
                    return new CohortRecord("LevelTaskGenerator", cohortIn.getStudentIDs(), "SilentMessageGenerator");
                }
                else if (messageGenerator instanceof LevelMessageGenerator){
                    return new CohortRecord("LevelTaskGenerator", cohortIn.getStudentIDs(), "LevelMessageGenerator");
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
                    return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator());
                }
                else if (messageGeneratorType.equals("LevelMessageGenerator")){
                    return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator());
                }

            case "OrderedTaskGenerator":
                JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
                QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
                List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
                if (messageGeneratorType.equals("SilentMessageGenerator")){
                    return new Cohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator());
                }
                else if (messageGeneratorType.equals("LevelMessageGenerator")){
                    return new Cohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator());
                }
            case "LevelTaskGenerator":
                if (messageGeneratorType.equals("SilentMessageGenerator")){
                    return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator());
                }
                else if (messageGeneratorType.equals("LevelMessageGenerator")){
                    return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator());
                }
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
                        toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new SilentMessageGenerator());
                    }
                    else if (messageGeneratorType.equals("LevelMessageGenerator")){
                        toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new LevelMessageGenerator());
                    }
                    break;
                case "OrderedTaskGenerator":
                    if (messageGeneratorType.equals("SilentMessageGenerator")){
                        toReturn.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs, new SilentMessageGenerator());
                    }
                    else if (messageGeneratorType.equals("LevelMessageGenerator")){
                        toReturn.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs, new LevelMessageGenerator());
                    }
                    break;
                case "LevelTaskGenerator":
                    if (messageGeneratorType.equals("SilentMessageGenerator")){
                        toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new SilentMessageGenerator());
                    }
                    else if (messageGeneratorType.equals("LevelMessageGenerator")){
                        toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator());
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

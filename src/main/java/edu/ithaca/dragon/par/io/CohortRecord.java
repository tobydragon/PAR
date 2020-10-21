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

    public CohortRecord(String taskGeneratorType, List<String> studentIDs){
        this.taskGeneratorType = taskGeneratorType;
        this.studentIDs = studentIDs;
    }

    public String getTaskGeneratorType() {
        return taskGeneratorType;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    public static CohortRecord makeCohortRecordFromCohort(Cohort cohortIn){
        TaskGenerator taskGenerator = cohortIn.getTaskGenerator();

        if(taskGenerator != null){
            if(taskGenerator instanceof RandomTaskGenerator){
                return new CohortRecord("RandomTaskGenerator", cohortIn.getStudentIDs());
            } else if (taskGenerator instanceof OrderedTaskGenerator){
                return new CohortRecord("OrderedTaskGenerator", cohortIn.getStudentIDs());
            } else if (taskGenerator instanceof LevelTaskGenerator){
                return new CohortRecord("LevelTaskGenerator", cohortIn.getStudentIDs());
            }
        }
        return null;
    }

    public static Cohort makeCohortFromCohortRecord(CohortRecord cohortRecordIn) throws IOException {
        String taskGeneratorType = cohortRecordIn.getTaskGeneratorType();
        switch (taskGeneratorType) {
            case "RandomTaskGenerator":
                return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator());

            case "OrderedTaskGenerator":
                JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
                QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
                List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
                return new Cohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), cohortRecordIn.getStudentIDs(), new SilentMessageGenerator());

            case "LevelTaskGenerator":
                return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs(), new LevelMessageGenerator());
        }
        return null;
    }

    public static JSONCohortDatastore makeCohortDatastoreFromCohortRecords(List<CohortRecord> cohortRecordsList) throws IOException {
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);

        JSONCohortDatastore toReturn = new JSONCohortDatastore();

        for (CohortRecord cohortRecord : cohortRecordsList) {
            String taskGeneratorType = cohortRecord.getTaskGeneratorType();
            List<String> studentIDs = cohortRecord.getStudentIDs();

            switch (taskGeneratorType) {
                case "RandomTaskGenerator":
                    toReturn.addCohort(new RandomTaskGenerator(), studentIDs, new SilentMessageGenerator());
                    break;
                case "LevelTaskGenerator":
                    toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator());
                    break;
                case "OrderedTaskGenerator":
                    toReturn.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs, new SilentMessageGenerator());
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

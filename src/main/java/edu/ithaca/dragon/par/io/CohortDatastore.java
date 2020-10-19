package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.OrderedTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.RandomTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.IOException;
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

    public static CohortDatastore makeCohortDatastoreFromCohortRecords(List<CohortRecord> cohortRecordsList) throws IOException {
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);

        CohortDatastore toReturn = new CohortDatastore();

        for (CohortRecord cohortRecord : cohortRecordsList) {
            String taskGeneratorType = cohortRecord.getTaskGeneratorType();
            List<String> studentIDs = cohortRecord.getStudentIDs();

            switch (taskGeneratorType) {
                case "RandomTaskGenerator":
                    toReturn.addCohort(new RandomTaskGenerator(), studentIDs);
                    break;
                case "LevelTaskGenerator":
                    toReturn.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs);
                    break;
                case "OrderedTaskGenerator":
                    toReturn.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs);
                    break;
            }
        }
        return toReturn;
    }

    public List<CohortRecord> makeCohortRecordsFromCohortDatastore() {
        return null;
    }
}

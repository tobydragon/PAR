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
                return new Cohort(new RandomTaskGenerator(), cohortRecordIn.getStudentIDs());

            case "OrderedTaskGenerator":
                JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
                QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
                List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
                return new Cohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), cohortRecordIn.getStudentIDs());

            case "LevelTaskGenerator":
                return new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), cohortRecordIn.getStudentIDs());
        }
        return null;
    }
}

package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.OrderedTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.RandomTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;

public class CohortRecord {
    private String taskGeneratorType;
    private List<String> studentIDs;

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

    public static Cohort makeCohortFromCohortRecord(CohortRecord cohortRecordIn){
        return null;
    }
}

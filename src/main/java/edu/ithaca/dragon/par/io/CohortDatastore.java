package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;

public interface CohortDatastore {

    int getNumberCohorts();
    int getTotalNumberStudents();
    List<Cohort> getMasterCohortList();
    TaskGenerator getTaskGeneratorFromStudentID(String studentIDIn);
    MessageGenerator getMessageGeneratorFromStudentID(String studentIDIn);
}

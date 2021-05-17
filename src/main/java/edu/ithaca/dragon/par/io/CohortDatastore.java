package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.io.IOException;
import java.util.List;

public interface CohortDatastore {

    int getNumberCohorts();
    int getTotalNumberStudents();
    List<Cohort> getMasterCohortList();
    TaskGenerator getTaskGeneratorFromStudentID(String studentIDIn) throws IOException;
    MessageGenerator getMessageGeneratorFromStudentID(String studentIDIn) throws IOException;
    String getCohortDatastoreFilename();
    Cohort getCohortById(String cohortIdIn);
}

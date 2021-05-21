package edu.ithaca.dragon.par.deprecated.pedagogicalModel;

import edu.ithaca.dragon.par.deprecated.io.ImageTask;
import edu.ithaca.dragon.par.deprecated.studentModel.StudentModel;

public interface TaskGenerator {
    ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis);
}

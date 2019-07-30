package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

public interface TaskGenerator {
    ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis);
}

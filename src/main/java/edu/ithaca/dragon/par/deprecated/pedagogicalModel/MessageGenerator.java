package edu.ithaca.dragon.par.deprecated.pedagogicalModel;

import edu.ithaca.dragon.par.deprecated.io.ImageTask;
import edu.ithaca.dragon.par.deprecated.studentModel.StudentModel;


public interface MessageGenerator {
    public String generateMessage(StudentModel studentModel, ImageTask imageTask);
}

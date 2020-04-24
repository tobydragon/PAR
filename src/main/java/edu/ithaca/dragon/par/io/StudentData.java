package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

public class StudentData {

    private String studentId;
    private int level;
    private int numQuestionsAnswered;
    //private double timeSpent; //minutes?

    public StudentData(StudentModel student){


    }

    public String getUserId() {
        return studentId;
    }

    public int getLevel() {
        return level;
    }

    public int getNumQuestionsAnswered() {
        return numQuestionsAnswered;
    }
}

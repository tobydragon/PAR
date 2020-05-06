package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

public class MessageGenerator {
    public static void generateMessage(StudentModel studentModel, ImageTask imageTask, int questionCountPerTypeForAnalysis){
        int level = LevelTaskGenerator.calcLevel(studentModel.calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis));
        //mastered student
        if (level == 7){
            imageTask.setMessage("You have mastered the material, feel free to keep practicing");
        }
    }
}

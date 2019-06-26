package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskGenerator {
    public static ImageTask makeTaskWithSingleQuestion(StudentModel studentModel){

        //there are questions the student has not seen
        if(studentModel.getUnseenQuestionCount()>0){
            //make and return an imageTask with the first question from the studentModels.unseenQuestions
            Question firstQuestionFromUnseen = studentModel.getUserQuestionSet().getUnseenQuestions().get(0);
            ImageTask imageTask = new ImageTask(firstQuestionFromUnseen.getImageUrl(), Arrays.asList(firstQuestionFromUnseen));
            return imageTask;
        }

        //the student has seen every question, get a question that they have already seen
        //TODO: because this takes the first question from seenQuestions, will it always take the first question? Maybe this should be random to stop repetition?
        Question firstQuestionFromSeen = studentModel.getUserQuestionSet().getSeenQuestions().get(0);
        ImageTask imageTask = new ImageTask(firstQuestionFromSeen.getImageUrl(), Arrays.asList(firstQuestionFromSeen));
        return imageTask;
    }
}

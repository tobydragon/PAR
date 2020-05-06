package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.List;

public class MessageGenerator {
    public static void generateMessage(StudentModel studentModel, ImageTask imageTask, int previousLevel){
        int level = studentModel.getLevel();

        //mastered student
        if (level == 7){
            //repeating questions
            List<Question> allQuestions = studentModel.getUserQuestionSet().getAllQuestions();
            int index = -1;
            for (int i = 0; i < allQuestions.size(); i++){
                //there should only be one zone question in this task
                if (allQuestions.get(i).getId().equals(imageTask.getTaskQuestions().get(0).getId())){
                    index = i;
                }
            }
            //question found
            if (index != -1){
                //question has been seen before
                if (studentModel.getUserQuestionSet().getTimesSeen(allQuestions.get(index).getId())>0){
                    imageTask.setMessage("You've mastered the material and started repeating questions");
                }
                //not seen yet
                else {
                    imageTask.setMessage("You have mastered the material, feel free to keep practicing");
                }
            }
            //if not found, message is null
        }

        //down level
        else if (previousLevel-level > 0 && previousLevel != -1){
            List<String> questionsInPreviousLevel = EquineQuestionTypes.getTypesForLevel(previousLevel);
            String questionsOneString = "";
            for(String q: questionsInPreviousLevel){
                questionsOneString += q + "/";
            }
            questionsOneString = questionsOneString.substring(0,questionsOneString.length()-1);
            imageTask.setMessage("Looks like you're having trouble with " +  questionsOneString + " questions, go look at resources and come back if you need to");
        }

        //up level
        else if (previousLevel-level < 0 && previousLevel != -1){
            String questionsInCurrentLevel = "hi";
            imageTask.setMessage("You're doing great!");
        }

        else{
            imageTask.setMessage("No message to display");
        }

        //stayed on same level, not mastered
    }
}

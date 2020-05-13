package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.List;

public class MessageGenerator {
    public static void generateMessage(StudentModel studentModel, ImageTask imageTask, int previousLevel){
        int level = studentModel.getLastLevelRecorded();
        imageTask.setMessage(null);

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
                //get index of first question
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


        else{
            //down level
            //if the student has just decreased in level, this will set the appropriate message.
            decreaseLevelMessage(studentModel, imageTask, previousLevel);

            //up level
            if (imageTask.getMessage() == null){
                increaseLevelMessage(studentModel, imageTask, previousLevel);
            }

            //stayed on same level, not mastered


        }



//        //stayed on same level, not mastered
//        else if (previousLevel-level == 0){
//            //get index of first question
//            int ind = -1;
//            List<Question> allQuestions = studentModel.getUserQuestionSet().getAllQuestions();
//            for (int i = 0; i < allQuestions.size(); i++){
//                if (allQuestions.get(i).getId().equals(imageTask.getTaskQuestions().get(0).getId())){
//                    ind = i;
//                }
//            }
//            if(ind!=-1){
//                ResponsesPerQuestion responsesPerQuestion = studentModel.getUserResponseSet().getResponseById(allQuestions.get(ind).getId());
//                //no responses
//                if (responsesPerQuestion.getAllResponses().size()<1){
//                    imageTask.setMessage("No message to display");
//                }
//                else{
//                    //current milliseconds
//
//                    //second most recent response milliseconds
//                    long sec2 =responsesPerQuestion.getAllResponses().get(responsesPerQuestion.getAllResponses().size()-2).getMillSeconds();
//
//                    //seen within last hour
//                    Date date=new Date();
//                    if (date.getTime()-sec2>360000){
//                        List<String> questionsInLevel = EquineQuestionTypes.getTypesForLevel(level);
//                        String questionsOneString = "";
//                        for(String q: questionsInLevel){
//                            questionsOneString += q + "/";
//                        }
//                        questionsOneString = questionsOneString.substring(0,questionsOneString.length()-1);
//                        imageTask.setMessage("You've seen this question recently, you might be stuck on "+questionsOneString+" questions.");
//                    }
//                }
//            }
//
//        }


    }

    public static void decreaseLevelMessage(StudentModel studentModel, ImageTask imageTask, int previousLevel){
        int level = studentModel.getLastLevelRecorded();
        if (previousLevel-level > 0 && previousLevel != -1){
            List<String> questionsInPreviousLevel = EquineQuestionTypes.getTypesForLevel(previousLevel);
            String questionsOneString = "";
            for(String q: questionsInPreviousLevel){
                questionsOneString += q + "/";
            }
            questionsOneString = questionsOneString.substring(0,questionsOneString.length()-1);
            imageTask.setMessage("Looks like you're having trouble with " +  questionsOneString + " questions, go look at resources and come back if you need to");
        }
        else{
            imageTask.setMessage(null);
        }
    }

    public static void increaseLevelMessage(StudentModel studentModel, ImageTask imageTask, int previousLevel) {
        int level = studentModel.getLastLevelRecorded();
        if (previousLevel-level < 0 && previousLevel != -1){
            imageTask.setMessage("You're doing great!");
        }
        else{
            imageTask.setMessage(null);
        }
    }
}

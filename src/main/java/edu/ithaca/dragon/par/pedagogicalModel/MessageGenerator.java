package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.Date;
import java.util.List;

public class MessageGenerator {
    private static final String increaseLevelString = "You're doing great!";
    private static final String masterRepeat = "You've mastered the material and started repeating questions";
    private static final String masterNoRepeat = "You have mastered the material, feel free to keep practicing";
    private static final String troublePart1 = "Looks like you're having trouble with ";
    private static final String troublePart2 = " questions, go look at resources and come back if you need to";
    private static final String repeatPart1 = "You've seen this question recently, you might be stuck on ";


    private static final int repeatWindow = 1800000; //30 min in mili
    public static String generateMessage(StudentModel studentModel, ImageTask imageTask) {
        int currentLevel = studentModel.getCurrentLevel();

        String message = null;

        //new student model
        if (currentLevel < 1) {
            return message;
        }
        else {

            //mastered student message
            if (currentLevel == 7) {
                message = level7Message(studentModel, imageTask);
            } else {
                //down level
                //if the student has just decreased in level, this will set the appropriate message.
                message = decreaseLevelMessage(studentModel);

                //up level
                if (message == null) {
                    message = increaseLevelMessage(studentModel);
                }

                //stayed on same level, not mastered
                if (message == null) {
                    message = repeatLevelMessage(studentModel, imageTask);
                }

            }
            return message;

        }
    }

    public static String decreaseLevelMessage(StudentModel studentModel){
        if (studentModel.getPreviousLevel()- studentModel.getCurrentLevel() > 0){
            List<String> questionsInPreviousLevel = EquineQuestionTypes.getTypesForLevel(studentModel.getPreviousLevel());
            String questionsOneString = "";
            for(String q: questionsInPreviousLevel){
                questionsOneString += q + "/";
            }
            questionsOneString = questionsOneString.substring(0,questionsOneString.length()-1);
            return (troublePart1 +  questionsOneString + troublePart2);
        }
        else{
            return null;
        }
    }

    public static String increaseLevelMessage(StudentModel studentModel) {
        int currentLevel = studentModel.getCurrentLevel();
        if (studentModel.getPreviousLevel()-currentLevel < 0){
            return increaseLevelString;
        }
        else{
            return null;
        }
    }

    public static String level7Message(StudentModel studentModel, ImageTask imageTask){
        int currentLevel = studentModel.getCurrentLevel();

        //check level
        if (currentLevel != 7){
            return null;
        }

        try{
            ResponsesPerQuestion responses = studentModel.getUserResponseSet().getResponseById(imageTask.getTaskQuestions().get(0).getId());

            long millisLastTimeSeen = responses.getAllResponses().get(responses.getAllResponses().size()-1).getMillSeconds();
            Date date = new Date();

            if (date.getTime() - millisLastTimeSeen < repeatWindow){
                return masterRepeat;
            }
            return masterNoRepeat;

        }
        catch (IllegalArgumentException e){ //no responses yet, hasn't been repeated.
            return masterNoRepeat;
        }

//
//        int index = -1;
//        for (int i = 0; i < allQuestions.size(); i++){
//            //there should only be one zone question in this task
//            if (allQuestions.get(i).getId().equals(imageTask.getTaskQuestions().get(0).getId())){
//                if (studentModel.getUserResponseSet().getResponseById(allQuestions.get()))
//            }
//        }
//        //question found
//        if (index != -1){
//            //get index of first question
//            if (studentModel.getUserQuestionSet().getTimesSeen(allQuestions.get(index).getId())>0){
//                return masterRepeat;
//            }
//            //not seen yet
//            else {
//                return masterNoRepeat;
//            }
//        }
//        //not found, message is null
//        else{
//            return null;
//        }
    }

    public static String repeatLevelMessage(StudentModel studentModel, ImageTask imageTask){
        int currentLevel = studentModel.getCurrentLevel();
        int previousLevel = studentModel.getPreviousLevel();

        //stayed on same level, not mastered
        if (previousLevel == currentLevel){
            //get index of first question
            int ind = -1;
            List<ResponsesPerQuestion> allResponses = studentModel.getUserResponseSet().getResponsesPerQuestionList();
            for (int i = 0; i < allResponses.size(); i++){
                //if the id of the current question equals the first in the task
                if (allResponses.get(i).getQuestionId().equals(imageTask.getTaskQuestions().get(0).getId())){
                    ind = i;
                }
            }
            if(ind!=-1){
                ResponsesPerQuestion responsesPerQuestion = studentModel.getUserResponseSet().getResponseById(allResponses.get(ind).getQuestionId());
                //no responses
                if (responsesPerQuestion.getAllResponses().size()<1){
                    return null;
                }
                else {
                    //if there's at least one response
                    if (responsesPerQuestion.getAllResponses().size() >= 1) {
                        //most recent response milliseconds
                        long sec2 = responsesPerQuestion.getAllResponses().get(responsesPerQuestion.getAllResponses().size() - 1).getMillSeconds();

                        //seen within the time window
                        Date date = new Date();
                        if (date.getTime() - sec2 <= repeatWindow) {
                            List<String> questionsInLevel = EquineQuestionTypes.getTypesForLevel(currentLevel);
                            String questionsOneString = "";
                            for (String q : questionsInLevel) {
                                questionsOneString += q + "/";
                            }
                            questionsOneString = questionsOneString.substring(0, questionsOneString.length() - 1);
                            return (repeatPart1 + questionsOneString + " questions.");
                        }
                    }
                    else{
                        return null;
                    }
                }
            }
            else{
                return null;
            }

        }
        else{
            return null;
        }
    return null;}
}

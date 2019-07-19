package edu.ithaca.dragon.par.pedagogicalModel;

import java.util.Map;

public class ImageTaskSettings {
     private boolean unsureShowsCorrectAnswer;
     private Map<String, String> feedbackByType;
     private boolean ableToResubmitAnswers;
     private Boolean mustSubmitAnswersToContinue;
     private Boolean canGiveNoAnswer;
     private Boolean willDisplayFeedback;


     public boolean getUnsureShowsCorrectAnswer(){
         return unsureShowsCorrectAnswer;
     }

     public Map<String, String> getFeedbackByType(){
        return feedbackByType;
     }

     public boolean getableToResubmitAnswers(){ return ableToResubmitAnswers; }

     public boolean getMustSubmitAnswersToContinue(){ return mustSubmitAnswersToContinue; }

     public boolean getCanGiveNoAnswer(){ return canGiveNoAnswer; }

    public boolean getWillDisplayFeedback(){ return willDisplayFeedback; }


    public void setUnsureShowsCorrectAnswer(boolean setNew){
        unsureShowsCorrectAnswer= setNew;
    }

    public void setFeedbackByType(Map<String, String> setNew){
        feedbackByType= setNew;
    }

    public void setableToResubmitAnswers(boolean setNew){
        ableToResubmitAnswers= setNew;
    }

    public void setMustSubmitAnswersToContinue(boolean setNew){
        mustSubmitAnswersToContinue= setNew;
    }

    public void setCanGiveNoAnswer(boolean setNew){
        canGiveNoAnswer= setNew;
    }

    public void setWillDisplayFeedback(boolean setNew){
        willDisplayFeedback= setNew;
    }
}

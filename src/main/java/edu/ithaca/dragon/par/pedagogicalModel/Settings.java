package edu.ithaca.dragon.par.pedagogicalModel;

import java.util.Map;

public class Settings {
     private boolean unsureShowsCorrectAnswer;
     private Map<String, String> feedbackByType;
     private boolean ableToResubmitAnswers;
     private String scoreType;
     private Boolean showScore;

     public Settings(){

     }

     public boolean getUnsureShowsCorrectAnswer(){
         return unsureShowsCorrectAnswer;
     }

     public Map<String, String> getFeedbackByType(){
        return feedbackByType;
     }

    public boolean getableToResubmitAnswers(){
        return ableToResubmitAnswers;
    }

     public String getScoreType(){
        return scoreType;
     }

     public boolean getShowScore(){
        return showScore;
     }

    public void setUnsureShowsCorrectAnswer(boolean setNew){
        unsureShowsCorrectAnswer= setNew;
    }

    public void setFeedbackByType(Map<String, String> setNew){
        feedbackByType= setNew;
    }

    public void setableToResubmitAnswers(boolean setNew){
        ableToResubmitAnswers= setNew;
    }

    public void setScoreType(String setNew){
        scoreType= setNew;
    }

    public void setShowScore(boolean setNew){
        showScore= setNew;
    }
}

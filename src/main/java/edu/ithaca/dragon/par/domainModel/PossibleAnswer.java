package edu.ithaca.dragon.par.domainModel;

import java.util.Objects;

public class PossibleAnswer {

    private String answerText;
    private String feedbackText;

    public PossibleAnswer(String answerTextIn, String feedbackIn){
        answerText = answerTextIn;
        feedbackText = feedbackIn;
    }
    public PossibleAnswer(String answerTextIn){
        answerText = answerTextIn;
        feedbackText = null;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    @Override
    public String toString() {
        return "PossibleAnswer [answerText=" + answerText + ", feedbackText=" + feedbackText + "]";
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!PossibleAnswer.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        PossibleAnswer other = (PossibleAnswer) otherObj;
        if (!Objects.equals(this.getAnswerText(), other.getAnswerText())) {
            return false;
        }
        if (!Objects.equals(this.getFeedbackText(), other.getFeedbackText())) {
            return false;
        }
        return true;
    }
}

package edu.ithaca.dragon.par.domainModel;

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
}

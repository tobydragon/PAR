package edu.ithaca.dragon.par.domainModel;

import java.util.List;

public class QuestionWithFollowup extends Question {

    private List<Question> followupQuestions;

    public QuestionWithFollowup(String idIn, String questionTextIn, String typeIn, String correctAnswerIn, List<String> answersIn, String imageUrlIn, List<Question> followupQuestionsIn){
        super(idIn, questionTextIn, typeIn, correctAnswerIn, answersIn, imageUrlIn);
        this.followupQuestions = followupQuestionsIn;
    }

    public void setFollowupQuestions(List<Question> followupQuestionsIn){ followupQuestions = followupQuestionsIn; }
    public List<Question> getFollowupQuestions(){ return followupQuestions; }
}

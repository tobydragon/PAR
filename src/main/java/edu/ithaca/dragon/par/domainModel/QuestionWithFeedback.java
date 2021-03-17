package edu.ithaca.dragon.par.domainModel;

import java.util.List;

public class QuestionWithFeedback {

    private Question question;
    private String feedback;

    public QuestionWithFeedback(){
        this.question = null;
        this.feedback = null;
    }

    public QuestionWithFeedback(Question questionIn){
        this.question = questionIn;
        this.feedback = null;
    }

    public QuestionWithFeedback(Question questionIn, String feedbackIn){
        this.question = questionIn;
        this.feedback = feedbackIn;
    }

    public Question getQuestion(){ return question;}
    public String getFeedback(){ return feedback;}

    public String getQuestionId() {return question.getId();}
    public String getQuestionText() { return question.getQuestionText(); }
    public String getQuestionType() { return question.getType(); }
    public String getQuestionCorrectAnswer() {return question.getCorrectAnswer();}
    public List<String> getQuestionPossibleAnswers() {return question.getPossibleAnswers();}
    public String getQuestionImageUrl() {return question.getImageUrl();}

    public List<Question> getQuestionFollowupQuestions() {return question.getFollowupQuestions();}

}

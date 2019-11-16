package edu.ithaca.dragon.par.io;

public class QuestionResponseOOP {
    String questionId;
    String questionText;
    String answerText;

    QuestionResponseOOP(){}

    public QuestionResponseOOP(String questionIdIn, String responseTextIn){
        this.questionId = questionIdIn;
        this.answerText = responseTextIn;
    }

    // used for authoring custom questions
    public QuestionResponseOOP(String questionIdIn, String questionTextIn, String responseTextIn){
        this.questionId = questionIdIn;
        this.questionText = questionTextIn;
        this.answerText = responseTextIn;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String toString(){
        return questionId + ": " + answerText;
    }
}

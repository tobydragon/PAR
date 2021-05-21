package edu.ithaca.dragon.par.deprecated.io;

public class QuestionResponseOOP {
    String questionId;
    String questionText;
    String responseText;

    QuestionResponseOOP(){}

    public QuestionResponseOOP(String questionIdIn, String responseTextIn){
        this.questionId = questionIdIn;
        this.responseText = responseTextIn;
    }

    // used for authoring custom questions
    public QuestionResponseOOP(String questionIdIn, String questionTextIn, String responseTextIn){
        this.questionId = questionIdIn;
        this.questionText = questionTextIn;
        this.responseText = responseTextIn;
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

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String toString(){
        return questionId + ": " + responseText;
    }
}

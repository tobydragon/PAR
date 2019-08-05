package edu.ithaca.dragon.par.io;

public class QuestionResponseOOP {
    String questionId;
    String responseText;

    QuestionResponseOOP(String questionIdIn, String responseTextIn){

    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}

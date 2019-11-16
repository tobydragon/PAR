package edu.ithaca.dragon.par.studentModel;


import java.util.Date;

public class QuestionResponse {
    private String answerText;
    private long millSeconds;

    public QuestionResponse(){

    }

    public QuestionResponse(String responseTextIn){
        this.answerText =responseTextIn;
        Date date=new Date();
        this.millSeconds=date.getTime();
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public long getMillSeconds() {
        return millSeconds;
    }

    public void setMillSeconds(long seconds) {
        this.millSeconds = seconds;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!QuestionResponse.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        QuestionResponse other = (QuestionResponse) otherObj;
        return this.getMillSeconds()==(other.getMillSeconds())
                && this.getAnswerText().equals(other.getAnswerText());

    }
}

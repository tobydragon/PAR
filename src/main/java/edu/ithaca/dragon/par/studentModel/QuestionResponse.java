package edu.ithaca.dragon.par.studentModel;


import java.util.Date;

public class QuestionResponse {
    private String responseText;
    private long millSeconds;

    public QuestionResponse(String responseTextIn){
        this.responseText=responseTextIn;
        Date date=new Date();
        this.millSeconds=date.getTime();
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public long getMillSeconds() {
        return millSeconds;
    }

    public void setMillSeconds(int seconds) {
        this.millSeconds = seconds;
    }
}

package edu.ithaca.dragon.par.studentModel;


import java.util.Date;

public class QuestionResponse {
    private String responseText;
    private long millSeconds;

    public QuestionResponse(){

    }

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
                && this.getResponseText().equals(other.getResponseText());

    }
}

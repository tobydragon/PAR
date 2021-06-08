package edu.ithaca.dragon.par.student.json;

import java.util.Date;

public class Response {
    public String responseText;
    public long millSeconds;

    public Response(){ }

    public Response(String responseTextIn){
        this.responseText = responseTextIn;
        this.millSeconds = new Date().getTime();
    }

    public String getResponseText(){
        return responseText;
    }
}

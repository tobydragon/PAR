package edu.ithaca.dragon.par.student.json;

import java.util.Date;

public class Response {
    public String responseText;
    public long millSeconds;

    public Response(){ }

    public Response(String responseText){
        this(responseText, new Date().getTime());
    }

    public Response(String responseTextIn, Long timestamp){
        this.responseText = responseTextIn;
        this.millSeconds = timestamp;
    }

    public String getResponseText(){
        return responseText;
    }

}

package edu.ithaca.dragon.par.io;

import java.util.ArrayList;
import java.util.List;

public class ImageTaskResponse {
    private String userId;
    private List<String> taskQuestionIds;
    private List<String> responseTexts;

    public ImageTaskResponse(){
        this.userId=null;
        taskQuestionIds=new ArrayList<>();
        responseTexts=new ArrayList<>();
    }
    public ImageTaskResponse(String userIdIn,List<String> taskQuestionIdsIn,List<String> responseTextsIn){
        this.userId=userIdIn;
        taskQuestionIds=taskQuestionIdsIn;
        responseTexts=responseTextsIn;
    }


    public List<String> getTaskQuestionIds() {
        return taskQuestionIds;
    }
    public void setTaskQuestionIds(List<String> taskQuestionIds) {
        this.taskQuestionIds = taskQuestionIds;
    }


    public List<String> getResponseTexts() {
        return responseTexts;
    }
    public void setResponseTexts(List<String> responseTexts) {
        this.responseTexts = responseTexts;
    }


    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }



    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!ImageTaskResponse.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        ImageTaskResponse other = (ImageTaskResponse) otherObj;
        return this.getResponseTexts().equals(other.getResponseTexts())
                && this.getTaskQuestionIds().equals(other.getTaskQuestionIds())
                && this.getUserId().equals(other.getUserId());
    }



}

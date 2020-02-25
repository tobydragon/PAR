package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class ImageTaskResponseImp1 implements ImageTaskReponse{
    private String userId;
    private List<String> taskQuestionIds;
    private List<String> responseTexts;

    public ImageTaskResponseImp1(){
        this.userId=null;
        taskQuestionIds=new ArrayList<>();
        responseTexts=new ArrayList<>();
    }
    public ImageTaskResponseImp1(String userIdIn, List<String> taskQuestionIdsIn, List<String> responseTextsIn){
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

    public String findResponseToQuestion(Question question){
        for(int i=0; i<taskQuestionIds.size(); i++){
            if(taskQuestionIds.get(i).equals(question.getId())){
                return responseTexts.get(i);
            }
        }
        return null;
    }


    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!ImageTaskResponseImp1.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        ImageTaskResponseImp1 other = (ImageTaskResponseImp1) otherObj;
        return this.getResponseTexts().equals(other.getResponseTexts())
                && this.getTaskQuestionIds().equals(other.getTaskQuestionIds())
                && this.getUserId().equals(other.getUserId());
    }



}

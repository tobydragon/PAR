package edu.ithaca.dragon.par.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class ImageTaskResponseOOP{

    private String userId;
    private List<QuestionResponseOOP> questionResponses;

    public ImageTaskResponseOOP(){
        questionResponses = new ArrayList<>();
    }

    //constructor taken from ImageTaskResponseImp1
    public ImageTaskResponseOOP(String userIdIn, List<String> taskQuestionIdsIn, List<String> responseTextsIn){
        this.userId = userIdIn;
        this.questionResponses = new ArrayList<>();
        for(int i=0; i<taskQuestionIdsIn.size(); i++){
            questionResponses.add(new QuestionResponseOOP(taskQuestionIdsIn.get(i), responseTextsIn.get(i)));
        }
    }

    public List<QuestionResponseOOP> getQuestionResponses(){
        return questionResponses;
    }

    public void setQuestionResponses(List<QuestionResponseOOP> questionResponsesIn) {
        questionResponses = questionResponsesIn;
    }

    @JsonIgnore
    public List<String> getTaskQuestionIds() {
        List<String> questionIds = new ArrayList<>();
        for(QuestionResponseOOP questionResponseOOP : questionResponses){
            questionIds.add(questionResponseOOP.questionId);
        }
        return questionIds;
    }

    @JsonIgnore
    public List<String> getResponseTexts() {
        List<String> responseTexts = new ArrayList<>();
        for(QuestionResponseOOP questionResponseOOP : questionResponses){
            responseTexts.add(questionResponseOOP.responseText);
        }
        return responseTexts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String findResponseToQuestion(Question question) {
        for(QuestionResponseOOP questionResponseOOP : questionResponses) {
            if(question.getId().equals(questionResponseOOP.questionId)){
                return questionResponseOOP.responseText;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!ImageTaskResponseOOP.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        ImageTaskResponseOOP other = (ImageTaskResponseOOP) otherObj;
        return this.getResponseTexts().equals(other.getResponseTexts())
                && this.getTaskQuestionIds().equals(other.getTaskQuestionIds())
                && this.getUserId().equals(other.getUserId());
    }
}

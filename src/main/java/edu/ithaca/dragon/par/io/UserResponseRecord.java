package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.UserResponse;

public class UserResponseRecord {
    private String userId;
    private String questionId;
    private String responseText;
    //private Question question;

    public UserResponseRecord(){ }

    public UserResponseRecord(UserResponse userResponseIn){
        userId=userResponseIn.getUserId();
        questionId=userResponseIn.getQuestionId();
        responseText=userResponseIn.getResponseText();
    }
     public UserResponse buildUserResponse(QuestionPool questionPool){
        Question question =questionPool.getQuestionFromId(questionId);
        return new UserResponse(userId,question,responseText);
     }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

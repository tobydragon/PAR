package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
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

}

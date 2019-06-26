package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.UserResponseSet;

import java.util.ArrayList;
import java.util.List;

public class UserResponseSetRecord {
    private String userId;
    private List<UserResponseRecord> userResponseRecords;

    public UserResponseSetRecord(){
        userResponseRecords=new ArrayList<>();
    }
    public UserResponseSetRecord(UserResponseSet userResponseSet){
        this();
        userId=userResponseSet.getUserId();

        for(ResponsesPerQuestion currResponsesPerQuestion : userResponseSet.getUserResponse()){
            userResponseRecords.add(new UserResponseRecord(currResponsesPerQuestion));
        }
    }

    public UserResponseSet buildUserResponseSet(QuestionPool questionPool){
        List<ResponsesPerQuestion> responses=new ArrayList<>();
        for(UserResponseRecord currURR : userResponseRecords){
            responses.add(currURR.buildUserResponse(questionPool));
        }
        UserResponseSet URS=new UserResponseSet(userId);
        URS.addAllResponses(responses);
        return URS ;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserResponseRecord> getUserResponseRecords() {
        return userResponseRecords;
    }

    public void setUserResponseRecords(List<UserResponseRecord> userResponseRecords) {
        this.userResponseRecords = userResponseRecords;
    }


}

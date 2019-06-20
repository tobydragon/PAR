package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.UserResponse;
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

        for(UserResponse currUserResponse : userResponseSet.getUserResponses()){
            userResponseRecords.add(new UserResponseRecord(currUserResponse));
        }
    }

    public UserResponseSet buildUserResponseSet(QuestionPool questionPool){
        List<UserResponse> responses=new ArrayList<>();
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

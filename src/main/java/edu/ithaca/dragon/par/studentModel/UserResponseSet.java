package edu.ithaca.dragon.par.studentModel;

import java.util.ArrayList;
import java.util.List;

public class UserResponseSet {
    private String userId;
    private List<UserResponse> userResponses;



    public UserResponseSet(String userIdIn){
        this.userId=userIdIn;
        userResponses=new ArrayList<>();
    }


    public void addResponse(UserResponse response){
        userResponses.add(response);
    }
    public void addAllResponses(List<UserResponse> allResponsesIn){
        userResponses.addAll(allResponsesIn);

    }
    public int getUserResponsesSize(){
        return userResponses.size();
    }


    public void setUserResponses(List<UserResponse> userResponsesIn) {
        this.userResponses = userResponsesIn;
    }
    public List<UserResponse> getUserResponses() {
        return userResponses;
    }


    public void setUserId(String userIdIn){this.userId=userIdIn;}
    public String getUserId(){return userId;}

    //TODO: shouldn't be capitalized, and there shouldn'be a a score property, or a getter or setter, let's talk about it.


    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!UserResponseSet.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        UserResponseSet other = (UserResponseSet) otherObj;
        return this.getUserResponsesSize()==(other.getUserResponsesSize())
                && this.getUserId().equals(other.getUserId());
    }
}

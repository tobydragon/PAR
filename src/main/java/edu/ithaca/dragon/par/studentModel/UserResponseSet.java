package edu.ithaca.dragon.par.studentModel;

import java.util.ArrayList;
import java.util.List;

public class UserResponseSet {
    private String userId;
    private List<ResponsesPerQuestion> userResponse;


    public UserResponseSet(String userIdIn){
        this.userId=userIdIn;
        userResponse =new ArrayList<>();
    }



    public void addResponse(ResponsesPerQuestion response) {
        int index=sameResponseCheck(response);

        if (index== -1) userResponse.add(response);

        else userResponse.get(index).addNewResponse(response.getResponseText());
    }
    private int sameResponseCheck(ResponsesPerQuestion response){
        if(userResponse.isEmpty()){
            return -1;
        }
        else {
            for (int i = 0; i < userResponse.size(); i++) {
                if (userResponse.get(i).getQuestionId() == response.getQuestionId()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void addAllResponses(List<ResponsesPerQuestion> allResponsesIn){
        int index=sameResponseListCheck(allResponsesIn);

        if(index==-1) userResponse.addAll(allResponsesIn);

        else{
            for(int i = index; i< userResponse.size(); i++){
                for(int k=0;k<allResponsesIn.size();k++){
                    if(userResponse.get(i).getQuestionId()==allResponsesIn.get(k).getQuestionId()){
                        userResponse.get(i).addNewResponse(allResponsesIn.get(k).getResponseText());
                    }
                }
            }
        }
    }
    private int sameResponseListCheck(List<ResponsesPerQuestion> allResponsesIn){
        if(userResponse.isEmpty()){
            return -1;
        }
        for(int i = 0; i< userResponse.size(); i++){
            for(int k=0;k<allResponsesIn.size();i++){
                if(userResponse.get(i).getQuestionId()==allResponsesIn.get(k).getQuestionId()){
                    return i;
                }
            }
        }
        return -1;
    }

    public int getUserResponsesSize(){
        return userResponse.size();
    }

    public void setUserResponse(List<ResponsesPerQuestion> userResponsesIn) {
        this.userResponse = userResponsesIn;
    }
    public List<ResponsesPerQuestion> getUserResponse() {
        return userResponse;
    }

    public void setUserId(String userIdIn){this.userId=userIdIn;}
    public String getUserId(){return userId;}

    public double knowledgeCalc(){
       return 0;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!UserResponseSet.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        UserResponseSet other = (UserResponseSet) otherObj;
        return this.getUserResponse().equals(other.getUserResponse())
                && this.getUserId().equals(other.getUserId());
    }
}

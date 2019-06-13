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
    public int getUserResponsesSize(){
        return userResponses.size();
    }

    public double getScore(){
        double score=100;
        double quesWeight=100/(double)getUserResponsesSize();

        for(int i=0;i<getUserResponsesSize();i++){
            if(userResponses.get(i).checkResponse()!=true){
                score=score-quesWeight;
            }
        }
        return score;
    }

}

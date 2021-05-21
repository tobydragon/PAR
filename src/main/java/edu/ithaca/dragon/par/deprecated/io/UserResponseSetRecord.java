package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.deprecated.studentModel.UserResponseSet;

import java.util.ArrayList;
import java.util.List;

public class UserResponseSetRecord {
    private String userId;
    private List<ResponsesPerQuestionRecord> responsesPerQuestionRecords;

    public UserResponseSetRecord(){
        responsesPerQuestionRecords =new ArrayList<>();
    }


    public UserResponseSetRecord(UserResponseSet userResponseSet){
        this();
        userId=userResponseSet.getUserId();

        for(ResponsesPerQuestion currResponsesPerQuestion : userResponseSet.getResponsesPerQuestionList()){
            responsesPerQuestionRecords.add(new ResponsesPerQuestionRecord(currResponsesPerQuestion));
        }
    }

    public UserResponseSet buildUserResponseSet(QuestionPool questionPool){
        List<ResponsesPerQuestion> responses=new ArrayList<>();
        for(ResponsesPerQuestionRecord currURR : responsesPerQuestionRecords){
            responses.add(currURR.buildResponsesPerQuestionRecord(questionPool));
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

    public List<ResponsesPerQuestionRecord> getResponsesPerQuestionRecords() {
        return responsesPerQuestionRecords;
    }

    public void setResponsesPerQuestionRecords(List<ResponsesPerQuestionRecord> responsesPerQuestionRecords) {
        this.responsesPerQuestionRecords = responsesPerQuestionRecords;
    }


}

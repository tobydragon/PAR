package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.List;

public class StudentModel {

    private String userId;
    private UserQuestionSet userQuestionSet;
    private UserResponseSet userResponseSet;

    public StudentModel(String userId, List<Question> questions){
        this.userId = userId;
        this.userQuestionSet = new UserQuestionSet(userId, questions);
        this.userResponseSet = new UserResponseSet(userId);
    }

    public int getSeenQuestionCount(){
        return userQuestionSet.getLenOfSeenQuestions();
    }

    public int getUnseenQuestionCount(){
        return userQuestionSet.getLenOfUnseenQuestions();
    }

    public int getResponseCount(){
        return userResponseSet.getUserResponsesSize();
    }

    public String getUserId(){
        return userId;
    }

}

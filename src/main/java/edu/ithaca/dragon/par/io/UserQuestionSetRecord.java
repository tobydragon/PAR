package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSetRecord {
    private String userId;
    private List<QuestionCountRecord> questionCountRecords;

    public UserQuestionSetRecord(){
        questionCountRecords = new ArrayList<>();
    }

    public UserQuestionSetRecord(UserQuestionSet userQuestionSet){
        this();
        userId = userQuestionSet.getUserId();
        for(QuestionCount questionCount : userQuestionSet.getQuestionCounts()){
            questionCountRecords.add(new QuestionCountRecord(questionCount));
        }
    }

    public UserQuestionSet buildUserQuestionSet(QuestionPool questionPool) {
        List<QuestionCount> questionCounts = QuestionCountRecord.questionCountRecordToQuestionCount(questionCountRecords,questionPool);
        return UserQuestionSet.buildUserQuestionSetFromCounts(userId, questionCounts);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<QuestionCountRecord> getQuestionCountRecords() {
        return questionCountRecords;
    }

    public void setQuestionCountRecords(List<QuestionCountRecord> questionCountRecords) {
        this.questionCountRecords = questionCountRecords;
    }
}

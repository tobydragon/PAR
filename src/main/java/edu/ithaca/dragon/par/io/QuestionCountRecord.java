package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.QuestionCount;

import java.util.ArrayList;
import java.util.List;

public class QuestionCountRecord {
    public String questionId;
    public int timesSeen;
    public List<QuestionCountRecord> followupCountRecords;

    public QuestionCountRecord(){
        followupCountRecords = new ArrayList<>();
    }

    public QuestionCountRecord(QuestionCount questionCount) {
        questionId = questionCount.getQuestion().getId();
        timesSeen = questionCount.getTimesSeen();
        if (questionCount.getFollowupCounts().size()>0) {
            followupCountRecords = QuestionCountRecord.questionCountToQuestionCountRecord(questionCount.getFollowupCounts());
        }
        else {
            followupCountRecords = new ArrayList<>();
        }
    }

    public static List<QuestionCountRecord> questionCountToQuestionCountRecord(List<QuestionCount> questionCounts) {
        List<QuestionCountRecord> questionCountRecordList = new ArrayList<>();
        for (int i = 0; i < questionCounts.size(); i++){
            QuestionCountRecord currQuestionCount = new QuestionCountRecord(questionCounts.get(i));
            questionCountRecordList.add(currQuestionCount);
        }
        return questionCountRecordList;
    }

    public static List<QuestionCount> questionCountRecordToQuestionCount(List<QuestionCountRecord> questionCountRecords, QuestionPool questionPool) {
        List<QuestionCount> questionCountList = new ArrayList<>();
        for (int i = 0; i < questionCountRecords.size(); i++){
            QuestionCount currQuestionCount = questionCountRecords.get(i).buildQuestionCount(questionPool);
            questionCountList.add(currQuestionCount);
        }
        return questionCountList;
    }

    public QuestionCount buildQuestionCount(QuestionPool questionPool) {
        QuestionCount questionCount = new QuestionCount(questionPool.getQuestionFromId(questionId));
        questionCount.setTimesSeen(timesSeen);
        if (followupCountRecords.size()>0){
            List<QuestionCount> followupList = new ArrayList<>();
            followupList = questionCountRecordToQuestionCount(followupCountRecords, questionPool);
            questionCount.setFollowupCounts(followupList);
        }
        else{
            questionCount.followupCounts = new ArrayList<>();
        }

        return questionCount;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getTimesSeen() {
        return timesSeen;
    }

    public void setTimesSeen(int timesSeen) {
        this.timesSeen = timesSeen;
    }

    public List<QuestionCountRecord> getFollowupCountRecords() {
        return followupCountRecords;
    }

    public void setFollowup(List<QuestionCountRecord> followup) {
        this.followupCountRecords = followup;
    }
}

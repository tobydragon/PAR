package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.studentModel.QuestionCount;

import java.util.ArrayList;
import java.util.List;

public class QuestionCountRecord {
    public String questionId;
    public int timesSeen;
    public List<QuestionCountRecord> followupCountRecords;

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

package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSetRecord {
    private String userId;
    private List<String> unseenQuestionIds;
    private List<String> seenQuestionIds;
    private List<Integer> timesSeen;

    public UserQuestionSetRecord(){
        unseenQuestionIds = new ArrayList<>();
        seenQuestionIds = new ArrayList<>();
        timesSeen = new ArrayList<>();
    }

    public UserQuestionSetRecord(UserQuestionSet userQuestionSet){
        this();
        userId = userQuestionSet.getUserId();
        for(Question question : userQuestionSet.getSeenQuestions()){
            seenQuestionIds.add(question.getId());
            timesSeen.add(userQuestionSet.getTimesSeen(question.getId()));
        }
        for(Question question : userQuestionSet.getUnseenQuestions()){
            unseenQuestionIds.add(question.getId());
        }
    }

    public UserQuestionSet buildUserQuestionSet(QuestionPool questionPool) {
        //use questionPool
        return null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUnseenQuestionIds() {
        return unseenQuestionIds;
    }

    public void setUnseenQuestionIds(List<String> unseenQuestionIds) {
        this.unseenQuestionIds = unseenQuestionIds;
    }

    public List<String> getSeenQuestionIds() {
        return seenQuestionIds;
    }

    public void setSeenQuestionIds(List<String> seenQuestionIds) {
        this.seenQuestionIds = seenQuestionIds;
    }

    public List<Integer> getTimesSeen() {
        return timesSeen;
    }

    public void setTimesSeen(List<Integer> timesSeen) {
        this.timesSeen = timesSeen;
    }
}

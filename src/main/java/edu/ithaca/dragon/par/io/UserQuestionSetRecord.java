package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSetRecord {
    private String userId;
    private List<String> questionIds;
    private List<Integer> timesSeen;

    public UserQuestionSetRecord(){
        questionIds = new ArrayList<>();
        timesSeen = new ArrayList<>();
    }

    public UserQuestionSetRecord(UserQuestionSet userQuestionSet){
        this();
        userId = userQuestionSet.getUserId();
        for(QuestionCount currQC : userQuestionSet.getQuestions()){
            questionIds.add(currQC.getQuestion().getId());
            timesSeen.add(currQC.getTimesSeen());
        }
    }

    public UserQuestionSet buildUserQuestionSet(QuestionPool questionPool) {
        List<Question> questions = new ArrayList<>();
        questions = questionPool.getQuestionsFromIds(questionIds);


        return new UserQuestionSet(userId, questions);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public List<Integer> getTimesSeen() {
        return timesSeen;
    }

    public void setTimesSeen(List<Integer> timesSeen) {
        this.timesSeen = timesSeen;
    }
}

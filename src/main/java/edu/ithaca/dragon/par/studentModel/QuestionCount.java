package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.List;

public class QuestionCount {

    public Question question;
    public int timesSeen;
    public List<QuestionCount> followupCounts;

    public QuestionCount(Question questionIn){
        question = questionIn;
        timesSeen = 0;
        followupCounts = UserQuestionSet.questionToQuestionCount(questionIn.getFollowupQuestions());
    }



    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setFollowupCounts(List<QuestionCount> followupCounts) {
        this.followupCounts = followupCounts;
    }

    public List<QuestionCount> getFollowupCounts() {
        return followupCounts;
    }

    public int getTimesSeen(){
        return timesSeen;
    }

    public void increaseTimesSeen(){
        timesSeen+=1;
    }

    public Question getQuestion(){
        return question;
    }

    public void setTimesSeen(int seen){
        timesSeen = seen;
    }

    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!QuestionCount.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        QuestionCount other = (QuestionCount) otherObj;
        return this.getQuestion().equals(other.getQuestion())
                && this.getTimesSeen()==(other.getTimesSeen());
    }

}

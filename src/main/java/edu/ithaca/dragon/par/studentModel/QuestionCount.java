package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionCount {

    Question question;
    int timesSeen;
    List<QuestionCount> followup

    public QuestionCount(Question questionIn){
        question = questionIn;
        timesSeen = 0;
        followup = UserQuestionSet.questionToQuestionCount(questionIn.getFollowupQuestions());
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

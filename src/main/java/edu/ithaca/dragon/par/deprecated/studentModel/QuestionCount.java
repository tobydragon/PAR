package edu.ithaca.dragon.par.deprecated.studentModel;

import edu.ithaca.dragon.par.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionCount {

    public Question question;
    public int timesAttempted;
    public List<QuestionCount> followupCounts;

    public QuestionCount(Question questionIn){
        question = questionIn;

        //this represents that you’ve seen it AND either skipped or answered.
        timesAttempted = 0;
        followupCounts = questionToQuestionCount(questionIn.getFollowupQuestions());
    }

    public static List<QuestionCount> questionToQuestionCount(List<Question> questions){
        List<QuestionCount> questionCountList = new ArrayList<QuestionCount>();
        for (int i = 0; i<questions.size(); i++){
            QuestionCount qc = new QuestionCount(questions.get(i));
            qc.setTimesAttempted(0);
            questionCountList.add(qc);
        }
        return questionCountList;
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

    public int getTimesAttempted(){
        return timesAttempted;
    }

    public void increaseTimesAttempted(){
        timesAttempted +=1;
    }

    public Question getQuestion(){
        return question;
    }

    public void setTimesAttempted(int seen){
        timesAttempted = seen;
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
                && this.getTimesAttempted()==(other.getTimesAttempted());
    }

}

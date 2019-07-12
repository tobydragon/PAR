package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;


public class UserQuestionSet {
    private String userId;
    private List<QuestionCount> questionCounts;


    public static UserQuestionSet buildNewUserQuestionSetFromQuestions(String userId, List<Question> questionsIn ){
        return new UserQuestionSet(userId, QuestionCount.questionToQuestionCount(questionsIn));
    }

    public static UserQuestionSet buildUserQuestionSetFromCounts(String userId, List<QuestionCount> questionCounts ){
        return new UserQuestionSet(userId, questionCounts);
    }

    private UserQuestionSet(String userId, List<QuestionCount> questionCounts ){
        this.userId = userId;
        this.questionCounts = questionCounts;
    }

    public List<QuestionCount> getQuestionCounts(){ return questionCounts; }

    public List<Question> getTopLevelSeenQuestions(){
        List<Question> seen = new ArrayList<>();
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesSeen()>0){
                seen.add(currQuestion.getQuestion());
            }
        }
        return seen;
    }

    public List<Question> getTopLevelUnseenQuestions(){
        List<Question> unseen = new ArrayList<>();
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesSeen()==0){
                unseen.add(currQuestion.getQuestion());
            }
        }
        return unseen;
    }


    public int getTimesSeen (String questionId){
        QuestionCount qc = getQuestionCountFromId(questionId);
        if(qc == null){
            throw new RuntimeException("QuestionCount with id:" + questionId + " does not exist");
        }
        return qc.timesSeen;
    }


    public String getUserId () {
        return userId;
    }

    public void increaseTimesSeen(String questionId){
        QuestionCount qc = getQuestionCountFromId(questionId);
        if(qc == null){
            throw new RuntimeException("QuestionCount with id:" + questionId + " does not exist");
        }
        qc.increaseTimesSeen();

    }

    public QuestionCount getQuestionCountFromId(String questionIdIn){
        QuestionCount q = getQuestionCountFromId(questionIdIn, questionCounts);
        if(q == null){
            throw new RuntimeException("QuestionCount with id:" + questionIdIn + " does not exist");
        }
        return q;
    }

    public static QuestionCount getQuestionCountFromId(String questionIdIn, List<QuestionCount> questionList){
        for(QuestionCount q : questionList){
            if(q.getQuestion().getId().equals(questionIdIn)){
                return q;
            }

            //call getQuestionFromId on the followup questions
            QuestionCount q2 = getQuestionCountFromId(questionIdIn, q.getFollowupCounts());
            if(q2 != null){
                return q2;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!UserQuestionSet.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        UserQuestionSet other = (UserQuestionSet) otherObj;
        return this.getUserId().equals(other.getUserId())
                && this.getQuestionCounts().equals(other.getQuestionCounts());
    }


}


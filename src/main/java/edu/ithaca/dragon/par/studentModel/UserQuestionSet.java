package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSet {
    private String userId;
    private List<QuestionCount> questionCounts;


    public static UserQuestionSet buildNewUserQuestionSetFromQuestions(String userId, List<Question> questionsIn ){
        return new UserQuestionSet(userId, questionToQuestionCount(questionsIn));
    }

    public static UserQuestionSet buildUserQuestionSetFromCounts(String userId, List<QuestionCount> questionCounts ){
        return new UserQuestionSet(userId, questionCounts);
    }

    private UserQuestionSet(String userId, List<QuestionCount> questionCounts ){
        this.userId = userId;
        this.questionCounts = questionCounts;
    }

    public static List<QuestionCount> questionToQuestionCount(List<Question> questions){
        List<QuestionCount> questionCountList = new ArrayList<QuestionCount>();
        for (int i = 0; i<questions.size(); i++){
            QuestionCount qc = new QuestionCount(questions.get(i));
            qc.setTimesSeen(0);
            questionCountList.add(qc);
        }
        return questionCountList;
    }


    public int getLenOfQuestions(){
        return questionCounts.size();
    }

    public List<QuestionCount> getQuestionCounts(){ return questionCounts; }

    public List<Question> getSeenQuestions(){
        List<Question> seen = new ArrayList<Question>();
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesSeen()>0){
                seen.add(currQuestion.getQuestion());
            }
        }
        return seen;
    }

    public List<Question> getUnseenQuestions(){
        List<Question> unseen = new ArrayList<Question>();
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesSeen()==0){
                unseen.add(currQuestion.getQuestion());
            }
        }
        return unseen;
    }


    public int getTimesSeen (String questionId){
        QuestionCount qc = getQuestioncountFromId(questionId);
        if(qc == null){
            throw new RuntimeException("QuestionCount with id:" + questionId + " does not exist");
        }
        return qc.timesSeen;
    }


    public String getUserId () {
        return userId;
    }

    public boolean increaseTimesSeen (String questionId){
        for (int i = 0; i< questionCounts.size(); i++){
            if(questionCounts.get(i).getQuestion().getId().equals(questionId)){
                questionCounts.get(i).increaseTimesSeen();
                return true;
            }
        }
        return false;
    }

    public  int getLenOfSeenQuestions(){
        int seen = 0;
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesSeen()>0){
                seen+=1;
            }
        }
        return seen;
    }

    public  int getLenOfUnseenQuestions(){
        int unseen = 0;
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesSeen()==0){
                unseen+=1;
            }
        }
        return unseen;
    }

    public void givenQuestion(String questionId){
        QuestionCount qc = getQuestioncountFromId(questionId);
        if(qc == null){
            throw new RuntimeException("QuestionCount with id:" + questionId + " does not exist");
        }
        qc.increaseTimesSeen();

    }

    public QuestionCount getQuestioncountFromId(String questionIdIn){
        QuestionCount q = getQuestioncountFromId(questionIdIn, questionCounts);
        if(q == null){
            throw new RuntimeException("QuestionCount with id:" + questionIdIn + " does not exist");
        }
        return q;
    }

    public static QuestionCount getQuestioncountFromId(String questionIdIn, List<QuestionCount> questionList){
        for(QuestionCount q : questionList){
            if(q.getQuestion().getId().equals(questionIdIn)){
                return q;
            }

            //call getQuestionFromId on the followup questions
            QuestionCount q2 = getQuestioncountFromId(questionIdIn, q.getFollowupCounts());
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


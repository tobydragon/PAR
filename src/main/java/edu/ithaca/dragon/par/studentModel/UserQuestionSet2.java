package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSet2 {
    private String userId;
    private List<QuestionCount>questions;


    public UserQuestionSet2(String userIdIn, List<Question> questionsIn){
        userId = userIdIn;
        questions = questionToQuestionCount(questionsIn);
    }

    public List<QuestionCount> questionToQuestionCount(List<Question> questions){
        List<QuestionCount> questionCountList = new ArrayList<QuestionCount>();
        for (int i = 0; i<questions.size(); i++){
            QuestionCount qc = new QuestionCount(questions.get(i));
            questionCountList.add(qc);
        }
        return questionCountList;
    }


    public int getLenOfQuestions(){
        return questions.size();
    }

    public List<QuestionCount> getQuestions(){ return questions; }


    public int getTimesSeen (String questionId){
        for (int i = 0; i<questions.size(); i++){
            if(questions.get(i).getQuestion().getId().equals(questionId)){
                return questions.get(i).getTimesSeen();
            }
        }
        throw new RuntimeException();
    }


    public String getUserId () {
        return userId;
    }

    public boolean increaseTimesSeen (String questionId){
        for (int i = 0; i<questions.size(); i++){
            if(questions.get(i).getQuestion().getId().equals(questionId)){
                questions.get(i).increaseTimesSeen();
                return true;
            }
        }
        return false;
    }

    public void givenQuestion(String questionId){
        boolean found = false;
        for(int i = 0; i<questions.size(); i++){
            if(questions.get(i).getQuestion().getId().equals(questionId)){
                questions.get(i).increaseTimesSeen();
                found = true;
            }
        }
        if(!found) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!UserQuestionSet2.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        UserQuestionSet2 other = (UserQuestionSet2) otherObj;
        return this.getUserId().equals(other.getUserId())
                && this.getQuestions().equals(other.getQuestions());
    }

}


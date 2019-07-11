package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSet {
    private String userId;
    private List<QuestionCount> questions;


    public UserQuestionSet(String userIdIn, List<Question> questionsIn){
        this(userIdIn, questionsIn, new ArrayList<>());
    }
    public UserQuestionSet(String userIdIn, List<Question> questionsIn, List<Integer> timesSeenIn){
        if(timesSeenIn.size()==0){
            for (int i = 0; i <questionsIn.size(); i++){
                timesSeenIn.add(0);
            }
        }
        if (timesSeenIn.size()!=questionsIn.size()){
            throw new RuntimeException();
        }
        userId = userIdIn;
        questions = questionToQuestionCount(questionsIn, timesSeenIn);
    }

    public static List<QuestionCount> questionToQuestionCount(List<Question> questions, List<Integer> timesSeenIn){
        List<QuestionCount> questionCountList = new ArrayList<QuestionCount>();
        for (int i = 0; i<questions.size(); i++){
            QuestionCount qc = new QuestionCount(questions.get(i));
            qc.setTimesSeen(timesSeenIn.get(i));
            questionCountList.add(qc);
        }
        return questionCountList;
    }


    public int getLenOfQuestions(){
        return questions.size();
    }

    public List<QuestionCount> getQuestions(){ return questions; }

    public List<Question> getSeenQuestions(){
        List<Question> seen = new ArrayList<Question>();
        for (QuestionCount currQuestion: questions){
            if (currQuestion.getTimesSeen()>0){
                seen.add(currQuestion.getQuestion());
            }
        }
        return seen;
    }

    public List<Question> getUnseenQuestions(){
        List<Question> unseen = new ArrayList<Question>();
        for (QuestionCount currQuestion: questions){
            if (currQuestion.getTimesSeen()==0){
                unseen.add(currQuestion.getQuestion());
            }
        }
        return unseen;
    }


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

    public  int getLenOfSeenQuestions(){
        int seen = 0;
        for (QuestionCount currQuestion: questions){
            if (currQuestion.getTimesSeen()>0){
                seen+=1;
            }
        }
        return seen;
    }

    public  int getLenOfUnseenQuestions(){
        int unseen = 0;
        for (QuestionCount currQuestion: questions){
            if (currQuestion.getTimesSeen()==0){
                unseen+=1;
            }
        }
        return unseen;
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
        if(!UserQuestionSet.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        UserQuestionSet other = (UserQuestionSet) otherObj;
        return this.getUserId().equals(other.getUserId())
                && this.getQuestions().equals(other.getQuestions());
    }


}


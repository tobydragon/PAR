package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSet {
    public String userId;
    public List<Question>unseenQuestions;
    public List<Question>seenQuestions;
    public List<Integer> timesSeen;


    public UserQuestionSet(String userIdIn, List<Question> unseenQuestionsIn){

        seenQuestions = new ArrayList<Question>();
        this.unseenQuestions = unseenQuestionsIn;
        timesSeen = new ArrayList<Integer>();
        this.userId=userIdIn;
    }

    /**
     *
     * @param questionId
     * @return question
     * @throws
     *
     */
    public Question getQ(String questionId) {
        for (int i = 0; i < unseenQuestions.size(); i++){
            if (unseenQuestions.get(i).getId().equals(questionId)){
                return unseenQuestions.get(i);
            }
        }
        return null;
    }

    public int getLenOfSeenQuestions(){
        return seenQuestions.size();
    }

    public int getLenOfUnseenQuestions(){
        return unseenQuestions.size();
    }

    public int getTimesSeen (String questionId){
        if(getLenOfSeenQuestions()<1){
            return -1;
        }
        for (int i =0; i<seenQuestions.size(); i++){
            if (seenQuestions.get(i).getId().equals(questionId)){
                return timesSeen.get(i);
            }
        }
        return -1;
    }


    public String getUserId () {
        return userId;
    }

    public void increaseTimesSeen (String questionId){
        for (int i = 0; i < seenQuestions.size(); i++){
            if (seenQuestions.get(i).getId().equals(questionId)){
                int newVal = timesSeen.get(i)+1;
                timesSeen.set(i, newVal);
            }
        }
    }

    public List<Question> getUnseenQuestions(){
        return unseenQuestions;
    }

    public List<Question> getSeenQuestions(){
        return seenQuestions;
    }

    public void givenQuestion(String questionId){
        for (int i = 0; i < unseenQuestions.size(); i++){
            if (unseenQuestions.get(i).getId().equals(questionId)){
                seenQuestions.add(unseenQuestions.get(i));
                unseenQuestions.remove(i);
                timesSeen.add(1);

            }
        }
    }

}


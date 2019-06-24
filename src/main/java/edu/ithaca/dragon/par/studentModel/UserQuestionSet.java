package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSet {
    private String userId;
    private List<Question>unseenQuestions;
    private List<Question>seenQuestions;
    private List<Integer> timesSeen;


    public UserQuestionSet(String userIdIn, List<Question> unseenQuestionsIn){
        //TODO: call the other constructor
        //???
        seenQuestions = new ArrayList<Question>();
        this.unseenQuestions = unseenQuestionsIn;
        timesSeen = new ArrayList<Integer>();
        this.userId=userIdIn;
    }

    public UserQuestionSet(String userIdIn, List<Question> unseenQuestionsIn, List<Question> seenQuestionsIn, List<Integer> timesSeenIn) {
        seenQuestions = seenQuestionsIn;
        unseenQuestions = unseenQuestionsIn;
        timesSeen = timesSeenIn;
        userId=userIdIn;
    }

    //TODO: should probably remove thie entirely
    //removed

    public int getLenOfSeenQuestions(){
        return seenQuestions.size();
    }

    public int getLenOfUnseenQuestions(){
        return unseenQuestions.size();
    }

    public int getTimesSeen (String questionId){
        if(getLenOfSeenQuestions()<1){
            return 0;
        }
        for (int i =0; i<seenQuestions.size(); i++){
            if (seenQuestions.get(i).getId().equals(questionId)){
                return timesSeen.get(i);
            }
        }
        for (int i =0; i<unseenQuestions.size(); i++) {
            if (unseenQuestions.get(i).getId().equals(questionId)) {
                return 0;
            }
        }

        //TODO: should check unseen to confirm the question is part of the set, throw exception if not
        //done, check with toby about assertThrows.
        throw new RuntimeException();
    }


    public String getUserId () {
        return userId;
    }

    //TODO: should return true or false as to whether the item was found
    //done.
    public boolean increaseTimesSeen (String questionId){
        for (int i = 0; i < seenQuestions.size(); i++){
            if (seenQuestions.get(i).getId().equals(questionId)){
                timesSeen.set(i, timesSeen.get(i)+1);
                return true;
            }
        }
        return false;
    }

    public List<Question> getUnseenQuestions(){
        return unseenQuestions;
    }

    public List<Question> getSeenQuestions(){
        return seenQuestions;
    }

    public void givenQuestion(String questionId){
        boolean found = false;
        int index = -1;
        for (int i = 0; i < unseenQuestions.size(); i++){
            if (unseenQuestions.get(i).getId().equals(questionId)){
                seenQuestions.add(unseenQuestions.get(i));
                //TODO: shouldn't remove in a loop over the list
                //done, see following if statement
                index = i;
                timesSeen.add(1);
                found = true;
            }
        }
        if(found){
            unseenQuestions.remove(index);
        }
        if (!found) {
            //checks seen list, adds 1 to time seen if question is found
            for(int i = 0; i < seenQuestions.size(); i++){
                if (seenQuestions.get(i).getId().equals(questionId)){
                    found = true;
                    increaseTimesSeen(questionId);
                }
            }
            if(!found){
                throw new RuntimeException();
            }
            //TODO: should check if the question was found in timesSeen, throw exception if not.
            //done, check junit assertThrows
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
                && this.getSeenQuestions().equals(other.getSeenQuestions())
                && this.getUnseenQuestions().equals(other.getUnseenQuestions())
                && this.timesSeen.equals(other.timesSeen);
    }

}


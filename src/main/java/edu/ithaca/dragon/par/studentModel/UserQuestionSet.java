package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class UserQuestionSet {
    public String userId;
    public List<Question>unseenQuestions;
    public List<Question>seenQuestions;
    public List<Integer> timesSeen;


    public UserQuestionSet(String userIdIn){

        unseenQuestions = new ArrayList<Question>();
        seenQuestions = new ArrayList<Question>();
        timesSeen = new ArrayList<Integer>();
        this.userId=userIdIn;

        List<String > eyeA = new ArrayList<String>();
        eyeA.add("purple");
        eyeA.add("blue");
        eyeA.add("green");
        eyeA.add("brown");
        Question q1 = new Question("1", "What is your eye color?", 1, "purple", eyeA);
        unseenQuestions.add(q1);

        List<String > colorA = new ArrayList<String>();
        colorA.add("purple");
        colorA.add("blue");
        colorA.add("green");
        colorA.add("brown");
        Question q2 = new Question("2", "What is your favorite color?", 2, "brown", colorA);
        unseenQuestions.add(q2);

        List<String > skyA = new ArrayList<String>();
        skyA.add("purple");
        skyA.add("blue");
        skyA.add("green");
        skyA.add("brown");
        Question q3 = new Question("3", "What color is the sky?", 3, "blue", skyA);
        unseenQuestions.add(q3);


        List<String > grassA = new ArrayList<String>();
        grassA.add("purple");
        grassA.add("blue");
        grassA.add("green");
        grassA.add("brown");
        Question q4 = new Question("4", "What color is the grass?", 1, "green", grassA);
        unseenQuestions.add(q4);

        List<String > ounceA = new ArrayList<String>();
        ounceA.add("4");
        ounceA.add("8");
        ounceA.add("16");
        ounceA.add("12");
        Question q5 = new Question("5", "How many ounces are in a pound?", 3, "16", ounceA);
        unseenQuestions.add(q5);

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
            if (unseenQuestions.get(i).getId()==questionId){
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
            if (seenQuestions.get(i).getId()==questionId){
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
            if (seenQuestions.get(i).getId()==questionId){
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
            if (unseenQuestions.get(i).getId()==questionId){
                seenQuestions.add(unseenQuestions.get(i));
                unseenQuestions.remove(i);
                timesSeen.add(0);

            }
        }
    }

}


package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;

public class QuestionCount {

    Question question;
    int timesSeen;

    public QuestionCount(Question questionIn){
        question = questionIn;
        timesSeen = 0;
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



}

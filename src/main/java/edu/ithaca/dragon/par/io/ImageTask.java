package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImageTask{

    private String imageUrl;
    private List<Question> taskQuestions;

    public ImageTask(){};

    public ImageTask(String imageUrlIn, List<Question> taskQuestionsIn){
        imageUrl = imageUrlIn;
        taskQuestions = new ArrayList<>(taskQuestionsIn);
    }

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrlIn) {imageUrl = imageUrlIn;}

    public List<Question> getTaskQuestions() {return taskQuestions;}
    public void setTaskQuestions(List<Question> taskQuestionsIn) {taskQuestions = taskQuestionsIn;}

    public void addQuestion(Question questionToAdd){
        taskQuestions.add(questionToAdd);
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!ImageTask.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        ImageTask other = (ImageTask) otherObj;
        return this.getImageUrl().equals(other.getImageUrl())
                && this.getTaskQuestions().equals(other.getTaskQuestions());
    }
}

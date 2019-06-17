package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class ImageTask{

    private String imageUrl;
    private List<Question> taskQuestions;

    public ImageTask(String imageUrlIn, List<Question> taskQuestionsIn){
        imageUrl = imageUrlIn;
        taskQuestions = taskQuestionsIn;
    }

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrlIn) {imageUrl = imageUrlIn;}

    public List<Question> getTaskQuestions() {return taskQuestions;}
    public void setTaskQuestions(List<Question> taskQuestionsIn) {taskQuestions = taskQuestionsIn;}


}

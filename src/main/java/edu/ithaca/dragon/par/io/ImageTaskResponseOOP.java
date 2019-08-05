package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.ArrayList;
import java.util.List;

public class ImageTaskResponseOOP implements ImageTaskReponse {

    private String userId;
    private List<QuestionResponseOOP> questionResponses;

    //constructor taken from ImageTaskResponseImp1
    public ImageTaskResponseOOP(String userIdIn, List<String> taskQuestionIdsIn, List<String> responseTextsIn){
        this.userId = userIdIn;
        this.questionResponses = new ArrayList<>();
        for(int i=0; i<taskQuestionIdsIn.size(); i++){
            questionResponses.add(new QuestionResponseOOP(taskQuestionIdsIn.get(i), responseTextsIn.get(i)));
        }
    }

    public List<String> getTaskQuestionIds() {
        return null;
    }

    public void setTaskQuestionIds(List<String> taskQuestionIds) {

    }

    public List<String> getResponseTexts() {
        return null;
    }

    public void setResponseTexts(List<String> responseTexts) {

    }

    public String getUserId() {
        return null;
    }

    public void setUserId(String userId) {

    }

    public String findResponseToQuestion(Question question) {
        return null;
    }
}

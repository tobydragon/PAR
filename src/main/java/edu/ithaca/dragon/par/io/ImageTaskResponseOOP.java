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
        List<String> questionIds = new ArrayList<>();
        for(QuestionResponseOOP questionResponseOOP : questionResponses){
            questionIds.add(questionResponseOOP.questionId);
        }
        return questionIds;
    }

    public List<String> getResponseTexts() {
        List<String> responseTexts = new ArrayList<>();
        for(QuestionResponseOOP questionResponseOOP : questionResponses){
            responseTexts.add(questionResponseOOP.responseText);
        }
        return responseTexts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String findResponseToQuestion(Question question) {
        for(QuestionResponseOOP questionResponseOOP : questionResponses) {
            if(question.getId().equals(questionResponseOOP.questionId)){
                return questionResponseOOP.responseText;
            }
        }
        return null;
    }
}

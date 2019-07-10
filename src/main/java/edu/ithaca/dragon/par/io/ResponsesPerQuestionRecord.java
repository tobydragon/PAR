package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.QuestionResponse;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;

import java.util.ArrayList;
import java.util.List;

public class ResponsesPerQuestionRecord {
    private String userId;
    private String questionId;
    private String responseText;
    private List<QuestionResponse> allResponseTexts;
    //private Question question;
//TODO:changes string list to QuestionResponse List
    public ResponsesPerQuestionRecord(){
        allResponseTexts=new ArrayList<>();
    }


    public ResponsesPerQuestionRecord(ResponsesPerQuestion responsesPerQuestionIn){
        this();
        userId= responsesPerQuestionIn.getUserId();
        questionId= responsesPerQuestionIn.getQuestionId();
        responseText= responsesPerQuestionIn.getResponseText();
        for(int i =0;i<responsesPerQuestionIn.allResponseTextSize();i++){
            allResponseTexts.add(responsesPerQuestionIn.getAllResponseTexts().get(i));
        }
    }
     public ResponsesPerQuestion buildResponsesPerQuestionRecord(QuestionPool questionPool){
        Question question =questionPool.getQuestionFromId(questionId);
        ResponsesPerQuestion responsesPerQuestion=new ResponsesPerQuestion(userId,question,responseText);
        responsesPerQuestion.setAllResponseTexts(allResponseTexts);
        return responsesPerQuestion;
     }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public List<QuestionResponse> getAllResponseTexts() {
        return allResponseTexts;
    }

    public void setAllResponseTexts(List<QuestionResponse> allResponseTexts) {
        this.allResponseTexts = allResponseTexts;
    }
}

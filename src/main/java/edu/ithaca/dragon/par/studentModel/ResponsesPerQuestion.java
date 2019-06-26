package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ResponsesPerQuestion {
    private String questionId;
    private String userId;
    private Question question;
    private String responseText;
    private List<String> allResponseTexts;

    public ResponsesPerQuestion(String userIdIn, Question questionIn, String responseIn){
        this.userId=userIdIn;
        this.question=questionIn;
        this.questionId=questionIn.getId();
        responseText=responseIn;
        allResponseTexts=new ArrayList<>();
        allResponseTexts.add(responseIn);
    }
//TODO: WRITE A KNOWLEDGE CALCULATOR
    public double knowledgeCalc(){
       return 0;
    }

    public void addNewResponse(String newResponse){
        allResponseTexts.add(newResponse);
    }

    public int allResponseTextSize(){
        return allResponseTexts.size();
    }

    public void setQuestionId(String questionIdIn){this.questionId=questionIdIn;}
    public String getQuestionId(){
        return questionId;
    }

    public void setUserId(String userIdIn){this.userId=userIdIn;}
    public String getUserId(){
        return userId;
    }

    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<String> getAllResponseTexts() {
        return allResponseTexts;
    }
    public void setAllResponseTexts(List<String> allResponseTexts) {
        this.allResponseTexts = allResponseTexts;
    }

    public void setResponseText(String responseTextIn){this.responseText=responseTextIn;}
    public String getResponseText(){return responseText;}



    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!ResponsesPerQuestion.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        ResponsesPerQuestion other = (ResponsesPerQuestion) otherObj;
        return this.getQuestionId().equals(other.getQuestionId())
                && this.getResponseText().equals(other.getResponseText())
                && this.getUserId().equals(other.getUserId())
                && this.getAllResponseTexts().equals(other.getAllResponseTexts())
                && this.getQuestion().equals(other.getQuestion());

    }

}


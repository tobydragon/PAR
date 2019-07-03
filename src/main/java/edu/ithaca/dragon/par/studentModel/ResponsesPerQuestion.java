package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;


import java.util.ArrayList;
import java.util.List;

public class ResponsesPerQuestion {
    private String questionId;
    private String userId;
    private Question question;
    private String responseText;
    private String questionType;
    private List<String> allResponseTexts;



    public ResponsesPerQuestion(String userIdIn, Question questionIn, String responseIn){
        this.userId=userIdIn;
        this.question=questionIn;
        this.questionId=questionIn.getId();
        this.questionType=questionIn.getType();
        responseText=responseIn;
        allResponseTexts=new ArrayList<>();
        allResponseTexts.add(responseIn);
    }

    public double knowledgeCalc(){
        double score;
        if(responseText.equals(question.getCorrectAnswer())) score = 100;

        else score = 0;

        if(allResponseTextSize()>1) {
            for (int i = 1; i < allResponseTextSize(); i++) {
                if (!allResponseTexts.get(i).equals(question.getCorrectAnswer()) && score==100) score = 50;

                else if(allResponseTexts.get(i).equals(question.getCorrectAnswer()) && score==0) score = 50;
            }
        }
        return score;
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

    public String getQuestionType() {
        return questionType;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

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


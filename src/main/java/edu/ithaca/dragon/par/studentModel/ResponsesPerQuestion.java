package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponsesPerQuestion {
    public static final long SECONDS_BETWEEN =30;
    private String questionId;
    private String userId;
    private Question question;
    private String firstResponse;
    private String questionType;
    private List<QuestionResponse> allResponses;


    public ResponsesPerQuestion(){
        this.allResponses=new ArrayList<>();
    }
    public ResponsesPerQuestion(String userIdIn, Question questionIn, String responseIn){
        QuestionResponse questionResponse=new QuestionResponse(responseIn);
        this.userId=userIdIn;
        this.question=questionIn;
        this.questionId=questionIn.getId();
        this.questionType=questionIn.getType();
        firstResponse=responseIn;
        allResponses =new ArrayList<>();
        allResponses.add(questionResponse);
    }
//TODO: CHANGE KNOWLEDGE CALC TO BE BASED ON TIME STAMP INFORMATION
    //IF RESPONSE IS GIVEN WITH 35(CAN CHANGE) SECONDS WITHIN THE SAME QUESTION,
// THEN DONT COUNT THE NEWEST RESPONSE
    public double knowledgeCalc(){
        double score;
        if(allResponses.get(0).getResponseText().equals(question.getCorrectAnswer())) score = 100;

        else score = 0;

        if(allResponsesSize()>1) {
            for (int i = 0; i < allResponsesSize(); i++) {
                if (!allResponses.get(i).getResponseText().equals(question.getCorrectAnswer()) && score == 100) score = 50;
                else if (allResponses.get(i).getResponseText().equals(question.getCorrectAnswer()) && score == 0) score = 50;
                }
        }
        return score;
}
    public int knowledge(){
       int score=0;
       if(allResponsesSize()==1) {
           if (allResponses.get(0).getResponseText().equals(question.getCorrectAnswer())) score = 100;
           else score = 0;
       }
        if(allResponsesSize()>1) {
            if(checkTimeStampDifference(allResponses.get(allResponsesSize()-1).getMillSeconds(),allResponses.get(allResponsesSize()-2).getMillSeconds())) {
                if(allResponses.get(allResponsesSize()-1).getResponseText().equals(question.getCorrectAnswer())){
                   score=100;
                }
                else{
                    score=0;
                }
                   }
            }
        return score;
    }


    public static boolean checkTimeStampDifference(long firstTimeStamp,long secondTimeStamp){
        long secondsDifference =Math.abs(firstTimeStamp-secondTimeStamp)/1000;
        if(secondsDifference>=SECONDS_BETWEEN) return true;
        return false;
    }
//change back
    public void addNewResponse(String newResponse){
        QuestionResponse questionResponse=new QuestionResponse(newResponse);
        this.allResponses.add(questionResponse);
    }

    public void addNewResponse(QuestionResponse questionResponse){
        allResponses.add(questionResponse);
    }

    public int allResponsesSize(){
        return allResponses.size();
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

    public List<QuestionResponse> getAllResponses(){
        return allResponses;
    }
    public void setAllResponses(List<QuestionResponse> allResponses) {
        this.allResponses = allResponses;
    }

    public void setFirstResponse(String responseTextIn){this.firstResponse=responseTextIn;}
    public String getFirstResponse(){return firstResponse;}

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
                && this.getFirstResponse().equals(other.getFirstResponse())
                && this.getUserId().equals(other.getUserId())
                && this.getAllResponses().equals(other.getAllResponses())
                && this.getQuestion().equals(other.getQuestion());

    }

}


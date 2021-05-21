package edu.ithaca.dragon.par.deprecated.studentModel;
import edu.ithaca.dragon.par.domain.Question;


import java.util.ArrayList;
import java.util.List;

public class ResponsesPerQuestion {
    public static final long SECONDS_BETWEEN =30;//TODO
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

    /**
     * Stub function that calls the static knowledgeCalc
     * @return 0 or 100
     */
    public double knowledgeCalc(){
       return knowledgeCalc(allResponses, question);
    }

    /**
     * Calculates the knowledge score of the student
     * This algorithm relies on the most recent response being at the front of allResponses!
     * @return 0 or 100
     */
    public static double knowledgeCalc(List<QuestionResponse> allResponses, Question question){
        if(allResponses.size() == 1){
            //is the one response correct?
            if(ResponsesPerQuestion.checkIfAnswerIsCorrect(question.getCorrectAnswer(), allResponses.get(0).getResponseText())){
                return 100;
            }
            else{
                return 0;
            }
        }
        //More than one response to look at.
        //In order to get a 100, all responses within 30 seconds of the most recent response must be correct
        else if(allResponses.size()>1) {
            //get relevant responses
            List<QuestionResponse> recentResponses = questionsWithinTimeFromRecentResponse(allResponses);
            //check if they are correct
            if(checkIfAnswersAreCorrect(recentResponses, question.getCorrectAnswer())){
                return 100;
            }
            else{
                return 0;
            }
        }
        else{ //there are no responses to check
            //TODO: should this throw an exception?
            return 0;
        }
    }

    /**
     * @param allResponses list of all the responses recorded
     *                     The most recent response is at the end of allResponses
     * @return list of QuestionResponses within less than a 30 seconds time window
     */
    public static List<QuestionResponse> questionsWithinTimeFromRecentResponse(List<QuestionResponse> allResponses){
        //make a list for all the questionResponses within the timeframe, initialize with the most recent response
        List<QuestionResponse> questionResponses=new ArrayList<>();
        QuestionResponse mostRecentQuestionResponse = allResponses.get(allResponses.size()-1);
        questionResponses.add(mostRecentQuestionResponse);

        //loop through the other questionResponses, check if they are in the window
        for(int k=allResponses.size()-2;k>-1;k--){//k=allResponses.size()-2 to get all the index before the last element in the list
            if(!checkTimeStampDifference(allResponses.get(allResponses.size()-1).getMillSeconds(),allResponses.get(k).getMillSeconds())){
                questionResponses.add(allResponses.get(k));
            }
        }
        return questionResponses;
}

    /**
     * Calculates the seconds difference between two timestamps
     * @param firstTimeStamp newest timestamp
     * @param secondTimeStamp timestamp before last
     * @return true if the timestamps are 30 or more seconds apart/false if less than 30 seconds apart
     */
    public static boolean checkTimeStampDifference(long firstTimeStamp,long secondTimeStamp){
        long secondsDifference =Math.abs(firstTimeStamp-secondTimeStamp)/1000;
        if(secondsDifference>=SECONDS_BETWEEN) return true;
        return false;
    }

    public void addNewResponse(String newResponse){
        //creates a new timestamp for new response
        QuestionResponse questionResponse=new QuestionResponse(newResponse);
        this.allResponses.add(questionResponse);
    }

    public static boolean checkIfAnswerIsCorrect(String correctAnswer, String response){
        return correctAnswer.trim().equalsIgnoreCase(response.trim());
    }

    public static boolean checkIfAnswersAreCorrect(List<QuestionResponse> responsesToCheck, String correctAnswer){
        //look oldest response to most recent because older responses are more likely to be incorrect
        for(int i=responsesToCheck.size()-1; i>=0; i--){
            if(!checkIfAnswerIsCorrect(responsesToCheck.get(i).getResponseText(), correctAnswer)){
                return false;
            }
        }
        return true;
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

    public long getTimeStamp(){
        return allResponses.get(allResponsesSize()-1).getMillSeconds();
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


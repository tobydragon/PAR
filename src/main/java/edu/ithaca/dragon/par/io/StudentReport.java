package edu.ithaca.dragon.par.io;

import java.util.Map;

public class StudentReport {
    private String userId;
    private Map<String,Double> scorePerType;
    private Map<String,Integer> responsesPerType;
    private Map<String,Integer> questionsPerTypeTotal;
    private Map<String,Integer> numberOfQuestionsAnswered;


    //TODO: ADD A QUESTIONS ANSWERED?
    public StudentReport(String userIdIn,Map<String,Double> scorePerTypeIn,Map<String,Integer> responsesPerTypeIn,Map<String,Integer> questionsPerTypeIn,Map<String,Integer> numberOfQuestionsAnsweredIn){
        this.userId=userIdIn;
        this.scorePerType=scorePerTypeIn;
        this.responsesPerType=responsesPerTypeIn;
        this.questionsPerTypeTotal =questionsPerTypeIn;
        this.numberOfQuestionsAnswered=numberOfQuestionsAnsweredIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Double> getScorePerType() {
        return scorePerType;
    }

    public void setScorePerType(Map<String, Double> scorePerType) {
        this.scorePerType = scorePerType;
    }

    public Map<String, Integer> getResponsesPerType() {
        return responsesPerType;
    }

    public void setResponsesPerType(Map<String, Integer> responsesPerType) {
        this.responsesPerType = responsesPerType;
    }

    public Map<String, Integer> getQuestionsPerTypeTotal() {
        return questionsPerTypeTotal;
    }

    public void setQuestionsPerTypeTotal(Map<String, Integer> questionsPerTypeTotal) {
        this.questionsPerTypeTotal = questionsPerTypeTotal;
    }

    public Map<String, Integer> getNumberOfQuestionsAnswered() {
        return numberOfQuestionsAnswered;
    }

    public void setNumberOfQuestionsAnswered(Map<String, Integer> numberOfQuestionsAnswered) {
        this.numberOfQuestionsAnswered = numberOfQuestionsAnswered;
    }

}

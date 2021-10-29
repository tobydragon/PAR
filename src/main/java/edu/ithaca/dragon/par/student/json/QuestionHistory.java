package edu.ithaca.dragon.par.student.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionHistory {
    public String questionId;
    public List<Long> timesSeen;
    public List<Response> responses;

    public QuestionHistory(){}

    public QuestionHistory(String questionId) {
        this.questionId = questionId;
        timesSeen = new ArrayList<>();
        responses = new ArrayList<>();
    }

    public long checkLastTimeSeen() {
        return timesSeen.get(timesSeen.size()-1);
    }

    public void addTimeSeen(){
        addTimeSeen(new Date().getTime());
    }

    public void addTimeSeen(Long timestamp){
        timesSeen.add(timestamp);
    }

    public void addResponse(String newResponseText){
        responses.add(new Response(newResponseText));
    }

    public void addResponse(String newResponseText, Long timestamp){
        responses.add(new Response(newResponseText, timestamp));
    }

    public String getQuestionId() {
        return questionId;
    }

    public List<Long> getTimesSeen(){
        return timesSeen;
    }
}

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

    public int checkTimesSeenCount(){
        return timesSeen.size();
    }

    public void addTimeSeen(){
        timesSeen.add(new Date().getTime());
    }

    public void addResponse(String newResponseText){
        responses.add(new Response(newResponseText));
    }

    public String getQuestionId() {
        return questionId;
    }
}

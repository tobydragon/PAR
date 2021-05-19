package edu.ithaca.dragon.par.studentmodel2;

import edu.ithaca.dragon.par.studentModel.QuestionResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionHistory {
    private String questionId;
    private List<Long> timesSeen;
    private List<QuestionResponse> responses;

    public QuestionHistory(String questionId) {
        this.questionId = questionId;
        timesSeen = new ArrayList<>();
        responses = new ArrayList<>();
    }

    public void addTimeSeen(){
        timesSeen.add(new Date().getTime());
    }

    public void addResponse(String newResponseText){
        responses.add(new QuestionResponse(newResponseText));
    }

    public String getQuestionId() {
        return questionId;
    }
}

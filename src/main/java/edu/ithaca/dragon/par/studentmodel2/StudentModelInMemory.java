package edu.ithaca.dragon.par.studentmodel2;

import java.util.HashMap;
import java.util.Map;

public class StudentModelInMemory {
    private String studentId;
    private Map<String, QuestionHistory> questionHistories;

    public StudentModelInMemory(String studentId){
        this.studentId = studentId;
        questionHistories = new HashMap<>();
    }

    public void addTimeSeen(String questionId){
        getOrCreateHistory(questionId).addTimeSeen();
    }

    public void addResponse(String questionId, String newResponseText){
        getOrCreateHistory(questionId).addResponse(newResponseText);
    }


    private QuestionHistory getOrCreateHistory(String questionId){
        QuestionHistory questionHistory = questionHistories.get(questionId);
        if (questionHistory == null){
            questionHistory = new QuestionHistory(questionId);
            questionHistories.put(questionId, questionHistory);
        }
        return questionHistory;
    }

}

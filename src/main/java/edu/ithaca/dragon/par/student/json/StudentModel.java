package edu.ithaca.dragon.par.student.json;

import java.util.HashMap;
import java.util.Map;

public class StudentModel {
    public String studentId;
    public Map<String, QuestionHistory> questionHistories;

    public StudentModel(){ }

    public StudentModel(String studentId){
        this.studentId = studentId;
        questionHistories = new HashMap<>();
    }

    public int checkTimesSeenCount(String questionId){
        if (questionHistories.containsKey(questionId)){
            return questionHistories.get(questionId).checkTimesSeenCount();
        }
        else {
            return 0;
        }
    }

    public void addTimeSeen(String questionId){
        retrieveOrCreateNewHistory(questionId).addTimeSeen();
    }

    public void addResponse(String questionId, String newResponseText){
        retrieveOrCreateNewHistory(questionId).addResponse(newResponseText);
    }

    /**
     * @return the timestamp when the question was seen, or 0 if not seen
     */
    public long checkTimeLastSeen(String questionId){
        QuestionHistory questionHistory = questionHistories.get(questionId);
        if (questionHistory != null){
            return questionHistory.checkLastTimeSeen();
        }
        else {
            return 0;
        }
    }

    private QuestionHistory retrieveOrCreateNewHistory(String questionId){
        QuestionHistory questionHistory = questionHistories.get(questionId);
        if (questionHistory == null){
            questionHistory = new QuestionHistory(questionId);
            questionHistories.put(questionId, questionHistory);
        }
        return questionHistory;
    }

}

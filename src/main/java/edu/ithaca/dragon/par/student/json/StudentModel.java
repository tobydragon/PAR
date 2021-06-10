package edu.ithaca.dragon.par.student.json;

import edu.ithaca.dragon.par.domain.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StudentModel {
    public String studentId;
    public Map<String, QuestionHistory> questionHistories;

    public StudentModel(){ }

    public StudentModel(String studentId) {
        this(studentId, new ArrayList<>());
    }

    public StudentModel(String studentId, Collection<QuestionHistory> questionHistoryCollection) {
        this.studentId = studentId;
        questionHistories = new HashMap<>();
        questionHistoryCollection.forEach((questionHistory) -> questionHistories.put(questionHistory.getQuestionId(), questionHistory));
    }

    public void addTimeSeen(String questionId){
        retrieveOrCreateNewHistory(questionId).addTimeSeen();
    }

    public void addTimeSeen(String questionId, Long timestamp){
        retrieveOrCreateNewHistory(questionId).addTimeSeen(timestamp);
    }

    public void addResponse(String questionId, String newResponseText){
        retrieveOrCreateNewHistory(questionId).addResponse(newResponseText);
    }

    public void addResponse(String questionId, String newResponseText, Long timestamp){
        retrieveOrCreateNewHistory(questionId).addResponse(newResponseText, timestamp);
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

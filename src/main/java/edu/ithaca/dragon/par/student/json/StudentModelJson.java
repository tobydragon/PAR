package edu.ithaca.dragon.par.student.json;

import edu.ithaca.dragon.par.student.StudentModelInfo;

import java.util.*;

public class StudentModelJson implements StudentModelInfo {
    public String studentId;
    public Map<String, QuestionHistory> questionHistories;

    public StudentModelJson(){ }

    public StudentModelJson(String studentId) {
        this(studentId, new ArrayList<>());
    }

    public StudentModelJson(String studentId, Collection<QuestionHistory> questionHistoryCollection) {
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

    /**
     * @param questionIdsToCheck list of questions in the order in which to check
     * @return the id from questionIdsToCheck that has been seen least recently,
     *          will return the first question never seen if any question is never seen
     */
    @Override
    public String findQuestionSeenLeastRecently( List<String> questionIdsToCheck){
        long currTimestamp = new Date().getTime();
        if (questionIdsToCheck.size() >0) {
            String leastRecentQuestionSoFar = questionIdsToCheck.get(0);
            long timeElapsedForLeastRecent = currTimestamp - checkTimeLastSeen(leastRecentQuestionSoFar);
            for (String questionId : questionIdsToCheck) {
                long timeElapsed = currTimestamp - checkTimeLastSeen(questionId);
                if (timeElapsed > timeElapsedForLeastRecent) {
                    leastRecentQuestionSoFar = questionId;
                    timeElapsedForLeastRecent = timeElapsed;
                }
            }
            return leastRecentQuestionSoFar;
        }
        else {
            throw new IllegalArgumentException("empty questionIdsToCheck List");
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

    public Map<String, QuestionHistory> getQuestionHistories(){
        return questionHistories;
    }

}

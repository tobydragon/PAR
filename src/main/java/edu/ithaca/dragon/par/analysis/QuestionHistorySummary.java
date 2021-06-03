package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.student.json.QuestionHistory;

import java.util.List;
import java.util.Map;

//TODO: can make these: across everyone for all types or certain type, for 1 student for all types or certain type, window size too
public class QuestionHistorySummary {
    private List<String> questionIdsSeen;
    private List<String> questionsAnswered;
    private List<String> questionsCorrectFirstTime;
    private List<String> questionsCorrectAfterIncorrect;
    private List<String> questionsOnlyAnsweredIncorrect;


    public QuestionHistorySummary(Map<String, QuestionHistory> questionHistoryMap){
        questionIdsSeen = buildQuestionIdsSeen(questionHistoryMap);
        throw new RuntimeException("Not implemented");
    }

    public static List<String> buildQuestionIdsSeen(Map<String, QuestionHistory> questionHistoryMap){
        throw new RuntimeException("Not implemented");
    }

    public List<String> getQuestionIdsSeen() {
        return questionIdsSeen;
    }

    public List<String> getQuestionsAnswered() {
        return questionsAnswered;
    }

    public List<String> getQuestionsCorrectFirstTime() {
        return questionsCorrectFirstTime;
    }

    public List<String> getQuestionsCorrectAfterIncorrect() {
        return questionsCorrectAfterIncorrect;
    }

    public List<String> getQuestionsOnlyAnsweredIncorrect() {
        return questionsOnlyAnsweredIncorrect;
    }
}

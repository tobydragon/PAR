package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.student.json.QuestionHistory;

import java.util.ArrayList;
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
    }

    public static List<String> buildQuestionIdsSeen(Map<String, QuestionHistory> questionHistoryMap){
        List<String> questionIdsList;
        if (questionHistoryMap.size() >0){
            questionIdsList = new ArrayList<String>(questionHistoryMap.keySet());
        }
        else{
            throw new RuntimeException("No Question Ids found");
        }
        return questionIdsList;
    }

    // public static List<String> buildQuestionAnswered(Map<String, QuestionHistory> questionHistoryMap){
    //     return null;
    // }

    public static List<String> buildQuestionsCorrectFirstTime(Map<String, QuestionHistory> questionHistoryMap){
        List<QuestionHistory> questionHist = new ArrayList<QuestionHistory>(questionHistoryMap.values());
        List<String> questionFirstTime = new ArrayList<String>();

        for(int i=0; i < questionHist.size(); i++){
            if (questionHist.get(i).checkTimesSeenCount() == 1){
                questionFirstTime.add(questionHist.get(i).getQuestionId());
            }
        }
        return questionFirstTime;
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
}

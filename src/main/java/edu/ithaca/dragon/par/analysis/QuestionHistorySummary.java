package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.student.json.QuestionHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: can make these: across everyone for all types or certain type, for 1 student for all types or certain type, window size too
public class QuestionHistorySummary {
    private List<String> questionIdsSeen;
    private List<String> questionsResponded; // changed from questionsAnswered
    private List<String> questionsCorrect; // changed from questionsCorrectFirstTime
    private List<String> questionsCorrectAfterIncorrect; // changed from questionsCorrectAfterIncorrect
    private List<String> questionsIncorrect; // changed from questionsOnlyAnsweredIncorrect


    public QuestionHistorySummary(Map<String, QuestionHistory> questionHistoryMap){
        questionIdsSeen = buildQuestionIdsSeen(questionHistoryMap);
        questionsResponded = checkQuestionsResponded(questionHistoryMap);
    }

    public static List<String> buildQuestionIdsSeen(Map<String, QuestionHistory> questionHistoryMap){
        List<String> questionIdsList;
        if (questionHistoryMap.size() > 0){
            questionIdsList = new ArrayList<String>(questionHistoryMap.keySet());
        }
        else{
            throw new RuntimeException("No Question Ids found");
        }
        return questionIdsList;
    }

    public static List<String> checkQuestionsResponded(Map<String, QuestionHistory> questionHistoryMap){
        List<String> questionsAnsweredList = new ArrayList<String>();

        for( QuestionHistory questionHist : questionHistoryMap.values()){
            if (questionHist.responses.isEmpty() != true){
                questionsAnsweredList.add(questionHist.getQuestionId());
            }
        }
        return questionsAnsweredList;
    }

    public static List<String> findQuestionsCorrect(Map<String, QuestionHistory> questionHistoryMap, DomainDatasourceJson domainData){
        return null;
    }

    public List<String> getQuestionIdsSeen() {
        return questionIdsSeen;
    }

    public List<String> getQuestionsResponded() {
        return questionsResponded;
    }

    public List<String> getQuestionsCorrect() {
        return questionsCorrect;
    }
}

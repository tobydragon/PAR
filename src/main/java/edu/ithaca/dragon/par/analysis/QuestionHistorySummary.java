package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.student.json.QuestionHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//TODO: can make these: across everyone for all types or certain type, for 1 student for all types or certain type, window size too
public class QuestionHistorySummary {
    private List<String> questionIdsSeen;
    private List<String> questionsRespondedTo; // changed from questionsAnswered
    private List<String> questionsCorrect; // changed from questionsCorrectFirstTime
    private List<String> questionsCorrectAfterIncorrect; // changed from questionsCorrectAfterIncorrect
    private List<String> questionsIncorrect; // changed from questionsOnlyAnsweredIncorrect


    public QuestionHistorySummary(Collection<QuestionHistory> questionHistoryCollection){
        questionIdsSeen = buildQuestionIdsSeen(questionHistoryCollection);
        questionsRespondedTo = checkQuestionsRespondedTo(questionHistoryCollection);
    }

    public static List<String> buildQuestionIdsSeen(Collection<QuestionHistory> questionHistoryCollection){
        List<String> questionIdsList = new ArrayList<>();

        for(QuestionHistory questionHist : questionHistoryCollection){
            if (questionHistoryCollection.size() > 0){
                questionIdsList.add(questionHist.getQuestionId());
            }
            else{
                throw new RuntimeException("No Question Ids found");
            }
        }
        return questionIdsList;
    }

    public static List<String> checkQuestionsRespondedTo(Collection<QuestionHistory> questionHistoryCollection){
        List<String> questionsAnsweredList = new ArrayList<String>();

        for(QuestionHistory questionHist : questionHistoryCollection){
            if (questionHist.responses.isEmpty() != true){
                questionsAnsweredList.add(questionHist.getQuestionId());
            }
        }
        return questionsAnsweredList;
    }

    public static List<String> findQuestionsCorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceJson domainData){
        return null;
    }

    public List<String> getQuestionIdsSeen() {
        return questionIdsSeen;
    }

    public List<String> getQuestionsRespondedTo() {
        return questionsRespondedTo;
    }

    public List<String> getQuestionsCorrect() {
        return questionsCorrect;
    }
}

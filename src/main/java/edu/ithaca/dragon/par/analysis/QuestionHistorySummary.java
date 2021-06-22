package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.json.QuestionHistory;
import edu.ithaca.dragon.par.student.json.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO: can make these: across everyone for all types or certain type, for 1 student for all types or certain type, window size too
public class QuestionHistorySummary {
    private List<String> questionIdsSeen;
    private List<String> questionsRespondedTo;
    private List<String> questionsCorrectFirstTime;
    private List<String> questionsCorrectAfterIncorrect;
    private List<String> questionsIncorrect;


    public QuestionHistorySummary(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
        questionIdsSeen = buildQuestionIdsSeen(questionHistoryCollection);
        questionsRespondedTo = checkQuestionsRespondedTo(questionHistoryCollection);
        questionsCorrectFirstTime = findQuestionsCorrectFirstTime(questionHistoryCollection, domainData);
        questionsIncorrect = findQuestionsIncorrect(questionHistoryCollection, domainData);
        questionsCorrectAfterIncorrect = findQuestionsCorrectAfterIncorrect(questionHistoryCollection, domainData);
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
        List<String> questionsRespondedList = new ArrayList<String>();

        for(QuestionHistory questionHist : questionHistoryCollection){
            if (questionHist.responses.isEmpty() != true){
                questionsRespondedList.add(questionHist.getQuestionId());
            }
        }
        return questionsRespondedList;
    }

    public static List<String> findQuestionsCorrectFirstTime(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
        List<String> correctList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.findAllHistoriesWithResponses(questionHistoryCollection);
        
        for (QuestionHistory questionHist : historyOfQuestions){
            String correctAnswer = domainData.getQuestion(questionHist.getQuestionId()).getCorrectAnswer();
            if (questionHist.responses.iterator().next().getResponseText().equals(correctAnswer)){
                correctList.add(questionHist.getQuestionId());
            }
        }

        return correctList;
    }

    public static List<String> findQuestionsIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
        List<String> incorrectList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.findAllHistoriesWithResponses(questionHistoryCollection);
        for (QuestionHistory questionHist : historyOfQuestions){
            boolean hasCorrectAnswer = false;

            for(Response questionResponse : questionHist.responses){
                String correctAnswer = domainData.getQuestion(questionHist.getQuestionId()).getCorrectAnswer();
                if (questionResponse.getResponseText().equals(correctAnswer) ){
                    hasCorrectAnswer = true;
                }
            }
            if (!hasCorrectAnswer){
                incorrectList.add(questionHist.getQuestionId());
            }
        }
        
        return incorrectList;
    }


    public static List<String> findQuestionsCorrectAfterIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
        List<String> correctAfterIncorrect = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.findAllHistoriesWithResponses(questionHistoryCollection);
        
        for (QuestionHistory questionHist : historyOfQuestions){
            for(Response questionResponse : questionHist.responses){
                String correctAnswer = domainData.getQuestion(questionHist.getQuestionId()).getCorrectAnswer();
                if (questionResponse.getResponseText().equals(correctAnswer) && !questionHist.responses.get(0).getResponseText().equals(correctAnswer)){
                    correctAfterIncorrect.add(questionHist.getQuestionId()); 
                }
            }
        }
        
        return correctAfterIncorrect;
    }

    public static List<QuestionHistory> findAllHistoriesWithResponses(Collection<QuestionHistory> questionHistoryCollection){
        List<QuestionHistory> questionsWithResponses = new ArrayList<QuestionHistory>();

        for(QuestionHistory questionHist : questionHistoryCollection){
            if (questionHist.responses.isEmpty() != true){
                questionsWithResponses.add(questionHist);
            }
        }
        return questionsWithResponses;
    }

    public List<String> getQuestionIdsSeen() {
        return questionIdsSeen;
    }

    public List<String> getQuestionsRespondedTo() {
        return questionsRespondedTo;
    }

    public List<String> getQuestionsCorrectFirstTime() {
        return questionsCorrectFirstTime;
    }

    public List<String> getQuestionsIncorrect(){
        return questionsIncorrect;
    }

    public List<String> getQuestionsCorrectAfterIncorrect(){
        return questionsCorrectAfterIncorrect;
    }
}

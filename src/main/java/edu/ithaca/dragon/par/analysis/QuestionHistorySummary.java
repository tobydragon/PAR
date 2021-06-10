package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceSimple;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.json.QuestionHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//TODO: can make these: across everyone for all types or certain type, for 1 student for all types or certain type, window size too
public class QuestionHistorySummary {

    private List<String> questionIdsSeen;
    private List<String> questionsRespondedTo;
    private List<String> questionsCorrect;
    private List<String> questionsCorrectAfterIncorrect;
    private List<String> questionsIncorrect;


    public QuestionHistorySummary(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceSimple domainData){
        questionIdsSeen = buildQuestionIdsSeen(questionHistoryCollection);
        questionsRespondedTo = checkQuestionsRespondedTo(questionHistoryCollection);
        questionsCorrect = findQuestionsCorrect(questionHistoryCollection, domainData);
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

    public static List<String> findQuestionsCorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceSimple domainData){
        List<String> correctList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.checkForBlanks(questionHistoryCollection);
        
        for(int i=0; i < historyOfQuestions.size(); i++){
            String correctResponse = historyOfQuestions.get(i).responses.get(0).getResponseText();
            String listOfQuestions = domainData.getQuestion(historyOfQuestions.get(i).getQuestionId()).getCorrectAnswer();
            if(correctResponse.equals(listOfQuestions)){
                correctList.add(listOfQuestions);
            }
        }
        return correctList;
    }

    public static List<String> findQuestionsIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceSimple domainData){
        List<String> incorrectList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.checkForBlanks(questionHistoryCollection);

        for(int i=0; i < historyOfQuestions.size(); i++){
            String incorrectResponse = historyOfQuestions.get(i).responses.get(0).getResponseText();
            String listOfQuestions = domainData.getQuestion(historyOfQuestions.get(i).getQuestionId()).getCorrectAnswer();
            if(!incorrectResponse.equals(listOfQuestions)){
                incorrectList.add(incorrectResponse);
            }
        }
        return incorrectList;
    }


    public static List<String> findQuestionsCorrectAfterIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceSimple domainData){
        List<String> correctAfterIncorrect = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.checkForBlanks(questionHistoryCollection);
        for(int i=0; i < historyOfQuestions.size(); i++){
            for(int j=0; j < historyOfQuestions.get(i).responses.size(); j++){                
                String correctAfterIncorrectResponse = historyOfQuestions.get(i).responses.get(j).getResponseText();
                String listOfQuestions = domainData.getQuestion(historyOfQuestions.get(i).getQuestionId()).getCorrectAnswer();    
                if(correctAfterIncorrectResponse.equals(listOfQuestions)){
                    correctAfterIncorrect.add(correctAfterIncorrectResponse);
                }
            }
        }
        return correctAfterIncorrect;
    }

    public static List<QuestionHistory> checkForBlanks(Collection<QuestionHistory> questionHistoryCollection){
        List<QuestionHistory> questionsNoBlanks = new ArrayList<QuestionHistory>();

        for(QuestionHistory questionHist : questionHistoryCollection){
            if (questionHist.responses.isEmpty() != true){
                questionsNoBlanks.add(questionHist);
            }
        }
        return questionsNoBlanks;
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

    public List<String> getQuestionsIncorrect(){
        return questionsIncorrect;
    }

    public List<String> getQuestionsCorrectAfterIncorrect(){
        return questionsCorrectAfterIncorrect;
    }
}

package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.json.QuestionHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO: can make these: across everyone for all types or certain type, for 1 student for all types or certain type, window size too
public class QuestionHistorySummary {
    private List<String> questionIdsSeen;
    private List<String> questionsRespondedTo;
    private List<String> questionsCorrect;
    private List<String> questionsCorrectAfterIncorrect;
    private List<String> questionsIncorrect;


    public QuestionHistorySummary(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
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

    public static List<String> findQuestionsCorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
        List<String> correctList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.findAllHistoriesWithResponses(questionHistoryCollection);
        //TODO: get the question id
        //TODO: use for each loop

        for(int i=0; i < historyOfQuestions.size(); i++){
            String correctAnswer = domainData.getQuestion(historyOfQuestions.get(i).getQuestionId()).getCorrectAnswer();
            if(historyOfQuestions.get(i).responses.get(0).getResponseText().equals(correctAnswer)){
                correctList.add(historyOfQuestions.get(i).getQuestionId());
            }
        }

        return correctList;
    }

    public static List<String> findQuestionsIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
        List<String> incorrectList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.findAllHistoriesWithResponses(questionHistoryCollection);

        for(int i=0; i < historyOfQuestions.size(); i++){
            String listOfQuestions = domainData.getQuestion(historyOfQuestions.get(i).getQuestionId()).getCorrectAnswer();
            if(!historyOfQuestions.get(i).responses.get(0).getResponseText().equals(listOfQuestions)){
                incorrectList.add(historyOfQuestions.get(i).getQuestionId());
            }
        }
        return incorrectList;
    }


    public static List<String> findQuestionsCorrectAfterIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasource domainData){
        List<String> correctAfterIncorrect = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = QuestionHistorySummary.findAllHistoriesWithResponses(questionHistoryCollection);
        for(int questionIndex=0; questionIndex < historyOfQuestions.size(); questionIndex++){
            for(int responseIndex=0; responseIndex < historyOfQuestions.get(questionIndex).responses.size(); responseIndex++){                
                String listOfQuestions = domainData.getQuestion(historyOfQuestions.get(questionIndex).getQuestionId()).getCorrectAnswer();    
                if(historyOfQuestions.get(questionIndex).responses.get(responseIndex).getResponseText().equals(listOfQuestions)){
                    correctAfterIncorrect.add(historyOfQuestions.get(questionIndex).getQuestionId());
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

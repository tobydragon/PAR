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
        List<String> questionsAnsweredList = new ArrayList<String>();

        for(QuestionHistory questionHist : questionHistoryCollection){
            if (questionHist.responses.isEmpty() != true){
                questionsAnsweredList.add(questionHist.getQuestionId());
            }
        }
        return questionsAnsweredList;
    }

    public static List<String> findQuestionsCorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceSimple domainData){
        List<String> correctList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = new ArrayList<QuestionHistory>(questionHistoryCollection);
        List<Question> listOfQuestions = domainData.getAllQuestions();
        
        for(int i=0; i < historyOfQuestions.size(); i++){
            String correctResponse = historyOfQuestions.get(i).responses.get(0).getResponseText();
            if(correctResponse.equals(listOfQuestions.get(i).getCorrectAnswer())){
                correctList.add(listOfQuestions.get(i).getCorrectAnswer());
            }
        }
        return correctList;
    }

    public static List<String> findQuestionsIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceSimple domainData){
        List<String> incorrectList = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = new ArrayList<QuestionHistory>(questionHistoryCollection);
        List<Question> listOfQuestions = domainData.getAllQuestions();

        for(int i=0; i < historyOfQuestions.size(); i++){
            String incorrectResponse = historyOfQuestions.get(i).responses.get(0).getResponseText();
            if(!incorrectResponse.equals(listOfQuestions.get(i).getCorrectAnswer())){
                incorrectList.add(historyOfQuestions.get(i).responses.get(0).getResponseText());
            }
        }
        return incorrectList;
    }


    public static List<String> findQuestionsCorrectAfterIncorrect(Collection<QuestionHistory> questionHistoryCollection, DomainDatasourceSimple domainData){
        List<String> correctAfterIncorrect = new ArrayList<String>();
        List<QuestionHistory> historyOfQuestions = new ArrayList<QuestionHistory>(questionHistoryCollection);
        List<Question> listOfQuestions = domainData.getAllQuestions();

        for(int i=0; i < historyOfQuestions.size(); i++){
            for(int j=0; j < historyOfQuestions.get(i).responses.size(); j++){
                String correctAfterIncorrectResponse = historyOfQuestions.get(i).responses.get(j).getResponseText();
                if(correctAfterIncorrectResponse.equals(listOfQuestions.get(i).getCorrectAnswer())){
                    correctAfterIncorrect.add(correctAfterIncorrectResponse);
                }
            }
        }
        return correctAfterIncorrect;
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

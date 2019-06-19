package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.Datastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionPool {
    List<Question> allQuestions;
    Datastore datastore;


    public QuestionPool(Datastore datastoreIn) throws IOException {
        datastore = datastoreIn;
        allQuestions = datastore.loadQuestions();
    }

    public List<Question> getAllQuestions(){
        return allQuestions;
    }

    public Question getQuestionFromId(String questionIdIn){
        for(int i = 0; i < allQuestions.size(); i++){
            if(allQuestions.get(i).getId().equals(questionIdIn))
                return allQuestions.get(i);
        }
        throw new RuntimeException("Question with id:" + questionIdIn + " does not exist");
    }

    public List<Question> getQuestionsFromUrl(String imageUrlIn){
        List<Question> toReturn = new ArrayList<>();
        for(int i = 0; i < allQuestions.size(); i++){
            if(allQuestions.get(i).getImageUrl().equals(imageUrlIn))
                toReturn.add(allQuestions.get(i));
        }
        return toReturn;
    }

    public List<Question> getQuestionsFromIds(List<String> idsIn){
        return null;
    }
}
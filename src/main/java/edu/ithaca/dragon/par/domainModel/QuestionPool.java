package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.Datastore;

import java.io.IOException;
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
        return null;
    }

    public List<Question> getQuestionsFromUrl(String imageUrlIn){
        return null;
    }
}
package edu.ithaca.dragon.par.domain;

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

    public void addQuestion(Question questionIn){
        allQuestions.add(questionIn);
    }

    public void removeQuestion(Question questionIn){
        allQuestions.remove(questionIn);
    }

    public List<Question> getAllQuestions(){
        return allQuestions;
    }
}

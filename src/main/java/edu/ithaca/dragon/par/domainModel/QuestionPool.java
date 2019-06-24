package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.Datastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionPool {
    private List<Question> allQuestions;
    private Datastore datastore;


    public QuestionPool(Datastore datastoreIn) throws IOException {
        datastore = datastoreIn;
        allQuestions = datastore.loadQuestions();
    }

    public List<Question> getAllQuestions(){
        return new ArrayList<>(allQuestions);
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
        List<Question> toReturn = new ArrayList<>();
        boolean validId;

        //TODO: find a better algorithm?
        for(String currId : idsIn){
            validId = false;
            for(Question currQuestion : allQuestions){
                if(currQuestion.getId().equals(currId)){
                    toReturn.add(currQuestion);
                    validId = true;
                }
            }
            if(!validId) //the id is invalid
                throw new RuntimeException("Question with id:" + currId + " does not exist");
        }

        return toReturn;
    }
}

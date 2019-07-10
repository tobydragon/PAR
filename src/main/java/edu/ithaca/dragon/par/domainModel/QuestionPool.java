package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.Datastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public boolean checkWindowSize(int desiredWindowSize){
        if(desiredWindowSize<1){
            return false;
        }
        //create parallel arrays of types and count of times seen
        List<String> enumNames = Stream.of(EquineQuestionTypes.values()).map(Enum::name).collect(Collectors.toList());
        List<Integer> typeCounts = new ArrayList<>();

        //initialize typeCounts with 0s
        for(int i=0; i<enumNames.size(); i++){
            typeCounts.add(0);
        }

        //loop through all questions
        for(Question currQuestion : allQuestions){

            //find which type it is
            for(int i=0; i<enumNames.size(); i++){
                if(enumNames.get(i).equals(currQuestion.getType())){
                    //increment the typecount
                    typeCounts.set(i, typeCounts.get(i) + 1);
                    break;
                }
            }
        }

        //check if the typecounts are high enough
        for(int i = 0; i<typeCounts.size();i++){
            if(typeCounts.get(i) < desiredWindowSize)
                return false;
        }
        return true;
    }
}

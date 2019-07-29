package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestionPool {

    private List<Question> allQuestions;

    public QuestionPool(List<Question> allQuestions){
        this.allQuestions = new ArrayList<>(allQuestions);
    }

    public List<Question> getAllQuestions(){
        return new ArrayList<>(allQuestions);
    }

    public void addQuestion(Question newQuestion){
        allQuestions.add(newQuestion);
    }

    public Question getQuestionFromId(String questionIdIn){
        Question q = getQuestionFromId(questionIdIn, allQuestions);
        if(q == null){
            throw new RuntimeException("Question with id:" + questionIdIn + " does not exist");
        }
        return q;
    }

    public boolean isIdTaken(String idToCheck){
        return getQuestionFromId(idToCheck, allQuestions) != null;
    }

    public static Question getQuestionFromId(String questionIdIn, List<Question> questionList){

        for(Question q : questionList){
            if(q.getId().equals(questionIdIn)){
                return q;
            }

            //call getQuestionFromId on the followup questions
            Question q2 = getQuestionFromId(questionIdIn, q.getFollowupQuestions());
            if(q2 != null){
                return q2;
            }
        }
        return null;
    }

    public void removeQuestionById(String id){
        removeQuestionFromIdRecursive(id, allQuestions);
    }

    public void removeQuestionFromIdRecursive(String id, List<Question> questionList){
        Iterator<Question> itr = questionList.listIterator();
        while(itr.hasNext()){
            Question currQuestion = itr.next();
            if(currQuestion.getId().equals(id)){
                itr.remove();
            }
            else{
                //call getQuestionFromId on the followup questions
                removeQuestionFromIdRecursive(id, currQuestion.getFollowupQuestions());
            }
        }
    }

    public static List<Question> getTopLevelQuestionsFromUrl(List<Question> questions, String imageUrlIn){
        List<Question> toReturn = new ArrayList<>();
        for(int i=0; i < questions.size(); i++){
            if(questions.get(i).getImageUrl().equals(imageUrlIn))
                toReturn.add(questions.get(i));
        }
        return toReturn;
    }

    public List<Question> getQuestionsFromUrl(String questionUrl){
        return QuestionPool.getQuestionsWithUrl(allQuestions, questionUrl);
    }

    public static List<Question> getQuestionsWithUrl(List<Question> questionList, String questionUrl){
        List<Question> toReturn = new ArrayList<>();
        for(Question questionToCheck : questionList){
            if(questionToCheck.getImageUrl().equals(questionUrl)){
                toReturn.add(questionToCheck);
            }
            toReturn.addAll(getQuestionsWithUrl(questionToCheck.getFollowupQuestions(), questionUrl));
        }
        return toReturn;
    }

    public Question getTopLevelQuestionById(String id){
        for (int i = 0; i <allQuestions.size(); i++){
            if (allQuestions.get(i).getId().equals(id)){
                return allQuestions.get(i);
            }
        }
        return null;
    }
}

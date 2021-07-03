package edu.ithaca.dragon.par.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DomainDatasourceSimple implements DomainDatasource {

    private List<Question> questions;

    public DomainDatasourceSimple(List<Question> questions){
        this.questions = questions;
    }

    @Override
    public List<Question> getAllQuestions() {
        return questions;
    }

    @Override
    public Question getQuestion(String id) {
        for (Question question : questions){
            if (question.getId().equals(id)){
                return question;
            }
        }
        throw new IllegalArgumentException("Question not found for id:" + id);
    }

    @Override
    public List<String> retrieveAllConcepts() {
        List<String> concepts = new ArrayList<>();

        for(Question question:questions){
            if(!concepts.contains(question.getType())){
                concepts.add(question.getType());
            }
        }
        return concepts;
    }

    @Override
    public String retrieveConceptForAQuestion(String id) {
        for (Question question: questions){
            if (question.getId().equalsIgnoreCase(id)){
                return question.getType();
            }
        }
        throw new IllegalArgumentException("No question found, bad ID:" + id);
    }

    @Override
    public List<Question> retrieveQuestionsByConcept(String concept) {
        List<Question> questionList = new ArrayList<>();
        for (Question question: questions){
            if (question.getType().equalsIgnoreCase(concept)){
                questionList.add(question);
            }
        }
        if(questionList.size()==0){
            throw new IllegalArgumentException("No questions found, bad concept: "+concept);
        }
        else{
            return questionList;
        }
    }

    
}

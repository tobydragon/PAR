package edu.ithaca.dragon.par.domain;

import java.util.List;

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
}

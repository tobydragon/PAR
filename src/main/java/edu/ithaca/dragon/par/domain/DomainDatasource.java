package edu.ithaca.dragon.par.domain;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.List;

public interface DomainDatasource {
    List<Question> getAllQuestions();
    Question getQuestion(String id);

}

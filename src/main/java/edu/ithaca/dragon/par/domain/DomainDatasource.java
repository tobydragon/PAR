package edu.ithaca.dragon.par.domain;

import java.util.List;

public interface DomainDatasource {
    List<Question> getAllQuestions();
    Question getQuestion(String id);

}

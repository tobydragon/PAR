package edu.ithaca.dragon.par.domain;

import java.util.List;

public interface DomainDatasource {

    List<Question> getAllQuestions();

    /**
     * @throws IllegalArgumentException if id is not found
     */
    Question getQuestion(String id);

}

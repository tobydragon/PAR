package edu.ithaca.dragon.par.domain;

import java.util.List;
import java.util.Set;

public interface DomainDatasource {

    List<Question> getAllQuestions();

    /**
     * @throws IllegalArgumentException if id is not found
     */
    Question getQuestion(String id);

    List<String> retrieveAllConcepts();

    String retrieveConceptForAQuestion(String id);

    List<Question> retrieveQuestionsByConcept(String concept);
}

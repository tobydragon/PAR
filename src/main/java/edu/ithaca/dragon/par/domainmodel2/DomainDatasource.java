package edu.ithaca.dragon.par.domainmodel2;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.List;

public interface DomainDatasource {
    public List<Question> getAllQuestions();
}

package edu.ithaca.dragon.par.cohort;

import edu.ithaca.dragon.par.pedagogy.QuestionChooser;

public interface CohortDatasource {
    public QuestionChooser getQuestionChooser(String studentId);
}

package edu.ithaca.dragon.par.cohort;

import edu.ithaca.dragon.par.pedagogicalmodel2.QuestionChooser;

public interface CohortDatasource {
    public QuestionChooser getQuestionChooser(String studentId);
}

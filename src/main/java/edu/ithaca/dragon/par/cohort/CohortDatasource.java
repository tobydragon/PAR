package edu.ithaca.dragon.par.cohort;

import java.util.List;

import edu.ithaca.dragon.par.pedagogy.QuestionChooser;

public interface CohortDatasource {
    public QuestionChooser getQuestionChooser(String studentId);
    public List<String> getCohortIds();
    public void addStudentToCohort(String cohortId, String studentId);
}

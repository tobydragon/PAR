package edu.ithaca.dragon.par.student;

import java.util.Collection;
import java.util.List;

import edu.ithaca.dragon.par.student.json.QuestionHistory;

public interface StudentModelInfo {
    String findQuestionSeenLeastRecently( List<String> questionIdsToCheck);
    Collection<QuestionHistory> getQuestionHistories();
}

package edu.ithaca.dragon.par.student;

import java.util.List;
import java.util.Map;

import edu.ithaca.dragon.par.student.json.QuestionHistory;

public interface StudentModelInfo {
    String findQuestionSeenLeastRecently( List<String> questionIdsToCheck);
    Map<String, QuestionHistory> getQuestionHistories();
}

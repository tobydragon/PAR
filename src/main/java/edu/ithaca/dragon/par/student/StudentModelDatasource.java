package edu.ithaca.dragon.par.student;

import java.util.List;

public interface StudentModelDatasource {

    boolean idIsAvailable(String studentId);
    String findQuestionSeenLeastRecently(String studentId, List<String> questionIdsToCheck);

    //mutators
    void addTimeSeen(String studentId, String questionId);
    void addResponse(String studentId, String questionId, String newResponseText);
    void createNewModelForId(String newId);
}

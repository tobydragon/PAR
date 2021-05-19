package edu.ithaca.dragon.par.studentmodel2;

import edu.ithaca.dragon.par.studentModel.QuestionResponse;

import java.io.IOException;
import java.util.List;

public interface StudentModelDatasource {

    boolean idIsAvailable(String studentId);
    String findQuestionLeastSeen(String studentId, List<String> questionIdsToCheck);

    //mutators
    void addTimeSeen(String studentId, String questionId);
    void addResponse(String studentId, String questionId, String newResponseText);
    void createNewModelForId(String newId);
}

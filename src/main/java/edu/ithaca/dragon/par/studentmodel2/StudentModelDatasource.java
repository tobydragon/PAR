package edu.ithaca.dragon.par.studentmodel2;

import edu.ithaca.dragon.par.studentModel.QuestionResponse;

import java.io.IOException;

public interface StudentModelDatasource {

    boolean idIsAvailable(String studentId);

    //mutators
    void addTimeSeen(String studentId, String questionId);
    void addResponse(String studentId, String questionId, String newResponseText);
    void createNewModelForId(String newId);
}

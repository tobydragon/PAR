package edu.ithaca.dragon.par.student;

import edu.ithaca.dragon.par.student.json.StudentModelJson;

public interface StudentModelDatasource {

    boolean idIsAvailable(String studentId);
    StudentModelInfo getStudentModel(String studentId);

    //mutators
    void addTimeSeen(String studentId, String questionId);
    void addResponse(String studentId, String questionId, String newResponseText);
    void createNewModelForId(String newId);
}

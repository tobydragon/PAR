package edu.ithaca.dragon.par.student;

public interface StudentModelDatasource {

    boolean idIsAvailable(String studentId);
    StudentModelInfo getStudentModel(String studentId);

    //mutators
    void addTimeSeen(String studentId, String questionId);
    void addResponse(String studentId, String questionId, String newResponseText);
    void createNewModelForId(String newId);
}

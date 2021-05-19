package edu.ithaca.dragon.par.studentmodel2;

import java.util.HashMap;
import java.util.Map;

public class StudentModelDatasourceInMemory implements StudentModelDatasource{
    private Map<String, StudentModelInMemory> studentMap;


    public StudentModelDatasourceInMemory(){
        studentMap = new HashMap<>();
    }

    @Override
    public boolean idIsAvailable(String studentId) {
        return studentMap.get(studentId) == null;
    }

    @Override
    public void addTimeSeen(String studentId, String questionId) {
        studentMap.get(studentId).addTimeSeen(questionId);
    }

    @Override
    public void addResponse(String studentId, String questionId, String newResponseText) {
        studentMap.get(studentId).addResponse(questionId, newResponseText);
    }

    @Override
    public void createNewModelForId(String newId) {
        if(idIsAvailable(newId)){
            studentMap.put(newId, new StudentModelInMemory(newId));
        }
        else {
            throw new IllegalArgumentException("id is already taken: " + newId);
        }
    }
}

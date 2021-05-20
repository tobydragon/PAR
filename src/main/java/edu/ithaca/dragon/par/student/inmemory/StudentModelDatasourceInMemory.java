package edu.ithaca.dragon.par.student.inmemory;

import edu.ithaca.dragon.par.student.StudentModelDatasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentModelDatasourceInMemory implements StudentModelDatasource {
    private Map<String, StudentModelInMemory> studentMap;


    public StudentModelDatasourceInMemory(){
        studentMap = new HashMap<>();
    }

    @Override
    public boolean idIsAvailable(String studentId) {
        return studentMap.get(studentId) == null;
    }

    @Override
    public String findQuestionLeastSeen(String studentId, List<String> questionIdsToCheck){
        if (questionIdsToCheck.size() > 0) {
            StudentModelInMemory studModel = studentMap.get(studentId);
            String questionLeastSeen = questionIdsToCheck.get(0);
            int numTimesLeastHasBeenSeen = studModel.getTimesSeenCount(questionIdsToCheck.get(0));
            for (String questionIdToCheck : questionIdsToCheck) {
                int numTimesSeen = studModel.getTimesSeenCount(questionIdToCheck);
                if (numTimesSeen < numTimesLeastHasBeenSeen) {
                    numTimesLeastHasBeenSeen = numTimesSeen;
                    questionLeastSeen = questionIdToCheck;
                }
            }
            return questionLeastSeen;
        }
        else {
            throw new IllegalArgumentException("no question ids included, empty list");
        }
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

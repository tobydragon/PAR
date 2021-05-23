package edu.ithaca.dragon.par.student.json;

import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentModelDatasourceJson implements StudentModelDatasource {
    private final String id;
    private final String directoryPath;
    private final JsonIoHelper jsonIoHelper;
    private final Map<String, StudentModel> studentMap;

    public StudentModelDatasourceJson(String id, String directoryPath, JsonIoHelper jsonIoHelper){
        this.studentMap = new HashMap<>();
        this.id = id;
        this.jsonIoHelper = jsonIoHelper;
        this.directoryPath = directoryPath;
    }

    @Override
    public boolean idIsAvailable(String studentId) {
        if (studentMap.get(studentId) == null) {
            String fullFileName = directoryPath + "/" + studentId + ".json";
            if (!jsonIoHelper.getReadAndWriteFile(fullFileName).exists()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String findQuestionSeenLeastRecently(String studentId, List<String> questionIdsToCheck){
        long currTimestamp = new Date().getTime();
        if (questionIdsToCheck.size() >0) {
            String leastRecentQuestionSoFar = questionIdsToCheck.get(0);
            StudentModel studentModel = getStudentModel(studentId);
            if (studentModel != null) {
                long timeElapsedForLeastRecent = currTimestamp - studentModel.checkTimeLastSeen(leastRecentQuestionSoFar);
                for (String questionId : questionIdsToCheck) {
                    long timeElapsed = currTimestamp - studentModel.checkTimeLastSeen(questionId);
                    if (timeElapsed > timeElapsedForLeastRecent) {
                        leastRecentQuestionSoFar = questionId;
                        timeElapsedForLeastRecent = timeElapsed;
                    }
                }
                return leastRecentQuestionSoFar;
            }
            else {
              throw new IllegalArgumentException("Bad student id: " +studentId);
            }

        }
        else {
            throw new IllegalArgumentException("empty questionIdsToCheck List");
        }
    }


    @Override
    public void addTimeSeen(String studentId, String questionId) {
        StudentModel studentModel =  getStudentModel(studentId);
        studentModel.addTimeSeen(questionId);
        overwriteStudentFile(studentModel);
    }

    @Override
    public void addResponse(String studentId, String questionId, String newResponseText) {
        StudentModel studentModel =  getStudentModel(studentId);
        studentModel.addResponse(questionId, newResponseText);
        overwriteStudentFile(studentModel);
    }

    @Override
    public void createNewModelForId(String newId) {
        if(idIsAvailable(newId)){
            StudentModel studentModel = new StudentModel(newId);
            studentMap.put(newId, studentModel);
            overwriteStudentFile(studentModel);
        }
        else {
            throw new IllegalArgumentException("id is already taken: " + newId);
        }
    }

    public StudentModel getStudentModel(String studentId) {
        StudentModel studentModel = studentMap.get(studentId);

        //if the student wasn't in the map, try to load from file
        if (studentModel == null) {
            try {
                studentModel = loadStudentModelFromFileIfPresent(studentId);
                //the student didn't have a file, create a new student
                if (studentModel != null) {
                    studentMap.put(studentId, studentModel);
                } else {
                    throw new IllegalArgumentException("No student model for id:" + studentId);
                }
            }
            catch(IOException e){
                throw new IllegalArgumentException("IOException for :" + studentId +  " caused by:\n" + e.toString());
            }
        }
        return studentModel;
    }

    private StudentModel loadStudentModelFromFileIfPresent(String studentId) throws IOException{
        String fullFileName = directoryPath + "/" + studentId + ".json";
        //check if file exists, return null if it doesn't
        File checkFile = jsonIoHelper.getReadAndWriteFile(fullFileName);
        if(!checkFile.exists()){
            return null;
        }
        return new JsonIoUtil(jsonIoHelper).fromFile(fullFileName, StudentModel.class);
    }

    private void overwriteStudentFile(StudentModel studentToWrite){
        String fullFileName = directoryPath + "/" + studentToWrite.studentId + ".json";
        try {
            new JsonIoUtil(jsonIoHelper).toFile(fullFileName, studentToWrite);
        }
        catch(IOException e){
            throw new IllegalArgumentException("IOException for :" + fullFileName +  " caused by:\n" + e.toString());
        }
    }
}

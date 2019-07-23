package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.FileSystemUtil;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonStudentModelDatastore extends JsonQuestionPoolDatastore implements Datastore{

    private String studentModelFilePath;

    public JsonStudentModelDatastore(String questionFilePath, String studentModelFilePath) throws IOException {
        super(questionFilePath);
        this.studentModelFilePath = studentModelFilePath;
    }

    //TODO: rename
    @Override
    public List<Question> loadQuestions(){
        return getAllQuestions();
    }


    @Override
    public List<StudentModel> loadStudentModels() throws IOException {
        if (studentModelFilePath != null) {
            List<StudentModel> studentModels = new ArrayList<>();
            for (String filePath : FileSystemUtil.addPathToFilenames(studentModelFilePath, FileSystemUtil.findAllFileNamesInDir(studentModelFilePath, "json"))) {
                StudentModelRecord newSMR = JsonUtil.fromJsonFile(filePath, StudentModelRecord.class);
                studentModels.add(newSMR.buildStudentModel(questionPool));
            }
            return studentModels;
        }
        else {
            throw new IOException("Trying to load student models but no path present");
        }
    }

    @Override
    public StudentModel loadStudentModel(String userId) throws IOException{
        String fullFileName = studentModelFilePath + "/" + userId + ".json";

        //check if file exists, return null if it doesn't
        File checkFile = new File(fullFileName);
        if(!checkFile.exists()){
            return null;
        }

        StudentModelRecord SMR = JsonUtil.fromJsonFile(fullFileName, StudentModelRecord.class);
        return SMR.buildStudentModel(questionPool);
    }

    @Override
    public void saveStudentModels(Collection<StudentModel> studentModelsIn) throws IOException {
        if(studentModelFilePath != null) {
            for(StudentModel currStudentModel : studentModelsIn){
                saveStudentModel(currStudentModel);
            }
        }
        else {
            throw new IOException("studentModelFilePath is null");
        }

    }

    @Override
    public void saveStudentModel(StudentModel studentModel) throws IOException{
        if(studentModelFilePath == null)
            throw new IOException("studentModelFilePath is null");
        String fullFilePath = studentModelFilePath + "/" +  studentModel.getUserId() + ".json";
        JsonUtil.toJsonFile(fullFilePath, new StudentModelRecord(studentModel));
    }
}

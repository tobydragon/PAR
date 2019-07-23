package edu.ithaca.dragon.par.io.springio;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.StudentModelDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.FileSystemUtil;
import edu.ithaca.dragon.util.JsonSpringUtil;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonSpringStudentModelDatastore extends JsonSpringQuestionPoolDatastore implements StudentModelDatastore {

    private String currentStudentModelDir;


    public JsonSpringStudentModelDatastore(String currentQuestionPoolLocation, String defaultQuestionPoolLocation, String currentStudentModelDir) throws IOException{
        super(currentQuestionPoolLocation, defaultQuestionPoolLocation );
        this.currentStudentModelDir = currentStudentModelDir;
    }

    @Override
    public StudentModel loadStudentModel(String userId) {
        String fullFileName = currentStudentModelDir + "/" + userId + ".json";
        try {
            StudentModelRecord studentModelRecord = JsonSpringUtil.fromFileSystemJson(fullFileName, StudentModelRecord.class);
            return studentModelRecord.buildStudentModel(questionPool);
        }
        catch (IOException e){
            return null;
        }
    }

    @Override
    public List<StudentModel> loadStudentModels() throws IOException {
        List<StudentModel> studentModels = new ArrayList<>();
        File studentModelFilePath = new FileSystemResource(currentStudentModelDir).getFile();
        for (String filePath : FileSystemUtil.addPathToFilenames(studentModelFilePath.toString(), FileSystemUtil.findAllFileNamesInDir(studentModelFilePath, "json"))) {
            StudentModelRecord newSMR = JsonSpringUtil.fromFileSystemJson(filePath, StudentModelRecord.class);
            studentModels.add(newSMR.buildStudentModel(questionPool));
        }
        return studentModels;
    }

    @Override
    public void saveStudentModel(StudentModel studentModel) throws IOException {
        String fullFilePath = currentStudentModelDir + "/" +  studentModel.getUserId() + ".json";
        JsonSpringUtil.toFileSystemJson(fullFilePath, new StudentModelRecord(studentModel));
    }

    @Override
    public void saveStudentModels(Collection<StudentModel> studentModelsIn) throws IOException {
        for(StudentModel currStudentModel : studentModelsIn){
            saveStudentModel(currStudentModel);
        }
    }


}

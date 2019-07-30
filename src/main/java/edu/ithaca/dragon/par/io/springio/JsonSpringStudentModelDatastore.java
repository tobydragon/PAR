package edu.ithaca.dragon.par.io.springio;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
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
    public StudentModel getStudentModel(String userId) {
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
    public void addQuestions(List<Question> questions) throws IOException {

    }

    @Override
    public void submitImageTaskResponse(String userId, ImageTaskResponse imageTaskResponse) throws IOException {

    }

    @Override
    public int getMinQuestionCountPerType() {
        return 0;
    }

    @Override
    public void logout(String userId) throws IOException {

    }


}

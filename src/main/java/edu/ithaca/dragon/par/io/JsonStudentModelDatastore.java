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

public class JsonStudentModelDatastore extends JsonQuestionPoolDatastore implements StudentModelDatastore {

    private String studentModelFilePath;

    public JsonStudentModelDatastore(String questionFilePath, String studentModelFilePath) throws IOException {
        super(questionFilePath);
        this.studentModelFilePath = studentModelFilePath;
    }

    @Override
    public StudentModel getStudentModel(String userId) throws IOException{
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
    public void saveStudentModel(StudentModel studentModel) throws IOException{
        if(studentModelFilePath == null)
            throw new IOException("studentModelFilePath is null");
        String fullFilePath = studentModelFilePath + "/" +  studentModel.getUserId() + ".json";
        JsonUtil.toJsonFile(fullFilePath, new StudentModelRecord(studentModel));
    }

    @Override
    public void addQuestions(List<Question> questions) throws IOException{
        List<String> studentIds = loadAllStudents();

    }

    public List<String> loadAllStudents() throws IOException{
        List<String> files = FileSystemUtil.findAllFileNamesInDir(studentModelFilePath, "json");
        return null;
    }
}

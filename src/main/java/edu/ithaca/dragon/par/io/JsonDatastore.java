package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonDatastore implements Datastore{

    private String questionFilePath;
    private String studentModelFilePath;
    private QuestionPool questionPool;

    //Constructor to be used only for loading Questions
    public JsonDatastore(String questionFilePath) throws IOException {
        this(questionFilePath, null);
    }

    public JsonDatastore(String questionFilePath, String studentModelFilePath) throws IOException {
        this.questionFilePath = questionFilePath;
        this.questionPool = new QuestionPool(this);
        this.studentModelFilePath = studentModelFilePath;
    }

    @Override
    public java.util.List<Question> loadQuestions() throws IOException {
        return JsonUtil.listFromJsonFile(questionFilePath, Question.class);
    }

    @Override
    public List<StudentModel> loadStudentModels() throws IOException {
        //TODO: This currently loads in every file in the studentModelFilePath package. Change it so it takes a userID and only load that studentModel

        //check if filepath is exists
        if(studentModelFilePath == null)
            throw new IOException("studentModelFilePath is null");

        //make a list of studentModelRecords from every file in the filepath
        List<StudentModelRecord> studentModelRecords = new ArrayList<>();
        File studentModelFolder = new File(studentModelFilePath);
        File[] listOfStudentModelFiles = studentModelFolder.listFiles();

        for(File file : listOfStudentModelFiles){
            if(file.isFile()){
                String fullFileName = studentModelFilePath + "/" + file.getName();
                StudentModelRecord newSMR = JsonUtil.fromJsonFile(fullFileName, StudentModelRecord.class);
                studentModelRecords.add(newSMR);
            }
        }

        //create studentModels from the studentModelRecords
        List<StudentModel> studentModels = new ArrayList<>();
        for(StudentModelRecord currSMR : studentModelRecords){
            studentModels.add(currSMR.buildStudentModel(questionPool));
        }
        return studentModels;
    }

    @Override
    public void saveStudentModels(Collection<StudentModel> studentModelsIn) throws IOException {
        if(studentModelFilePath == null)
            throw new IOException("studentModelFilePath is null");
        List<StudentModelRecord> studentModelRecords = new ArrayList<>();
        for(StudentModel currStudentModel : studentModelsIn){
            studentModelRecords.add(new StudentModelRecord(currStudentModel));
        }
        JsonUtil.toJsonFile(studentModelFilePath, studentModelRecords);
    }

}

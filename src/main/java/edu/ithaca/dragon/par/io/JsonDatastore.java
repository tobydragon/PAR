package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.FileSystemUtil;
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
    private String authorModelFilePath;
    private QuestionPool questionPoolTemplate;
    private String questionTemplateFilePath;

    //Constructor to be used only for loading Questions
    public JsonDatastore(String questionFilePath) throws IOException {
        this(questionFilePath, null, null, null);
    }

    public JsonDatastore(String questionFilePath, String studentModelFilePath) throws IOException {
        this(questionFilePath, studentModelFilePath, null, null);
    }

    public JsonDatastore(String questionFilePath, String questionTemplateFilePath, String authorModelFilePath) throws IOException {
        this(questionFilePath, null, questionTemplateFilePath, authorModelFilePath);

    }

    public JsonDatastore(String questionFilePath, String studentModelFilePath, String questionTemplateFilePath, String authorModelFilePath) throws IOException {
        this.questionFilePath = questionFilePath;
        if(questionFilePath != null){
            this.questionPool = new QuestionPool(this.loadQuestionsStatic(questionFilePath));
        }
        this.studentModelFilePath = studentModelFilePath;
        this.questionTemplateFilePath = questionTemplateFilePath;
        if(questionTemplateFilePath != null){
            this.questionPoolTemplate = new QuestionPool(this.loadQuestionsStatic(questionTemplateFilePath));
        }
        this.authorModelFilePath = authorModelFilePath;
    }

    @Override
    public List<Question> loadQuestions(){
        return questionPool.getAllQuestions();
    }

    public List<Question> getQuestionTemplates(){
        return questionPoolTemplate.getAllQuestions();
    }

    public static List<Question> loadQuestionsStatic(String filePath) throws IOException {
        return JsonUtil.listFromJsonFile(filePath, Question.class);
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

    @Override
    public AuthorModel loadAuthorModel() throws IOException {
        return null;
    }

    @Override
    public void saveAuthorModel(AuthorModel authorModel) throws IOException {

    }
}

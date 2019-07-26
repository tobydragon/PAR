package edu.ithaca.dragon.par.io.springio;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.AuthorDatastore;
import edu.ithaca.dragon.par.io.AuthorModelRecord;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.util.JsonSpringUtil;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;


//TODO: delete
public class JsonSpringAuthorDatastore implements AuthorDatastore {

    private JsonSpringQuestionPoolDatastore questionsDatastore;
    private JsonSpringQuestionPoolDatastore questionTemplatesDatastore;
    private String authorFilepath;
    private AuthorModel authorModel;

    public JsonSpringAuthorDatastore(String questionsFilePath, String defaultQuestionsFilePath,
                                     String questionTemplatesFilepath, String defaultQuestionTemplatesFilePath,
                                     String authorFilepath) throws IOException{
        this.questionsDatastore = new JsonSpringQuestionPoolDatastore(questionsFilePath, defaultQuestionsFilePath);
        this.questionTemplatesDatastore = new JsonSpringQuestionPoolDatastore(questionTemplatesFilepath, defaultQuestionTemplatesFilePath);
        this.authorFilepath = authorFilepath;
        setUpAuthorModelProperties(authorFilepath, questionTemplatesDatastore.getAllQuestions());
    }

    private void setUpAuthorModelProperties(String filepath, List<Question> questionTemplates) throws IOException{
        this.authorFilepath = filepath;
        try{
            authorModel = JsonSpringUtil.fromFileSystemJson(filepath, AuthorModelRecord.class).buildAuthorModel(questionTemplatesDatastore.getQuestionPool());
        } catch (IOException e){
            authorModel = new AuthorModel("author", QuestionCount.questionToQuestionCount(questionTemplates));
            overwriteAuthorFile();
        }
    }

    public Question findTopLevelQuestionTemplateById(String questionId){
        return questionTemplatesDatastore.findTopLevelQuestionTemplateById(questionId);
    }

    //used only for testing currently
    public Question findTopLevelQuestionById(String questionId){
        return questionsDatastore.findTopLevelQuestionTemplateById(questionId);
    }

    public void addQuestion(Question question) throws IOException{
        questionsDatastore.addQuestion(question);
    }

    private void overwriteAuthorFile() throws IOException{
        JsonSpringUtil.toFileSystemJson(authorFilepath, new AuthorModelRecord(authorModel));
    }

    public void removeQuestionTemplateById(String templateId) throws IOException{
        questionTemplatesDatastore.removeQuestionById(templateId);
        authorModel.removeQuestion(templateId);
        overwriteAuthorFile();
    }

    public AuthorModel getAuthorModel(){
        return authorModel;
    }

    public int getQuestionCount(){
        return questionsDatastore.getQuestionCount();
    }

    public int getQuestionTemplateCount(){
        return questionTemplatesDatastore.getQuestionCount();
    }

    @Override
    public List<Question> getAllAuthoredQuestions() {
        //TODO
        return null;
    }

    @Override
    public List<Question> removeAllAuthoredQuestions() throws IOException {
        //TODO
        return null;
    }
}

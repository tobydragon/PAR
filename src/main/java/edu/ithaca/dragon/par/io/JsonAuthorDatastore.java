package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonAuthorDatastore implements AuthorDatastore {

    private JsonQuestionPoolDatastore questionsDatastore;
    private JsonQuestionPoolDatastore questionTemplatesDatastore;
    private String authorFilepath;
    private AuthorModel authorModel;
    private JsonIoUtil jsonIoUtil;

    public JsonAuthorDatastore(String questionsFilePath, String questionTemplatesFilepath, String authorFilepath) throws IOException {
        this(questionsFilePath, questionTemplatesFilepath, authorFilepath, new JsonIoHelperDefault());
    }

    public JsonAuthorDatastore(String questionsFilePath, String questionTemplatesFilepath, String authorFilepath, JsonIoHelper jsonIoHelper) throws IOException {
        this(questionsFilePath, null, questionTemplatesFilepath, null, authorFilepath, jsonIoHelper);
    }

    public JsonAuthorDatastore(String questionsFilePath, String defaultQuestionsReadOnlyFilePath,
                               String questionTemplatesFilepath, String defaultQuestionTemplatesReadOnlyFilePath,
                               String authorFilepath, JsonIoHelper jsonIoHelper) throws IOException{
        this.jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        this.questionsDatastore = new JsonQuestionPoolDatastore(questionsFilePath,defaultQuestionsReadOnlyFilePath, jsonIoHelper);
        this.questionTemplatesDatastore = new JsonQuestionPoolDatastore(questionTemplatesFilepath, defaultQuestionTemplatesReadOnlyFilePath, jsonIoHelper);
        this.authorFilepath = authorFilepath;
        setUpAuthorModelProperties(authorFilepath, questionTemplatesDatastore.getAllQuestions());
    }

    private void setUpAuthorModelProperties(String filepath, List<Question> questionTemplates) throws IOException{
        this.authorFilepath = filepath;
        //check if file exists, return null if it doesn't
        File checkFile = new File(filepath);
        if(!checkFile.exists()){
            authorModel = new AuthorModel("author", QuestionCount.questionToQuestionCount(questionTemplates));
            overwriteAuthorFile();
        }
        else {
            authorModel = (jsonIoUtil.fromFile(filepath, AuthorModelRecord.class).buildAuthorModel(questionTemplatesDatastore.getQuestionPool()));
        }
    }

    @Override
    public Question findTopLevelQuestionTemplateById(String questionId){
        return questionTemplatesDatastore.findTopLevelQuestionTemplateById(questionId);
    }

    //used only for testing currently
    @Override
    public Question findTopLevelQuestionById(String questionId){
        return questionsDatastore.findTopLevelQuestionTemplateById(questionId);
    }

    @Override
    public void addQuestion(Question question) throws IOException{
        questionsDatastore.addQuestion(question);
    }

    private void overwriteAuthorFile() throws IOException{
       jsonIoUtil.toFile(authorFilepath, new AuthorModelRecord(authorModel));
    }

    @Override
    public void removeQuestionTemplateById(String templateId) throws IOException{
        questionTemplatesDatastore.removeQuestionById(templateId);
        authorModel.removeQuestion(templateId);
        overwriteAuthorFile();
    }

    @Override
    public AuthorModel getAuthorModel(){
        return authorModel;
    }

    @Override
    public int getQuestionCount(){
        return questionsDatastore.getQuestionCount();
    }

    @Override
    public int getQuestionTemplateCount(){
        return questionTemplatesDatastore.getQuestionCount();
    }

    @Override
    public List<Question> getAllAuthoredQuestions(){
        return questionsDatastore.getAllQuestions();
    }

    //removes and returns all questions that are authored (leaving a blank question file for authored questions)
    //no effect on the templates
    @Override
    public List<Question> removeAllAuthoredQuestions() throws IOException {
        return questionsDatastore.removeAllQuestions();
    }
}

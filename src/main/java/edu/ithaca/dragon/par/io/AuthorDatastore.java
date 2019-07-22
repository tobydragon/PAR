package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AuthorDatastore {

    private JsonQuestionPoolDatastore questionsDatastore;
    private JsonQuestionPoolDatastore questionTemplatesDatastore;
    private String authorFilepath;
    private AuthorModel authorModel;

    public AuthorDatastore(String questionsFilePath, String questionTemplatesFilepath, String authorFilepath) throws IOException{
        this.questionsDatastore = new JsonQuestionPoolDatastore(questionsFilePath);
        this.questionTemplatesDatastore = new JsonQuestionPoolDatastore(questionTemplatesFilepath);
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
            authorModel = JsonUtil.fromJsonFile(filepath, AuthorModel.class);
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
        JsonUtil.toJsonFile(authorFilepath, authorModel);
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
}

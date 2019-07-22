package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AuthorDatastore {

    private JsonQuestionPoolDatastore questionsDatastore;
    private JsonQuestionPoolDatastore questionTemplatesDatastore;
    private String authorFilepath;

    public AuthorDatastore(String questionsFilePath, String questionTemplatesFilepath, String authorFilepath) throws IOException{
        this.questionsDatastore = new JsonQuestionPoolDatastore(questionsFilePath);
        this.questionTemplatesDatastore = new JsonQuestionPoolDatastore(questionTemplatesFilepath);
        this.authorFilepath = authorFilepath;
    }

    public List<Question> getAllQuestions(){
        return questionsDatastore.getAllQuestions();
    }

    public List<Question> getAllQuestionTemplates(){
        return questionTemplatesDatastore.getAllQuestions();
    }

    public AuthorModel getAuthorModel() throws IOException{
        File checkFile = new File(authorFilepath);
        if(!checkFile.exists()){
            return null;
        }
        else {
            return JsonUtil.fromJsonFile(authorFilepath, AuthorModel.class);
        }
    }

    public void replaceAll(List<Question> questions, List<Question> questionTemplates, AuthorModel authorModel) throws IOException{
        replaceAllQuestions(questions);
        replaceAllQuestionTemplatesAndAuthorModel(questionTemplates, authorModel);
    }

    public void replaceAllQuestions(List<Question> questions) throws IOException{
        questionsDatastore.replaceQuestions(questions);
    }

    //author model is dependent on questionTemplates, should be replaced with updated author model when templates are changed
    public void replaceAllQuestionTemplatesAndAuthorModel(List<Question> questions, AuthorModel authorModel) throws IOException{
        questionTemplatesDatastore.replaceQuestions(questions);
    }


}

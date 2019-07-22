package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.util.List;

public class AuthorDatastore {

    private JsonQuestionPoolDatastore questionsDatastore;
    private JsonQuestionPoolDatastore questionTemplatesDatastore;
    private String authorFilepath;

    public List<Question> getAllQuestions(){
        return questionsDatastore.getAllQuestions();
    }

    public void replaceAllQuestions(List<Question> questions) throws IOException{
        questionsDatastore.replaceQuestions(questions);
    }

    public List<Question> getAllQuestionTemplates(){
        return questionTemplatesDatastore.getAllQuestions();
    }

    public void saveAllQuestionTemplates(List<Question> questions) throws IOException{
        questionTemplatesDatastore.replaceQuestions(questions);
    }

    public AuthorModel getAuthorModel() throws IOException{
        return JsonUtil.fromJsonFile(authorFilepath, AuthorModel.class);
    }

    public void replaceAuthorModel(AuthorModel authorModel) throws IOException{
        JsonUtil.toJsonFile(authorFilepath, authorModel);
    }
}

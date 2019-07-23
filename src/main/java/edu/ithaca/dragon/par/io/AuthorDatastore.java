package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;

import java.io.IOException;

public interface AuthorDatastore {
    Question findTopLevelQuestionTemplateById(String questionId);

    //used only for testing currently
    Question findTopLevelQuestionById(String questionId);

    void addQuestion(Question question) throws IOException;

    void removeQuestionTemplateById(String templateId) throws IOException;

    AuthorModel getAuthorModel();

    int getQuestionCount();

    int getQuestionTemplateCount();
}

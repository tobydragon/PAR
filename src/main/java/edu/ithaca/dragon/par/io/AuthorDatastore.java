package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;

import java.io.IOException;
import java.util.List;

public interface AuthorDatastore {
    Question findTopLevelQuestionTemplateById(String questionId);

    //used only for testing currently
    Question findTopLevelQuestionById(String questionId);

    void addQuestion(Question question) throws IOException;

    void removeQuestionTemplateById(String templateId) throws IOException;

    AuthorModel getAuthorModel();

    int getQuestionCount();

    int getQuestionTemplateCount();

    public List<Question> getAllAuthoredQuestions();

    //removes and returns all questions that are authored (leaving a blank question file for authored questions)
    //no effect on the templates
    public List<Question> removeAllAuthoredQuestions() throws IOException;
}

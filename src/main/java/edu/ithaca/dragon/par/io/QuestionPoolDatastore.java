package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;

import java.io.IOException;
import java.util.List;

public interface QuestionPoolDatastore {

    void addQuestion(Question newQuestion) throws IOException;

    void removeQuestionById(String questionId) throws IOException;
    List<Question> removeAllQuestions() throws IOException;

    List<Question> getAllQuestions();
}

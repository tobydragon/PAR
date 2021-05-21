package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.domain.Question;

import java.io.IOException;
import java.util.List;

public interface QuestionPoolDatastore {

    void addQuestion(Question newQuestion) throws IOException;

    void removeQuestionById(String questionId) throws IOException;
    List<Question> removeAllQuestions() throws IOException;

    List<Question> getAllQuestions();
}

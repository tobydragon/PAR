package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.List;

public interface ImageTaskReponse {

    List<String> getTaskQuestionIds();
    List<String> getResponseTexts();

    String getUserId();
    void setUserId(String userId);

    String findResponseToQuestion(Question question);
}

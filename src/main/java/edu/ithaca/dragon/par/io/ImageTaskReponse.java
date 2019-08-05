package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;

import java.util.List;

public interface ImageTaskReponse {

    List<String> getTaskQuestionIds();
    void setTaskQuestionIds(List<String> taskQuestionIds);


    List<String> getResponseTexts();
    void setResponseTexts(List<String> responseTexts);


    String getUserId();
    void setUserId(String userId);

    String findResponseToQuestion(Question question);
}

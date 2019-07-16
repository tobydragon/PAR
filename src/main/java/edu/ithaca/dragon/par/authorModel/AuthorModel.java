package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.QuestionCount;

import java.util.List;

public class AuthorModel {
    String authorId;
    List<QuestionCount> questionCountList;

    public AuthorModel(String authorId, List<Question> questionsIn){

    }

    public void removeQuestion(String questionId){

    }

    public String getAuthorId() {
        return authorId;
    }

    public List<QuestionCount> getQuestionCountList() {
        return questionCountList;
    }
}

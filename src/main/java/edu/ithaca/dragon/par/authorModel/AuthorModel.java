package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.QuestionCount;

import java.security.InvalidParameterException;
import java.util.List;

public class AuthorModel {
    String authorId;
    List<QuestionCount> questionCountList;

    public AuthorModel(String authorId, List<Question> questionsIn){
        this.authorId = authorId;
        this.questionCountList = QuestionCount.questionToQuestionCount(questionsIn);
    }

    /**
     * Removes a question from the list of questionCounts, and returns the question object
     */
    public Question removeQuestion(String questionId){
        for(QuestionCount questionCount : questionCountList){
            if(questionCount.getQuestion().getId().equals(questionId)){
                Question q = questionCount.getQuestion();
                questionCountList.remove(questionCount);
                return q;
            }
        }

        throw new InvalidParameterException("Question with id:" + questionId + " does not exist in authorModel:" + authorId);
    }

    public String getAuthorId() {
        return authorId;
    }

    public List<QuestionCount> getQuestionCountList() {
        return questionCountList;
    }

    public void increaseTimesSeen(String questionId){

    }
}

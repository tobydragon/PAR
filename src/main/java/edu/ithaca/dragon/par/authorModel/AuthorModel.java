package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.deprecated.studentModel.QuestionCount;

import java.security.InvalidParameterException;
import java.util.List;

public class AuthorModel {
    String authorId;
    List<QuestionCount> questionCountList;

    public AuthorModel(String authorId, List<QuestionCount> questionCountsIn){
        this.authorId = authorId;
        this.questionCountList = questionCountsIn;
    }

    /**
     * Removes a question from the list of questionCounts, and returns the question object
     */
    public Question removeQuestion(String questionId){
        QuestionCount toRemove = null;
        for(QuestionCount questionCount : questionCountList){
            if(questionCount.getQuestion().getId().equals(questionId)){
                toRemove = questionCount;
            }
        }
        if (toRemove != null) {
            Question q = toRemove.getQuestion();
            questionCountList.remove(toRemove);
            return q;
        }
        else {
            throw new InvalidParameterException("Question with id:" + questionId + " does not exist in authorModel:" + authorId);
        }
    }

    public String getAuthorId() {
        return authorId;
    }

    public List<QuestionCount> getQuestionCountList() {
        return questionCountList;
    }

    public boolean increaseTimesAttempted(String questionId){
        boolean found = false;
        for(QuestionCount questionCount : questionCountList){
            if(questionCount.getQuestion().getId().equals(questionId)){
                questionCount.increaseTimesAttempted();
                found = true;
                break;
            }
            if(questionCount.getFollowupCounts().size()>0){ //attachment
                for(QuestionCount qc : questionCount.getFollowupCounts()){
                    if (qc.getQuestion().getId().equals(questionId)){
                        qc.increaseTimesAttempted();
                        found = true;
                        break;
                    }
                }
            }
        }
        if(!found){
            throw new InvalidParameterException("Question with id:" + questionId + " does not exist in authorModel:" + authorId);
        }
        return found;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null)
            return false;
        if(otherObj.getClass() != AuthorModel.class){
            return false;
        }
        AuthorModel other = (AuthorModel)otherObj;
        return authorId.equals(other.authorId) &&
                questionCountList.equals(other.questionCountList);
    }
}

package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParAuthoringServer {

    private AuthorDatastore authorDatastore;

    public ParAuthoringServer(AuthorDatastore authorDatastore) {
        this.authorDatastore = authorDatastore;
    }

    public ImageTask nextImageTaskTemplate() {
        return AuthorTaskGenerator.makeTaskTemplate(authorDatastore.getAuthorModel());
    }

    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse) throws IOException{
        for(String currId : imageTaskResponse.getTaskQuestionIds()){
            Question currQuestion = authorDatastore.findTopLevelQuestionTemplateById(currId);
            if(currQuestion != null){
                Question newQuestion = buildQuestionFromTemplate(currQuestion, imageTaskResponse);
                authorDatastore.addQuestion(newQuestion);
                authorDatastore.removeQuestionTemplateById(currQuestion.getId());
            }
        }
    }

    public List<ImageTask> authoredQuestions(QuestionPool questionPool) {
        return AuthorTaskGenerator.authoredQuestions(questionPool);
    }

    public static Question buildQuestionFromTemplate(Question questionIn, ImageTaskResponse imageTaskResponse){
        String answer = imageTaskResponse.findResponseToQuestion(questionIn);
        if(answer == null){
            return null;
        }
        List<Question> followupQuestions = new ArrayList<>();
        for(Question currFollowup : questionIn.getFollowupQuestions()){
            Question newFollowUp = buildQuestionFromTemplate(currFollowup, imageTaskResponse);
            if (newFollowUp != null) {
                followupQuestions.add(newFollowUp);
            }
        }
        return new Question(questionIn, answer, followupQuestions);
    }

    //removes and returns all questions that are authored (leaving a blank question file for authored questions)
    //no effect on the templates
    public List<Question> removeAllAuthoredQuestions() throws IOException{
        return authorDatastore.removeAllAuthoredQuestions();
    }


    //TODO: these are only used for testing, can they be replaced somehow?
    public int getQuestionCount(){
        return authorDatastore.getQuestionCount();
    }

    public int getQuestionTemplateCount(){
        return authorDatastore.getQuestionTemplateCount();
    }

    public Question findTopLevelQuestionById(String questionId){
        return authorDatastore.findTopLevelQuestionById(questionId);
    }
}

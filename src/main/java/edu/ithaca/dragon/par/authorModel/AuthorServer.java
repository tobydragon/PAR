package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorServer {

    private AuthorDatastore authorDatastore;

    public AuthorServer(AuthorDatastore authorDatastore) {
        this.authorDatastore = authorDatastore;
    }

    public ImageTask nextImageTaskTemplate() {
        AuthorModel am = authorDatastore.getAuthorModel();
        ImageTask it = AuthorTaskGenerator.makeTaskTemplate(am);
        for (int i = 0; i < it.getTaskQuestions().size(); i++){
            am.increaseTimesSeen(it.getTaskQuestions().get(i).getId());
        }
        return it;
    }

    public void imageTaskResponseSubmitted(ImageTaskResponseOOP imageTaskResponse) throws IOException{
        for(String currId : imageTaskResponse.getTaskQuestionIds()){
            Question currQuestion = authorDatastore.findTopLevelQuestionTemplateById(currId);
            if(currQuestion != null){
                Question newQuestion = buildQuestionFromTemplate(currQuestion, imageTaskResponse);
                authorDatastore.addQuestionToQuestionsDatastore(newQuestion);
                authorDatastore.removeQuestionTemplateById(currQuestion.getId());
            }
        }
    }

    public List<ImageTask> authoredQuestions() {
        return AuthorTaskGenerator.authoredQuestions(authorDatastore.getAllAuthoredQuestions());
    }

    public static Question buildQuestionFromTemplate(Question questionIn, ImageTaskResponseOOP imageTaskResponse){
        String questionText = imageTaskResponse.findQuestionTextOfQuestion(questionIn);
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
        if (questionText == null)
            return new Question(questionIn, answer.toLowerCase(), followupQuestions);
        else //it is a custom question
            return new Question(questionIn, questionText, answer.toLowerCase(), followupQuestions);
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

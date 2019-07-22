package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.*;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParAuthoringServer {


    private AuthorModel authorModel;
    //The QP that is initially empty, and gets filled with answered Questions
    private QuestionPool questionPool;
    //The QP full of template Questions (null answers)
    private QuestionPool questionPoolTemplate;

    //There are multiple QuestionPool objects, in this class and in the datastore, probably should be fixed
    private AuthorDatastore authorDatastore;


    public ParAuthoringServer(AuthorDatastore authorDatastore) throws IOException {
        this.authorDatastore = authorDatastore;
        this.questionPool = new QuestionPool(authorDatastore.getAllQuestions());
        this.questionPoolTemplate = new QuestionPool(authorDatastore.getAllQuestionTemplates());
        this.authorModel = getOrCreateAuthorModel(authorDatastore);
    }

    public QuestionPool getQuestionPool() {
        return questionPool;
    }

    public QuestionPool getQuestionPoolTemplate() {
        return questionPoolTemplate;
    }

    public static AuthorModel getOrCreateAuthorModel(AuthorDatastore authorDatastore) throws IOException{
        AuthorModel authorModel = authorDatastore.getAuthorModel();
        //if the student didn't have a file, create a new student
        if(authorModel == null){
            authorModel = new AuthorModel("author", QuestionCount.questionToQuestionCount(authorDatastore.getAllQuestionTemplates()));
        }
        return authorModel;
    }

    public ImageTask nextImageTaskTemplate() {
        return AuthorTaskGenerator.makeTaskTemplate(authorModel);
    }

    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse) throws IOException{
        for(String currId : imageTaskResponse.getTaskQuestionIds()){
            Question currQuestion = questionPoolTemplate.getTopLevelQuestionById(currId);
            if(currQuestion != null){
                Question newQuestion = buildQuestionFromTemplate(currQuestion, imageTaskResponse);
                questionPool.addQuestion(newQuestion);
                questionPoolTemplate.removeQuestionById(currQuestion.getId());
                authorModel.removeQuestion(currId);
                authorDatastore.replaceAll(questionPool.getAllQuestions(), questionPoolTemplate.getAllQuestions(), authorModel);
            }
        }
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
}

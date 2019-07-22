package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParAuthoringServer {

    //The QP full of unanswered Questions
    private QuestionPool questionPoolTemplate;

    //The QP that is initially empty, and gets filled with answered Questions
    private QuestionPool questionPool;

    private Datastore datastore;
    private Datastore datastoreForTemplate;
    private AuthorModel authorModel;

    public ParAuthoringServer(Datastore datastore, Datastore datastoreForTemplate) throws IOException {
        this.datastore = datastore;
        this.questionPool = new QuestionPool(datastore.loadQuestions());
        this.datastoreForTemplate = datastoreForTemplate;
        this.questionPoolTemplate = new QuestionPool(datastoreForTemplate.loadQuestions());

        authorModel = getOrCreateAuthorModel(questionPoolTemplate.getAllQuestions());
    }

    public QuestionPool getQuestionPool() {
        return questionPool;
    }

    public QuestionPool getQuestionPoolTemplate() {
        return questionPoolTemplate;
    }

    public AuthorModel getOrCreateAuthorModel(List<Question> questionTemplates) throws IOException{
        AuthorModel authorModel = datastore.loadAuthorModel();
        //if the student didn't have a file, create a new student
        if(authorModel == null){
            authorModel = new AuthorModel("author", QuestionCount.questionToQuestionCount(questionTemplates));
        }
        return authorModel;
    }

    /**
     * Creates a question out of a questionTemplate and its answer
     * Removes the old questionTemplate from the questionPoolTemplate
     * Adds the new question to the ParServer questionPool
     *
     * @param questionId the id of a question to convert
     */
    public void convertQuestionTemplateToQuestion(String questionId, String answer) {
        Question questionTemplate = questionPoolTemplate.getQuestionFromId(questionId);





        if (!checkIfAnswerIsValid(questionTemplate, answer)){
            throw new RuntimeException();
        }
        List<Question> followup = getValidFollowUpQuestions(questionTemplate);
        Question questionToAdd = new Question(questionTemplate.getId(), questionTemplate.getQuestionText(), questionTemplate.getType(), answer, questionTemplate.getPossibleAnswers(), questionTemplate.getImageUrl(), followup);

        // ^ move this code into a static function that


        questionPool.addQuestion(questionToAdd);
        questionPoolTemplate.removeQuestionById(questionTemplate.getId());
    }

    //TODO: Deal with case
    public static boolean checkIfAnswerIsValid(Question questionTemplate, String answer) {
        if (answer==null){
            return false;
        }
        for (int i = 0; i < questionTemplate.getPossibleAnswers().size(); i++) {
            if (questionTemplate.getPossibleAnswers().get(i).equals(answer)) {
                //found
                return true;
            }
        }
        //it was not found
        return false;
    }

    /**
     * @return A list of pointers to followup Questions which need to be converted
     */
    public static List<Question>  getValidFollowUpQuestions(Question questionTemplate){
        List<Question> validFollowUps = new ArrayList<>();
        for (int i = 0; i <questionTemplate.getFollowupQuestions().size(); i++){
            boolean answerValid = checkIfAnswerIsValid(questionTemplate.getFollowupQuestions().get(i), questionTemplate.getFollowupQuestions().get(i).getCorrectAnswer());
            if (answerValid){
                validFollowUps.add(questionTemplate.getFollowupQuestions().get(i));
            }
        }
        return validFollowUps;
    }

    public ImageTask nextImageTaskTemplate() {
        return AuthorTaskGenerator.makeTaskTemplate(authorModel);
    }

    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse) throws IOException{

    }
}

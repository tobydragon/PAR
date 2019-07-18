package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParAuthoringServer extends ParServer {

    private QuestionPool questionPoolTemplate;
    private Map<String, AuthorModel> authorModelMap;
    private Datastore datastoreForTemplate;

    public QuestionPool getQuestionPoolTemplate() {
        return questionPoolTemplate;
    }

    public ParAuthoringServer(Datastore datastore, Datastore datastoreForTemplate) throws IOException {
        this(datastore, datastoreForTemplate, null);
    }

    public ParAuthoringServer(Datastore datastore, Datastore datastoreForTemplate, Integer windowSizeOverride) throws IOException {
        super(datastore, windowSizeOverride);
        this.datastoreForTemplate = datastoreForTemplate;
        this.questionPoolTemplate = new QuestionPool(datastoreForTemplate);
        authorModelMap = new HashMap<>();
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

        //question not found
        if (questionTemplate == null){
            throw new RuntimeException("A question with Id:" + questionId + " does not exist or has already been answered");
        }

        else{
            //checks if answer is in the possibleAnswers list
            boolean answerValid = checkIfAnswerIsValid(questionTemplate, answer);
            if (!answerValid){
                throw new RuntimeException();
            }

            else{
                List<Question> followup = getValidFollowUpQuestions(questionTemplate);

            }

        }


    }

    //TODO: Deal with case
    public static boolean checkIfAnswerIsValid(Question questionTemplate, String answer) {
        if (answer==null){
            return false;
        }
        boolean answerFound = false;
        for (int i = 0; i < questionTemplate.getPossibleAnswers().size(); i++) {
            if (questionTemplate.getPossibleAnswers().get(i).equals(answer)) {
                //found
                return true;
            }
        }
        //it was not found
        return false;
    }

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



    public static AuthorModel getOrCreateAuthorModel() {
    return null;
    }


    public ImageTask nextImageTaskTemplate(String authorId) throws IOException {
        return null;
    }
}

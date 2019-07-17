package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParAuthoringServer extends ParServer {

    private QuestionPool questionPoolTemplate;
    private Map<String, StudentModel> authorModelMap;
    private Datastore datastoreForTemplate;

    public ParAuthoringServer(Datastore datastore, Datastore datastoreForTemplate) throws IOException {
        super(datastore);
        this.datastoreForTemplate = datastoreForTemplate;
        this.questionPoolTemplate = new QuestionPool(datastoreForTemplate);
        authorModelMap = new HashMap<>();
    }

    /**
     * Creates a question out of a questionTemplate and its answer
     * Removes the old questionTemplate from the questionPoolTemplate
     * Adds the new question to the ParServer questionPool
     * @param questionId the id of a question to convert
     */
    public void convertQuestionTemplateToQuestion(String questionId, String answer){

    }

}

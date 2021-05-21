package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonQuestionPoolDatastore implements QuestionPoolDatastore {
    private Logger logger = LogManager.getLogger(this.getClass());


    private String questionFilePath;
    protected QuestionPool questionPool;
    private JsonIoUtil jsonIoUtil;

    public JsonQuestionPoolDatastore(String questionFilePath) throws IOException {
        this(questionFilePath, new JsonIoHelperDefault());
    }

    public JsonQuestionPoolDatastore(String questionFilePath, JsonIoHelper jsonIoHelper) throws IOException {
        this(questionFilePath, null, jsonIoHelper);
    }

    public JsonQuestionPoolDatastore(String questionFilePath, String defaultQuestionReadOnlyFilePath, JsonIoHelper jsonIoHelper) throws IOException {
        this.jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        this.questionFilePath = questionFilePath;
        if (defaultQuestionReadOnlyFilePath != null){
            this.questionPool = new QuestionPool(jsonIoUtil.listFromFileOrCopyFromReadOnlyFile(questionFilePath, defaultQuestionReadOnlyFilePath, Question.class));
        }
        else {
            this.questionPool = new QuestionPool(jsonIoUtil.listFromFile(questionFilePath, Question.class));
        }
    }

    public void addQuestion(Question newQuestion) throws IOException {
        questionPool.addQuestion(newQuestion);
        overwriteQuestionPoolFile();
    }

    public void removeQuestionById(String questionId) throws IOException {
        questionPool.removeQuestionById(questionId);
        overwriteQuestionPoolFile();
    }

    public List<Question> removeAllQuestions() throws IOException{
        List<Question> allQuestions = getAllQuestions();
        this.questionPool = new QuestionPool(new ArrayList<>());
        overwriteQuestionPoolFile();
        return allQuestions;
    }

    protected void addQuestions(List<Question> questions) throws IOException{
        for (Question question : questions){
            //TODO: figure out better how to handle questions with same ID
            if (!questionPool.isIdTaken(question.getId())) {
                questionPool.addQuestion(question);
            }
            else {
                logger.error("Ignoring Question "+question.getId()+", another with same ID already exists in the JsonQuestionPoolDatastore.");
            }
        }
        overwriteQuestionPoolFile();
    }

    private void overwriteQuestionPoolFile() throws IOException{
        jsonIoUtil.toFile(questionFilePath, questionPool.getAllQuestions());
    }

    //---------  accessors --------------//

    public List<Question> getAllQuestions(){
        return questionPool.getAllQuestions();
    }

    public QuestionPool getQuestionPool(){
        return questionPool;
    }

    public Question findTopLevelQuestionTemplateById(String questionId){
        return questionPool.getTopLevelQuestionById(questionId);
    }

    public int getQuestionCount(){
        return questionPool.getAllQuestions().size();
    }


}

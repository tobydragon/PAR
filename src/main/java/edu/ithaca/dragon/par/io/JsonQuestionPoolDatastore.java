package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonQuestionPoolDatastore {
    private String questionFilePath;
    protected QuestionPool questionPool;

    public JsonQuestionPoolDatastore(String questionFilePath) throws IOException {
        this.questionFilePath = questionFilePath;
        this.questionPool = new QuestionPool(JsonUtil.listFromJsonFile(questionFilePath, Question.class));
    }

    public List<Question> getAllQuestions(){
        return questionPool.getAllQuestions();
    }

    public QuestionPool getQuestionPool(){
        return questionPool;
    }

    public Question findTopLevelQuestionTemplateById(String questionId){
        return questionPool.getTopLevelQuestionById(questionId);
    }

    public void addQuestion(Question newQuestion) throws IOException {
        questionPool.addQuestion(newQuestion);
        overwriteQuestionPoolFile();
    }

    public void removeQuestionById(String questionId) throws IOException {
        questionPool.removeQuestionById(questionId);
        overwriteQuestionPoolFile();
    }

    public int getQuestionCount(){
        return questionPool.getAllQuestions().size();
    }

    public List<Question> removeAllQuestions() throws IOException{
        List<Question> allQuestions = getAllQuestions();
        this.questionPool = new QuestionPool(new ArrayList<>());
        overwriteQuestionPoolFile();
        return allQuestions;
    }

    private void overwriteQuestionPoolFile() throws IOException{
        JsonUtil.toJsonFile(questionFilePath, questionPool.getAllQuestions());
    }

    protected void addQuestions(List<Question> questions) throws IOException{
        for (Question question : questions){
            questionPool.addQuestion(question);
        }
        overwriteQuestionPoolFile();
    }
}

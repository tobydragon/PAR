package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
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

    public void replaceQuestions(List<Question> questions) throws IOException{
        questionPool = new QuestionPool(questions);
        JsonUtil.toJsonFile(questionFilePath, questions);
    }

    public QuestionPool getQuestionPool(){
        return questionPool;
    }

    public Question findTopLevelQuestionTemplateById(String questionId){
        return questionPool.getTopLevelQuestionById(questionId);
    }

    public void addQuestion(Question newQuestion) throws IOException {
        questionPool.addQuestion(newQuestion);
        JsonUtil.toJsonFile(questionFilePath, questionPool.getAllQuestions());
    }

    public void removeQuestionById(String questionId) throws IOException {
        questionPool.removeQuestionById(questionId);
        JsonUtil.toJsonFile(questionFilePath, questionPool.getAllQuestions());
    }

    public int getQuestionCount(){
        return questionPool.getAllQuestions().size();
    }
}

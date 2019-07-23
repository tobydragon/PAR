package edu.ithaca.dragon.par.io.springio;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.util.JsonSpringUtil;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.util.List;

public class JsonSpringQuestionPoolDatastore {
    private String questionFilePath;
    protected QuestionPool questionPool;

    public JsonSpringQuestionPoolDatastore(String currentQuestionFilePath, String defaultQuestionFilePath) throws IOException {
        this.questionFilePath = currentQuestionFilePath;
        this.questionPool = new QuestionPool(JsonSpringUtil.listFromFileSystemOrCopyFromDefaultClassPathJson(currentQuestionFilePath, defaultQuestionFilePath, Question.class));
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
        JsonSpringUtil.toFileSystemJson(questionFilePath, questionPool.getAllQuestions());
    }

    public void removeQuestionById(String questionId) throws IOException {
        questionPool.removeQuestionById(questionId);
        JsonSpringUtil.toFileSystemJson(questionFilePath, questionPool.getAllQuestions());
    }

    public int getQuestionCount(){
        return questionPool.getAllQuestions().size();
    }
}

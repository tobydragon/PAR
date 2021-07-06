package edu.ithaca.dragon.par.domain;

import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class DomainDatasourceJson implements  DomainDatasource{
    private final String id;
    private final String questionFilePath;
    private List<Question> questions;

    public DomainDatasourceJson(String id, String questionFilePath) throws IOException {
        this(id, questionFilePath, null, new JsonIoHelperDefault());
    }

    public DomainDatasourceJson(String id, String questionFilePath, String defaultQuestionReadOnlyFilePath, JsonIoHelper jsonIoHelper) throws IOException {
        this.id = id;
        JsonIoUtil jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        this.questionFilePath = questionFilePath;
        if (defaultQuestionReadOnlyFilePath != null){
            questions = jsonIoUtil.listFromFileOrCopyFromReadOnlyFile(questionFilePath, defaultQuestionReadOnlyFilePath, Question.class);
        }
        else {
            questions = jsonIoUtil.listFromFile(questionFilePath, Question.class);
        }
    }

    @Override
    public List<Question> getAllQuestions() {
        return questions;
    }

    public Question getQuestion(String id){
        for (Question question: questions){
            if (question.getId().equalsIgnoreCase(id)){
                return question;
            }
        }
        throw new IllegalArgumentException("No question found, bad ID:" + id);
    }

    @Override
    public List<String> retrieveAllConcepts() {
        List<String> concepts = new ArrayList<>();

        for(Question question:questions){
            if(!concepts.contains(question.getType())){
                concepts.add(question.getType());
            }
        }
        return concepts;
    }

    @Override
    public String retrieveConceptForAQuestion(String id) {
        for (Question question: questions){
            if (question.getId().equalsIgnoreCase(id)){
                return question.getType();
            }
        }
        throw new IllegalArgumentException("No question found, bad ID:" + id);
    }

    @Override
    public List<Question> retrieveQuestionsByConcept(String concept) {
        List<Question> questionList = new ArrayList<>();
        for (Question question: questions){
            if (question.getType().equalsIgnoreCase(concept)){
                questionList.add(question);
            }
        }
        if(questionList.size()==0){
            throw new IllegalArgumentException("No questions found, bad concept: "+concept);
        }
        else{
            return questionList;
        }
    }
}

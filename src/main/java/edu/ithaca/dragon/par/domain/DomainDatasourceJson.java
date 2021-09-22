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
    //TODO: For Loop --> Recursive call
    //Base Case: If there is no follow-up, return top level
    //Recursion: Check the length of follow-up question

    public Question getQuestion(String id){
        for (Question question: questions){
            Question potentialFollowUp = getQuestionRecursive(id, question);
            if (potentialFollowUp != null){
                return potentialFollowUp;
            }
        }
        throw new IllegalArgumentException("No question found, bad ID:" + id);
    }

    public static Question getQuestionRecursive(String id, Question question){
        if (question.getId().equalsIgnoreCase(id)){
            return question;
        }
        else{
            for (Question followUp : question.getFollowupQuestions()){
                Question potentialFollowUp = getQuestionRecursive(id, followUp);
                if (potentialFollowUp != null){
                    return potentialFollowUp;
                }
            }
            return null;
        }
    }

    @Override
    public List<String> retrieveAllConcepts() {
        List<String> concepts = new ArrayList<>();

        for(Question question:questions){
            String concept = question.getType();
            if(!concepts.contains(concept)){
                concepts.add(concept);
            }
            List<Question> followUpQuestions = question.getFollowupQuestions();
            if(followUpQuestions.size()!=0){
                for(Question fq:followUpQuestions){
                    String fqConcept = fq.getType();
                    if(!concepts.contains(fqConcept)){
                        concepts.add(fqConcept);
                    }
                }
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
        
        return questionList;
        
    }
}

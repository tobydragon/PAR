package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.*;

public class LevelTaskGenerator implements TaskGenerator {

    private Map<Integer, List<String>> levelToTypesMap;

    public LevelTaskGenerator(Map<Integer, List<String>> levelToTypesMap){
        this.levelToTypesMap = levelToTypesMap;
    }

    //TODO:TEST makeTask
    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        int studentLevel=StudentModel.calcLevel(studentModel.calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis));
        List<String> levelTypes= levelToTypesMap.get(studentLevel);

        Question initialQuestion= leastSeenQuestionWithTypesNeeded(levelTypes,studentModel);
        List<Question> questionList = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getAllQuestions(), initialQuestion.getImageUrl());
        questionList = TaskGeneratorImp1.filterQuestions(studentLevel, questionList);
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList);

        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(questionList);
        return imageTask;
    }

    public static Question leastSeenQuestionWithTypesNeeded(List<String> typesNeeded, StudentModel studentModel){
        Map<String,List<QuestionCount>> questionTypesListMap = studentModel.questionCountsByTypeMap();
        List<QuestionCount> typeQuestions=questionTypesListMap.get(typesNeeded.get(0));

        QuestionCount leastSeenWithTypesNeeded = null;
        for(QuestionCount questionCount : typeQuestions){
            if(checkForAllNeededTypesOfQuestions(typesNeeded,studentModel,questionCount.getQuestion())) {
                if (leastSeenWithTypesNeeded == null  ){
                    leastSeenWithTypesNeeded = questionCount;
                }
                else if(questionCount.getTimesSeen() < leastSeenWithTypesNeeded.getTimesSeen()) {
                    leastSeenWithTypesNeeded = questionCount;
                }
            }
        }
        if(leastSeenWithTypesNeeded == null){
            throw new RuntimeException("Questions not present for the type(s)");
        }
        else {
            return leastSeenWithTypesNeeded.getQuestion();
        }
    }

    public static boolean checkForAllNeededTypesOfQuestions(List<String> types, StudentModel studentModel, Question question){
        Set<String> typesPresent=new LinkedHashSet<>();
        List<Question> questionList =QuestionPool.getQuestionsWithUrl(studentModel.getUserQuestionSet().getAllQuestions(), question.getImageUrl());
        for(Question currQuestion: questionList){
            typesPresent.add(currQuestion.getType());
        }
        for(String currType:types){
            if(!typesPresent.contains(currType)){
                return false;
            }
        }
        return true;
    }

}

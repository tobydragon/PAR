package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.*;

public class LevelTaskGeneratorAttachment implements TaskGenerator {

    private Map<Integer, List<String>> levelToTypesMap;

    public LevelTaskGeneratorAttachment(Map<Integer, List<String>> levelToTypesMap){
        this.levelToTypesMap = levelToTypesMap;
    }

    //TODO: test makeTask more thoroughly
    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        int studentLevel = calcLevel(studentModel.calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis));
        List<String> levelTypes = levelToTypesMap.get(studentLevel);
        ImageTask im = makeTaskGivenLevel(studentModel, levelTypes, studentLevel);
        return im;
    }

    public static ImageTask makeTaskGivenLevel(StudentModel studentModel, List<String> levelTypes, int level) {
        Question initialQuestion= leastSeenQuestionWithTypesNeeded(levelTypes,studentModel);
        List<Question> questionList = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getAllQuestions(), initialQuestion.getImageUrl());
        questionList = filterQuestions(level, questionList);
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList, "None");
        return imageTask;
    }

    public static Question leastSeenQuestionWithTypesNeeded(List<String> typesNeeded, StudentModel studentModel){
        Map<String,List<QuestionCount>> questionTypesListMap = studentModel.questionCountsByTypeMap();
        List<QuestionCount> typeQuestions=questionTypesListMap.get(typesNeeded.get(0));
        //if the questionType we're basing our selection on is a childType, the list of questions we select from is their parentType (that has children)
        if(EquineQuestionTypes.isChildQuestionType(typesNeeded.get(0))){
            typeQuestions = EquineQuestionTypes.getParentQuestionCountsWithChildren(questionTypesListMap.get("structure"));
        }
        QuestionCount leastSeenWithTypesNeeded = null;
        //for every questionCount object in the list
        for(QuestionCount questionCount : typeQuestions){
            if(checkRelatedImageHasAllNeededTypesOfQuestions(typesNeeded,studentModel,questionCount.getQuestion())) {

                if (leastSeenWithTypesNeeded == null  ){
                    //set leastSeenWithTypesNeeded to the current QuestionCount
                    leastSeenWithTypesNeeded = questionCount;
                }
                //if leastSeenWithTypesNeeded hasn't been set to a questionCount yet and the current's timesSeen is less than leastSeenWithTypesNeeded
                else if(questionCount.getTimesSeen() < leastSeenWithTypesNeeded.getTimesSeen()) {
                    //set leastSeenWithTypesNeeded to the current QuestionCount
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

    public static boolean checkRelatedImageHasAllNeededTypesOfQuestions(List<String> types, StudentModel studentModel, Question question){
        Set<String> typesPresent=new LinkedHashSet<>();
        List<Question> questionList = QuestionPool.getQuestionsWithUrl(studentModel.getUserQuestionSet().getAllQuestions(), question.getImageUrl());
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


    public static int calcLevel(Map<String, Double> scoresPerType) {
        List<Double> orderedScores = orderedScores(scoresPerType);
        if (orderedScores.get(0) == 100 && orderedScores.get(1) == 100 && orderedScores.get(2) == 100) { //100 on all
            return 6;
        }
        else if (orderedScores.get(0) == 100 && orderedScores.get(1) == 100 && orderedScores.get(2) > 50) { //above 50 on attachment
            return 5;
        }
        else if (orderedScores.get(0) == 100 && orderedScores.get(1) == 100) { //100 on structure
            return 4;
        }
        else if (orderedScores.get(0) == 100 && orderedScores.get(1) > 50 && orderedScores.get(1) < 100) { //100 on plane, between 50 and 100 on structure
            return 3;
        }
        else if (orderedScores.get(0) > 50) { //over 50 on plane
            return 2;
        }
        else if (orderedScores.get(0) <= 50) { //less than 50 on plane
            return 1;
        }
        else {
            throw new RuntimeException("no valid level for data");
        }
    }

    //TODO: abstract this into the domain package
    private static List<Double> orderedScores(Map<String, Double> scoresPerType){
        List<Double> orderedScores=new ArrayList<>();
        for(EquineQuestionTypes quesType: EquineQuestionTypes.values()){
            if(scoresPerType.get(quesType.toString())==null){
                orderedScores.add(-1.0);
            }
            else {
                orderedScores.add(scoresPerType.get(quesType.toString()));
            }
        }

        return orderedScores;//ordered list of scores
    }

    public static List<Question> filterQuestions(int level, List<Question> questionList){
        if(level > 2){
            questionList = removeTypeFromQuestionList(questionList,  EquineQuestionTypes.plane.toString());
        }
        if(level < 2 || level > 6){
            questionList = removeTypeFromQuestionList(questionList,  EquineQuestionTypes.structure.toString());
        }
        if(level < 4 || level > 6){
            questionList = removeTypeFromQuestionList(questionList, EquineQuestionTypes.attachment.toString());
        }
        if(level <= 6){
            questionList = removeTypeFromQuestionList(questionList,  EquineQuestionTypes.zone.toString());
        }
        return questionList;
    }

    public static List<Question> removeTypeFromQuestionList(List<Question> questions, String type){
        List<Question> newList = new ArrayList<>();
        for(Question currQuestion: questions){
            if(!currQuestion.getType().equals(type)){
                Question cleanQuestion = removeTypeFromQuestion(currQuestion, type);
                newList.add(cleanQuestion);
            }
        }
        return newList;
    }

    public static Question removeTypeFromQuestion(Question question, String type){
        if(!question.getType().equals(type)) {
            if (question.getFollowupQuestions().size() != 0){
                List<Question> cleanFollowups = new ArrayList<>();
                for (Question followupQuestion : question.getFollowupQuestions()){
                    if (!followupQuestion.getType().equals(type)) {
                        Question cleanFollowUp = removeTypeFromQuestion(followupQuestion, type);
                        cleanFollowups.add(cleanFollowUp);
                    }
                }
                return new Question(question, cleanFollowups);
            }
            return question;
        }
        else{
            throw new RuntimeException("root question matches type, cannot remove itself");
        }
    }

}

package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.*;

public class LevelTaskGenerator implements TaskGenerator {

    private Map<Integer, List<String>> levelToTypesMap;

    public LevelTaskGenerator(Map<Integer, List<String>> levelToTypesMap){
        this.levelToTypesMap = levelToTypesMap;
    }

    //TODO: test makeTask more thoroughly
    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        int studentLevel = calcLevel(studentModel.calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis));
        return makeTaskGivenLevel(studentModel, levelToTypesMap.get(studentLevel), studentLevel);
    }

    public static ImageTask makeTaskGivenLevel(StudentModel studentModel, List<String> levelTypes, int level) {
        Question initialQuestion= leastSeenQuestionWithTypesNeeded(levelTypes,studentModel);
        List<Question> questionList = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getAllQuestions(), initialQuestion.getImageUrl());
        questionList = filterQuestions(level, questionList);
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList, "None");

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

    public static int calcLevel(Map<String, Double> scoresPerType) {
        List<Double> orderedScores = orderedScores(scoresPerType);
        int level = 1;//sets score to one

        if (orderedScores.get(0) < 60)
            return level;//if user has score less than 75 on plane , returns level 1

        else {
            for(int i = 0; i < orderedScores.size()-1; i++) {

                if (orderedScores.get(i) >= 60 && orderedScores.get(i) < 100) {//if score is less than 100 and greater than 74, adds a level
                    level = level + 1;
                    return level;//returns level in this case
                }

                else if (orderedScores.get(i) == 100)
                    level = level + 2;//if score is 100, adds 2 to level/skips a level
            }

            return level;
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
        if(level < 6){
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

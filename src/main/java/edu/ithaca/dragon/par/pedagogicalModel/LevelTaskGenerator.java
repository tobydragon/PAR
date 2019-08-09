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

    //TODO:TEST makeTask
    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        Map<String,List<QuestionCount>> questionTypesListMap=new LinkedHashMap<>();
        StudentModel.questionByTypeMap(studentModel.getUserQuestionSet().getQuestionCounts(),questionTypesListMap);
        int studentLevel=StudentModel.calcLevel(studentModel.calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis));
        List<String> levelTypes= levelToTypesMap.get(studentLevel);

        Question initialQuestion=leastSeenQuestion(levelTypes,studentModel,questionTypesListMap);
        List<Question> questionList = buildQuestionListWithSameUrl2(studentModel, initialQuestion);
        questionList = TaskGeneratorImp1.filterQuestions(studentLevel, questionList);
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList);

        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(questionList);

        return imageTask;
    }

    public static Question leastSeenQuestion(List<String> types,StudentModel studentModel,Map<String,List<QuestionCount>> questionTypesListMap){
        List<QuestionCount> typeQuestions=questionTypesListMap.get(types.get(0));

        int index=0;

        for(int i=0;i<typeQuestions.size();i++){
            if(checkIfAllTypesInQuesList(types,studentModel,typeQuestions.get(i).getQuestion())) {
                if(typeQuestions.get(i).timesSeen <typeQuestions.get(index).timesSeen || !checkIfAllTypesInQuesList(types,studentModel,typeQuestions.get(index).getQuestion())) {
                    index = i;
                }
            }
        }
        if(!checkIfAllTypesInQuesList(types,studentModel,typeQuestions.get(index).getQuestion())){
            throw new RuntimeException("Questions not present for the type(s)");
        }
        return typeQuestions.get(index).getQuestion();
    }

    public static boolean checkIfAllTypesInQuesList(List<String> types,StudentModel studentModel,Question question){
        Set<String> typesPresent=new LinkedHashSet<>();
        List<Question> questionList = buildQuestionListWithSameUrl2(studentModel, question);
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

    public static List<Question> buildQuestionListWithSameUrl2(StudentModel studentModel, Question initialQuestion){
        List<Question> questionList = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getAllQuestions(), initialQuestion.getImageUrl());
        return questionList;
    }

    private static QuestionCount getLeastSeenQuestionWithFollowup(List<QuestionCount> questionCountList){
        int index=0;
        List<QuestionCount> questionsWithFollowup=new ArrayList<>();
        for (int i=0;i<questionCountList.size();i++){
            if(questionCountList.get(i).question.getFollowupQuestions().size()>=1){
                questionsWithFollowup.add(questionCountList.get(i));
            }
        }
        for(int i=0;i<questionsWithFollowup.size();i++){
            if(questionsWithFollowup.get(i).timesSeen <questionsWithFollowup.get(index).timesSeen ){
                index=i;
            }
        }
        return questionsWithFollowup.get(index);
    }
    /**
     * This function is supposed to look through a list of questions and return the structure question
     * seen the least that contains followup questions.
     * @param questionCountList
     * @return the least seen structure question that has followup questions.
     */
    public static QuestionCount getLeastSeenQuestionWithAttachmentQuestions(List<QuestionCount> questionCountList){
        return getLeastSeenQuestionWithFollowup(questionCountList);
    }

}

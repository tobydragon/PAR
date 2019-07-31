package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.*;

public class LevelTaskGenerator implements TaskGenerator {

    private static final Map<Integer, List<String>> levelMap=makeLevelMap();

    private static Map<Integer, List<String>> makeLevelMap() {
        Map<Integer,List<String>> levelMap=new LinkedHashMap<>();
        if(levelMap.isEmpty()) {
            levelMap.put(1, Arrays.asList(EquineQuestionTypes.plane.toString()));
            levelMap.put(2, Arrays.asList(EquineQuestionTypes.plane.toString(), EquineQuestionTypes.structure.toString()));
            levelMap.put(3, Arrays.asList(EquineQuestionTypes.structure.toString()));
            levelMap.put(4, Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()));
            levelMap.put(5, Arrays.asList(EquineQuestionTypes.attachment.toString()));
            levelMap.put(6, Arrays.asList(EquineQuestionTypes.attachment.toString(),EquineQuestionTypes.zone.toString()));
            levelMap.put(7,Arrays.asList(EquineQuestionTypes.zone.toString()));

        }
        return levelMap;
    }

    public LevelTaskGenerator(){}

    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        Map<String,List<QuestionCount>> questionTypesListMap=new LinkedHashMap<>();
        questionByTypeMap(studentModel.getUserQuestionSet().getQuestionCounts(),questionTypesListMap);
        int studentLevel=StudentModel.calcLevel(studentModel.calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis));
        List<String> levelTypes=levelMap.get(studentLevel);

        Question initialQuestion=leastSeenQuestion(questionTypesListMap.get(levelTypes.get(0)));
        List<Question> questionList = TaskGeneratorImp1.addAllQuestions(studentModel, initialQuestion);
        questionList = TaskGeneratorImp1.filterQuestions(studentLevel, questionList);
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList);

        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(questionList);

        return imageTask;
    }

    private static Question leastSeenQuestion(List<QuestionCount> questionCountList){
        int index=0;
        for(int i=0;i<questionCountList.size();i++){
            if(questionCountList.get(i).timesSeen <questionCountList.get(index).timesSeen ){
                index=i;
            }
        }
        return questionCountList.get(index).getQuestion();
    }




    public  void questionByTypeMap(List<QuestionCount> questionCountList, Map<String,List<QuestionCount>> questionTypesListMap ){

            for(QuestionCount questionCount:questionCountList){
                if(questionTypesListMap.get(questionCount.getQuestion().getType())==null){
                    List<QuestionCount> questions=new ArrayList<>();
                    questions.add(questionCount);
                    questionTypesListMap.put(questionCount.getQuestion().getType(),questions);
                }
                else questionTypesListMap.get(questionCount.getQuestion().getType()).add(questionCount);
                //makes recursive call for follow ups
                questionByTypeMap(questionCount.getFollowupCounts(),questionTypesListMap);
            }


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

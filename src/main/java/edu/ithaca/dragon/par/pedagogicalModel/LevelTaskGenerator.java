package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.*;

public class LevelTaskGenerator implements TaskGenerator {

    private static final Map<Integer, List<EquineQuestionTypes>> levelMap=makeLevelMap();

    private static Map<Integer, List<EquineQuestionTypes>> makeLevelMap() {
        Map<Integer,List<EquineQuestionTypes>> levelMap=new LinkedHashMap<>();
        if(levelMap.isEmpty()) {
            levelMap.put(1, Arrays.asList(EquineQuestionTypes.plane));
            levelMap.put(2, Arrays.asList(EquineQuestionTypes.plane, EquineQuestionTypes.structure));
            levelMap.put(3, Arrays.asList(EquineQuestionTypes.structure));
            levelMap.put(4, Arrays.asList(EquineQuestionTypes.structure,EquineQuestionTypes.attachment));
            levelMap.put(5, Arrays.asList(EquineQuestionTypes.attachment));
            levelMap.put(6, Arrays.asList(EquineQuestionTypes.attachment,EquineQuestionTypes.zone));
            levelMap.put(7,Arrays.asList(EquineQuestionTypes.zone));

        }
        return levelMap;
    }

    public LevelTaskGenerator(){}

    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        Map<EquineQuestionTypes,List<Question>> equineQuestionTypesListMap=new LinkedHashMap<>();
        int studentLevel=StudentModel.calcLevel(studentModel.calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis));


        return null;
    }

//TODO:MAKE RECURSIVE
    public  Map<EquineQuestionTypes,List<Question>> equineQuestionTypesMap(StudentModel studentModel){
        Map<EquineQuestionTypes,List<Question>> equineQuestionTypesMap=new LinkedHashMap<>();
        List<QuestionCount> userQuestionSet=studentModel.getUserQuestionSet().getQuestionCounts();

        for(EquineQuestionTypes types:EquineQuestionTypes.values()){
            List<Question> questions=new ArrayList<>();
            for(int i=0;i<userQuestionSet.size();i++){
                if(userQuestionSet.get(i).getQuestion().getType().equals(types.toString()))
                    questions.add(userQuestionSet.get(i).getQuestion());
            }
            equineQuestionTypesMap.put(types,questions);
        }
        return equineQuestionTypesMap;
    }
}

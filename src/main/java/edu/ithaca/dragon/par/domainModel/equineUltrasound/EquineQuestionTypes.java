package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.QuestionCount;

import java.util.*;

public enum EquineQuestionTypes {
    plane, structure, attachment, zone;

    public static Map<Integer, List<String>> makeLevelToTypesMap() {
        Map<Integer,List<String>> levelMap=new LinkedHashMap<>();
        levelMap.put(1, Arrays.asList(EquineQuestionTypes.plane.toString()));
        levelMap.put(2, Arrays.asList(EquineQuestionTypes.plane.toString(), EquineQuestionTypes.structure.toString()));
        levelMap.put(3, Arrays.asList(EquineQuestionTypes.structure.toString()));
        levelMap.put(4, Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()));
        levelMap.put(5, Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString(), EquineQuestionTypes.attachment.toString()));
        levelMap.put(6, Arrays.asList(EquineQuestionTypes.zone.toString()));
        levelMap.put(7,Arrays.asList(EquineQuestionTypes.zone.toString()));
        return levelMap;
    }

    public static boolean isChildQuestionType(String type){
        String childType = EquineQuestionTypes.attachment.toString();
        if(type.equals(childType)){
            return true;
        }
        return false;
    }

    public static List<QuestionCount> getParentQuestionCountsWithChildren(List<QuestionCount> questionCounts) {
        List<QuestionCount> typeQuestionsStructureWithFollowup = new ArrayList<>();
        for (QuestionCount questionCountType : questionCounts) {
            if (questionCountType.getFollowupCounts().size() > 0) {
                typeQuestionsStructureWithFollowup.add(questionCountType);
            }
        }
        return typeQuestionsStructureWithFollowup;
    }

}

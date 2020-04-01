package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum EquineQuestionTypes {
    plane, structure, attachment, zone;

    public static Map<Integer, List<String>> makeLevelToTypesMap() {
        Map<Integer,List<String>> levelMap=new LinkedHashMap<>();
        levelMap.put(1, Arrays.asList(EquineQuestionTypes.plane.toString()));
        levelMap.put(2, Arrays.asList(EquineQuestionTypes.plane.toString(), EquineQuestionTypes.structure.toString()));
        levelMap.put(3, Arrays.asList(EquineQuestionTypes.structure.toString()));
        levelMap.put(4, Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()));
        levelMap.put(5, Arrays.asList(EquineQuestionTypes.attachment.toString()));
        levelMap.put(6, Arrays.asList(EquineQuestionTypes.attachment.toString(),EquineQuestionTypes.zone.toString()));
        levelMap.put(7,Arrays.asList(EquineQuestionTypes.zone.toString()));
        return levelMap;
    }

    public static boolean isChildQuestionType(String status){
        String childType = "attachment";
        if(status == childType){
            return true;
        }
        return false;
    }
}

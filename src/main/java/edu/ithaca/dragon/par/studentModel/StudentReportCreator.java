package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.io.StudentReport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentReportCreator {

    public static StudentReport buildStudentReport(UserResponseSet userResponseSet, Map<String,List<QuestionCount>> questionTypesListMap, int numOfRecentResponsesToConsider){

        Map<String, List<ResponsesPerQuestion>> splitResponsesByType=UserResponseSet.splitResponsesByType(userResponseSet.getResponsesPerQuestionList());
        Map<String,Integer> responsesPerType=allResponsesPerType(splitResponsesByType);
        Map<String,Integer> questionsPerType=numberOfQuestionsPerType(questionTypesListMap);
        Map<String,Integer> numberOfQuestionsAnswered=numOfQuestionsAnswered(splitResponsesByType);
        Map<String,Double> currScoreForEachType=userResponseSet.calcKnowledgeEstimateByType(numOfRecentResponsesToConsider);
        return new StudentReport(userResponseSet.getUserId(),currScoreForEachType,responsesPerType,questionsPerType,numberOfQuestionsAnswered);
    }

    public static Map<String,Integer> allResponsesPerType(Map<String, List<ResponsesPerQuestion>> responseByType){
        Map<String,Integer> responsesPerType=new LinkedHashMap<>();
        int responseCount;
        for(String currType:responseByType.keySet()){
            responseCount=0;
            for(int i=0;i<responseByType.get(currType).size();i++){
                responseCount=responseCount+responseByType.get(currType).get(i).getAllResponses().size();
            }
            responsesPerType.put(currType,responseCount);
        }
        return responsesPerType;
    }

    public static Map<String, Integer> numberOfQuestionsPerType(Map<String, List<QuestionCount>> questionTypesListMap) {
        Map<String, Integer> numberOfQuestionsPerType=new LinkedHashMap<>();
        for(String currType:questionTypesListMap.keySet())
            numberOfQuestionsPerType.put(currType,questionTypesListMap.get(currType).size());

        return numberOfQuestionsPerType;
    }

    public static Map<String, Integer> numOfQuestionsAnswered(Map<String, List<ResponsesPerQuestion>> splitResponsesByType) {
        Map<String, Integer> numOfQuestionsAnswered=new LinkedHashMap<>();
        for(String currType:splitResponsesByType.keySet()){
            numOfQuestionsAnswered.put(currType,splitResponsesByType.get(currType).size());
        }
        return numOfQuestionsAnswered;
    }






}

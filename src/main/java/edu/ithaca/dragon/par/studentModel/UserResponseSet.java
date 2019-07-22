package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;

import java.util.*;

public class UserResponseSet {
    private String userId;
    private List<ResponsesPerQuestion> userResponses;

    //windowSize is the amount of responses to look back on when calculating the understanding of a topic
    public static int windowSize = 4;

    public UserResponseSet(String userIdIn) {
        this.userId = userIdIn;
        userResponses = new ArrayList<>();
    }

    public void addResponse(ResponsesPerQuestion response) {
        int index = sameResponseCheck(response);

        if (index == -1) userResponses.add(response);

        else userResponses.get(index).addNewResponse(response.getFirstResponse());
    }
    public void addAllResponses(List<ResponsesPerQuestion> allResponsesIn) {
        for (int i = 0; i < allResponsesIn.size(); i++) {
            addResponse(allResponsesIn.get(i));
        }
    }

    private int sameResponseCheck(ResponsesPerQuestion response) {
        if (userResponses.isEmpty()) {
            return -1;
        } else {
            for (int i = 0; i < userResponses.size(); i++) {
                if (userResponses.get(i).getQuestionId().equals(response.getQuestionId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getUserResponsesSize() {
        return userResponses.size();
    }

    public int countTotalResponses() {
        int count = 0;
        for (ResponsesPerQuestion responses : userResponses) {
            count = count + responses.allResponsesSize();
        }
        return count;
    }

    public void setUserResponses(List<ResponsesPerQuestion> userResponsesIn) {
        this.userResponses = userResponsesIn;
    }
    public List<ResponsesPerQuestion> getUserResponses() {
        return userResponses;
    }

    public void setUserId(String userIdIn) {
        this.userId = userIdIn;
    }
    public String getUserId() {
        return userId;
    }

    public double knowledgeCalc() {
        //TODO: should 12 be replaced by windowSize?
        return knowledgeCalc(userResponses, 12);
    }
    /**
     * @param responsesToConsider how many responses should the algorithm look back on to calculate the recent average?
     * @return
     */
    public double knowledgeCalc(int responsesToConsider) {
        return knowledgeCalc(userResponses, responsesToConsider);
    }

    /**
     * @param allResponses
     * @param responsesToConsider how many responses should the algorithm look back on
     * @return
     */
    private static double knowledgeCalc(List<ResponsesPerQuestion> allResponses, int responsesToConsider) {
        //return -1 if the list is empty
        if (allResponses.size() == 0)
            return -1.0;
        double scoreBeforeDivision = 0;
        for (int i = allResponses.size() - 1, j = 0; j < responsesToConsider; i--, j++) {

            if (i >= 0) scoreBeforeDivision += allResponses.get(i).knowledgeCalc();

        }
        return (scoreBeforeDivision / responsesToConsider);
    }
    public Map<String, Double> knowledgeScoreByType() {
        Map<String, List<ResponsesPerQuestion>> responseByType = splitResponsesByType(userResponses);
        Map<String, Double> responseByTypeDouble = new LinkedHashMap<>();
        for (EquineQuestionTypes currType: EquineQuestionTypes.values()) {
            List<ResponsesPerQuestion> quesList = responseByType.get(currType.toString());
            responseByTypeDouble.put(currType.toString(), knowledgeCalc(quesList, windowSize));
        }
        return responseByTypeDouble;
    }

    private static  Map<String, List<ResponsesPerQuestion>> splitResponsesByType(List<ResponsesPerQuestion> responsesPerQuestions) {
        Map<String, List<ResponsesPerQuestion>> responseByType = new HashMap<>();
        List<ResponsesPerQuestion> responsesPerQuestion = new ArrayList<>();
        //adds types to the map from EquineQuestionTypes and give each type a new empty list
        for (EquineQuestionTypes currType: EquineQuestionTypes.values()) {
            responseByType.put(currType.toString(), responsesPerQuestion);
        }
        for (int i = 0; i < responsesPerQuestions.size(); i++) {
            String currType=responsesPerQuestions.get(i).getQuestionType();

            if (responseByType.get(currType).size()==0) {
                List<ResponsesPerQuestion> responsesPerQuestions1 = new ArrayList<>();
                responsesPerQuestions1.add(responsesPerQuestions.get(i));
                //replaces the empty list with the new list that will contain the first response that corresponds to the type
                responseByType.replace(currType, responsesPerQuestions1);
            }
            //adds the rest if the responses to the current type
            else responseByType.get(currType).add(responsesPerQuestions.get(i));

        }
        return responseByType;
    }

    public Map<EquineQuestionTypes, String> generateKnowledgeBaseMap(){
        Map<String, List<ResponsesPerQuestion>> responseByType = splitResponsesByType(userResponses);
        Map<EquineQuestionTypes,String> knowledgeBaseMap=new HashMap<>();
        for (EquineQuestionTypes currType : EquineQuestionTypes.values()) {
            List<ResponsesPerQuestion> quesList = responseByType.get(currType.toString());
            knowledgeBaseMap.put(currType, knowledgeBaseCalc(quesList,windowSize));
        }
        return knowledgeBaseMap;
    }
    private static String knowledgeBaseCalc(List<ResponsesPerQuestion> allResponses, int responsesToConsider) {
        //return ____ if the list is empty
        if (allResponses.size() == 0)  return "____";

        String knowledgeBase="____";
        List<Double> scores=new ArrayList<>();
        for (int i = allResponses.size() - 1, j = 0; j < responsesToConsider; i--, j++) {
            //scores are added backwards from the most recent to older responses
            if (i >= 0) scores.add(allResponses.get(i).knowledgeCalc());
        }
        //reads in the scores from the last element to first since the most recent response is at beginning of the list
        for(int i=scores.size()-1;i>-1;i--){
            //re-writes the string knowledgeBase i.e ____ -> ___O or ___X and so on

            if(scores.get(i)==100) knowledgeBase=knowledgeBase.substring(1)+"O";

            else knowledgeBase=knowledgeBase.substring(1)+"X";
        }
        return knowledgeBase;
    }


    @Override
    public boolean equals(Object otherObj) {
        if (otherObj == null) {
            return false;
        }
        if (!UserResponseSet.class.isAssignableFrom(otherObj.getClass())) {
            return false;
        }
        UserResponseSet other = (UserResponseSet) otherObj;
        return this.getUserResponses().equals(other.getUserResponses())
                && this.getUserId().equals(other.getUserId());
    }
}
package edu.ithaca.dragon.par.studentModel;

import java.util.*;

public class UserResponseSet {
    private String userId;
    private List<ResponsesPerQuestion> userResponse;

    //windowSize is the amount of responses to look back on when calculating the understanding of a topic
    private int windowSize;

    public UserResponseSet(String userIdIn) {
        this.userId = userIdIn;
        userResponse = new ArrayList<>();

        //TODO: consider taking windowSize in as a parameter instead of hardcoding
        windowSize = 4;
    }


    public void addResponse(ResponsesPerQuestion response) {
        int index = sameResponseCheck(response);

        if (index == -1) userResponse.add(response);

        else userResponse.get(index).addNewResponse(response.getResponseText());
    }

    public void addAllResponses(List<ResponsesPerQuestion> allResponsesIn) {
        for (int i=0;i<allResponsesIn.size();i++) {
            addResponse(allResponsesIn.get(i));
        }
    }

    private int sameResponseCheck(ResponsesPerQuestion response) {
        if (userResponse.isEmpty()) {
            return -1;
        } else {
            for (int i = 0; i < userResponse.size(); i++) {
                if (userResponse.get(i).getQuestionId().equals(response.getQuestionId())) {
                    return i;
                }
            }
        }
        return -1;
    }
//get question answered
    public int getUserResponsesSize() {
        return userResponse.size();
    }

    public int countTotalResponses(){
        int count=0;
        for(ResponsesPerQuestion responses: userResponse){
            count=count+responses.allResponseTextSize();
        }
        return count;
    }

    public void setUserResponse(List<ResponsesPerQuestion> userResponsesIn) {
        this.userResponse = userResponsesIn;
    }

    public List<ResponsesPerQuestion> getUserResponse() {
        return userResponse;
    }

    public void setUserId(String userIdIn) {
        this.userId = userIdIn;
    }

    public String getUserId() {
        return userId;
    }

    public int getWindowSize(){ return windowSize; }

    public double knowledgeCalc() {
        //TODO: should 12 be replaced by windowSize?
        return knowledgeCalc(userResponse, 12);
    }
    /**
     * @param responsesToConsider how many responses should the algorithm look back on to calculate the recent average?
     * @return
     */
    public double knowledgeCalc(int responsesToConsider) {
        return knowledgeCalc(userResponse, responsesToConsider);
    }

    /**
     *
     * @param allResponses
     * @param responsesToConsider how many responses should the algorithm look back on
     * @return
     */
    public static double knowledgeCalc(List<ResponsesPerQuestion> allResponses, int responsesToConsider) {
        //return -1 if the list is empty
        if(allResponses.size() == 0)
            return -1.0;

        double scoreBeforeDivision = 0;
        for(int i = allResponses.size()-1, j = 0; j < responsesToConsider; i--, j++){

            if(i >= 0){
                scoreBeforeDivision+=allResponses.get(i).knowledgeCalc();

            }
        }
        return (scoreBeforeDivision / responsesToConsider);
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
        return this.getUserResponse().equals(other.getUserResponse())
                && this.getUserId().equals(other.getUserId());
    }

    public Map<String, Double> knowledgeScoreByType() {
        Map<String, List<ResponsesPerQuestion>> responseByType = splitResponsesByType(userResponse);
        Map<String, Double> responseByTypeDouble = new HashMap<>();
        for (String quesType : responseByType.keySet()) {
            List<ResponsesPerQuestion> quesList = responseByType.get(quesType);
            responseByTypeDouble.put(quesType, knowledgeCalc(quesList, windowSize));
        }
        return responseByTypeDouble;
    }

    private static Map<String, List<ResponsesPerQuestion>> splitResponsesByType(List<ResponsesPerQuestion> responsesPerQuestions) {
        Map<String, List<ResponsesPerQuestion>> responseByType = new HashMap<>();
        for (int i = 0; i < responsesPerQuestions.size(); i++) {
            if (responseByType.get(responsesPerQuestions.get(i).getQuestionType()) == null) {
                List<ResponsesPerQuestion> responsesPerQuestions1 = new ArrayList<>();
                responsesPerQuestions1.add(responsesPerQuestions.get(i));
                responseByType.put(responsesPerQuestions.get(i).getQuestionType(), responsesPerQuestions1);
            } else {
                responseByType.get(responsesPerQuestions.get(i).getQuestionType()).add(responsesPerQuestions.get(i));
            }
        }
        return responseByType;
    }
}

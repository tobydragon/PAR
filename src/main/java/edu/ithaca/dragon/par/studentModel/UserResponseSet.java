package edu.ithaca.dragon.par.studentModel;

import java.util.*;

public class UserResponseSet {
    private String userId;
    private List<ResponsesPerQuestion> userResponse;


    public UserResponseSet(String userIdIn) {
        this.userId = userIdIn;
        userResponse = new ArrayList<>();
    }


    public void addResponse(ResponsesPerQuestion response) {
        int index = sameResponseCheck(response);

        if (index == -1) userResponse.add(response);

        else userResponse.get(index).addNewResponse(response.getResponseText());
    }

    public void addAllResponses(List<ResponsesPerQuestion> allResponsesIn) {
        int index = 0;
        for (int i = 0; i < allResponsesIn.size(); i++) {
            index = sameResponseCheck(allResponsesIn.get(i));
            if (index > -1) break;
        }
        if (index == -1) userResponse.addAll(allResponsesIn);

        else {
            for (int i = 0; i < userResponse.size(); i++) {
                for (int k = 0; k < allResponsesIn.size(); k++) {
                    if (userResponse.get(i).getQuestionId().equals(allResponsesIn.get(k).getQuestionId())) {
                        userResponse.get(i).addNewResponse(allResponsesIn.get(k).getResponseText());
                    }
                }
            }
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

    public int getUserResponsesSize() {
        return userResponse.size();
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

    /**
     * @param responsesToConsider how many responses should the algorithm look back on to calculate the recent average?
     * @return
     */
    public double knowledgeCalc() {
        return knowledgeCalc(userResponse, 12);
    }

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

            if(i >= 0){ //if there are still responses, get one and contribute it to the running score
                //check if the response is correct
                if(allResponses.get(i).getResponseText().equals(allResponses.get(i).getQuestion().getCorrectAnswer())){
                    scoreBeforeDivision++;
                }
            }
            else { //there are no responses left

            }
        }
        return (scoreBeforeDivision / responsesToConsider) * 100;
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
            responseByTypeDouble.put(quesType, knowledgeCalc(quesList, 4));
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

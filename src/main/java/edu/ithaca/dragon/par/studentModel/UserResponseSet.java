package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Double.parseDouble;

public class UserResponseSet {
    private String userId;
    private List<ResponsesPerQuestion> responsesPerQuestionList;

    public UserResponseSet(String userIdIn) {
        this.userId = userIdIn;
        responsesPerQuestionList = new ArrayList<>();
    }


    public void addResponse(ResponsesPerQuestion response) {
        int index = sameResponseCheck(response);

        if (index == -1) responsesPerQuestionList.add(response);

        else {
            responsesPerQuestionList.get(index).addNewResponse(response.getFirstResponse());//adds response to allResponses list
            ResponsesPerQuestion response1= responsesPerQuestionList.get(index);//keeps new value
            responsesPerQuestionList.remove(index);//removes old index
            responsesPerQuestionList.add(response1);//updates to the last position (most recently answered)
        }
    }
    public ResponsesPerQuestion getResponseById(String Id){
        for (ResponsesPerQuestion response : responsesPerQuestionList){
            if (response.getQuestionId().equals(Id)){
                return response;
            }
        }
        throw new IllegalArgumentException();
    }

    public void addAllResponses(List<ResponsesPerQuestion> allResponsesIn) {
        for (int i = 0; i < allResponsesIn.size(); i++) {
            addResponse(allResponsesIn.get(i));
        }
    }

    private int sameResponseCheck(ResponsesPerQuestion response) {
        if (responsesPerQuestionList.isEmpty()) {
            return -1;
        } else {
            for (int i = 0; i < responsesPerQuestionList.size(); i++) {
                if (responsesPerQuestionList.get(i).getQuestionId().equals(response.getQuestionId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getUserResponsesSize() {
        return responsesPerQuestionList.size();
    }

    /**
     *
     * @return the number of questions that have been responded to
     */
    public int countTotalResponses() {
        int count = 0;
        for (ResponsesPerQuestion responses : responsesPerQuestionList) {
            count = count + responses.allResponsesSize();
        }
        return count;
    }

    public void setResponsesPerQuestionList(List<ResponsesPerQuestion> userResponsesIn) {
        this.responsesPerQuestionList = userResponsesIn;
    }

    public List<ResponsesPerQuestion> getResponsesPerQuestionList() {
        return responsesPerQuestionList;
    }

    public void setUserId(String userIdIn) {
        this.userId = userIdIn;
    }

    public String getUserId() {
        return userId;
    }

    public double calcKnowledgeEstimate() {
        //TODO: should 12 be replaced by windowSize?
        return calcKnowledgeEstimate(responsesPerQuestionList, 12);
    }

    public double calcKnowledgeEstimate(int responseCountToConsider) {
        return calcKnowledgeEstimate(responsesPerQuestionList, responseCountToConsider);
    }

    private static double calcKnowledgeEstimate(List<ResponsesPerQuestion> allResponses, int responsesToConsider) {
        if (allResponses.size() == 0) {
            return -1.0;
        }
        double scoreBeforeDivision = 0;
        for (int i = allResponses.size() - 1, j = 0; j < responsesToConsider; i--, j++) {
            if (i >= 0) {
                scoreBeforeDivision += allResponses.get(i).knowledgeCalc();
            }
        }
        return (scoreBeforeDivision / responsesToConsider);
    }

    public Map<String, Double> calcKnowledgeEstimateByType(int numOfRecentResponsesToConsider) {
        Map<String, List<ResponsesPerQuestion>> responseByType = splitResponsesByType(responsesPerQuestionList);
        Map<String, Double> responseByTypeDouble = new LinkedHashMap<>();
        for (EquineQuestionTypes currType: EquineQuestionTypes.values()) {
            List<ResponsesPerQuestion> quesList = responseByType.get(currType.toString());
            responseByTypeDouble.put(currType.toString(), calcKnowledgeEstimate(quesList, numOfRecentResponsesToConsider));
        }
        return responseByTypeDouble;
    }

    public static  Map<String, List<ResponsesPerQuestion>> splitResponsesByType(List<ResponsesPerQuestion> responsesPerQuestions) {
        Map<String, List<ResponsesPerQuestion>> responseByType = new LinkedHashMap<>();
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

    public Map<EquineQuestionTypes, String> calcKnowledgeEstimateStringsByType(int numOfRecentResponsesToConsider){
        Map<String, List<ResponsesPerQuestion>> responseByType = splitResponsesByType(responsesPerQuestionList);
        Map<EquineQuestionTypes,String> knowledgeBaseMap=new LinkedHashMap<>();
        for (EquineQuestionTypes currType : EquineQuestionTypes.values()) {
            List<ResponsesPerQuestion> quesList = responseByType.get(currType.toString());
            knowledgeBaseMap.put(currType, calcKnowledgeEstimateString(quesList,numOfRecentResponsesToConsider));
        }
        return knowledgeBaseMap;
    }

    public static String calcKnowledgeEstimateString(List<ResponsesPerQuestion> allResponses, int numOfRecentResponsesToConsider) {
        String estimateString = "";
        if (allResponses.size() >= numOfRecentResponsesToConsider ){
            for( int i = allResponses.size()-1; i>=allResponses.size()-numOfRecentResponsesToConsider; i--){
                estimateString += convertNumEstimateToStringRepresentation(allResponses.get(i).knowledgeCalc());
            }
            return estimateString;
        }
        else {
            for( int i = allResponses.size()-1; i>=0; i--){
                estimateString += convertNumEstimateToStringRepresentation(allResponses.get(i).knowledgeCalc());
            }
            int numBlank = numOfRecentResponsesToConsider-allResponses.size();
            for (int i=0; i < numBlank; i++){
                estimateString = estimateString + "_";
            }
            return estimateString;
        }
    }

    public static String convertNumEstimateToStringRepresentation(Double numEstimate){
        if (numEstimate > 99){
            return "O";
        }
        else {
            return "X";
        }

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
        return this.getResponsesPerQuestionList().equals(other.getResponsesPerQuestionList())
                && this.getUserId().equals(other.getUserId());
    }

    /**
     *
     * @return number of responses across all questions
     */
    public int getAllResponseCount() {
        int count = 0;
        List<ResponsesPerQuestion> responseList = getResponsesPerQuestionList();
        for (ResponsesPerQuestion response : responseList){
            count += response.getAllResponses().size();
        }
        return count;
    }

    /**
     *
     * @return percent of questions that were most recently answered right after being answered wrong the first time
     */
    public double calcPercentLastAnswerRightAfterWrong(){
        if (responsesPerQuestionList.size()==0){
            return -1.0;
        }
        double origWrongCount = 0.0;
        double countRightAfterWrong = 0.0;
        for(ResponsesPerQuestion responseObject: responsesPerQuestionList){
            //if the first answer is wrong
            if (!responseObject.getFirstResponse().equalsIgnoreCase(responseObject.getQuestion().getCorrectAnswer())){
                origWrongCount += 1;
                int i = 1;
                //if the current response is a correct response
                if (responseObject.getAllResponses().get(responseObject.allResponsesSize()-1).getResponseText().equalsIgnoreCase(responseObject.getQuestion().getCorrectAnswer())){
                    countRightAfterWrong += 1.0;
                }
            }
        }

        DecimalFormat df = new DecimalFormat(".##"); //format output to 2 decimal places
        if (origWrongCount == 0.0 || countRightAfterWrong == 0.0){
            return 0;
        }
        return parseDouble(df.format(countRightAfterWrong / origWrongCount * 100.00));
    }

    /**
     *
     * @return percent of questions that were answered incorrectly the first time
     */
    public double calcPercentWrongFirstTime() {
        //no responses
        if (responsesPerQuestionList.size()==0){
            return -1.0;
        }
        double count = 0.0;
        //for every respinse object
        for(ResponsesPerQuestion responseObject: responsesPerQuestionList){
            //if the first answer is wrong
            if (!responseObject.getFirstResponse().equalsIgnoreCase(responseObject.getQuestion().getCorrectAnswer())){
                count += 1;
            }
        }
        DecimalFormat df = new DecimalFormat(".##"); //format output to 2 decimal places
        return parseDouble(df.format(count / responsesPerQuestionList.size() * 100.00));
    }
}

package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponse;

import java.util.*;

public class StudentModel {

    private String userId;
    private UserQuestionSet userQuestionSet;
    private UserResponseSet userResponseSet;

    public StudentModel(String userId, List<Question> questions){
        this.userId = userId;
        this.userQuestionSet = UserQuestionSet.buildNewUserQuestionSetFromQuestions(userId, questions);
        this.userResponseSet = new UserResponseSet(userId);
    }

    public StudentModel(String userId, UserQuestionSet userQuestionSet, UserResponseSet userResponseSet){
        this.userId = userId;
        this.userQuestionSet = userQuestionSet;
        this.userResponseSet = userResponseSet;
    }

    public UserQuestionSet getUserQuestionSet() {
        return userQuestionSet;
    }

    public UserResponseSet getUserResponseSet() {
        return userResponseSet;
    }

    public void givenQuestion(String questionId){
        userQuestionSet.increaseTimesSeen(questionId);
    }

    public int getSeenQuestionCount(){
        return userQuestionSet.getTopLevelSeenQuestions().size();
    }

    public int getUnseenQuestionCount(){
        return userQuestionSet.getTopLevelUnseenQuestions().size();
    }

    public int getResponseCount(){
        return userResponseSet.getUserResponsesSize();
    }

    public String getUserId(){
        return userId;
    }

    public double knowledgeScore(){
        return userResponseSet.calcKnowledgeEstimate();
    }

    public Map<String,Double> calcKnowledgeEstimateByType(int numOfRecentResponsesToConsider){
        return userResponseSet.calcKnowledgeEstimateByType(numOfRecentResponsesToConsider);
    }

    /**
     * Creates a list of ResponsesPerQuestion objects
     * @param imageTaskResponses: response submitted by the user
     * @param questions: QuestionPool object
     * @param userId: user ID
     * @return: List of ResponsesPerQuestion objects
     */
    public static List<ResponsesPerQuestion> createUserResponseObj(ImageTaskResponse imageTaskResponses, QuestionPool questions, String userId){
        List<ResponsesPerQuestion> userResponse =new ArrayList<>();
        ResponsesPerQuestion response; Question ques;
        for(int i=0;i<imageTaskResponses.getTaskQuestionIds().size();i++){
            ques=questions.getQuestionFromId(imageTaskResponses.getTaskQuestionIds().get(i));//finds question in QuestionPool creates a question object
            response=new ResponsesPerQuestion(userId,ques,imageTaskResponses.getResponseTexts().get(i));//creates new response object
            userResponse.add(response);
        }
        return userResponse;
    }

    /**
     * Adds all responses from the imageTaskResponses to the UserResponseSet
     * @param imageTaskResponses: response submitted by the user
     * @param questions: QuestionPool object
     */
    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponses, QuestionPool questions){
        userResponseSet.addAllResponses(createUserResponseObj(imageTaskResponses,questions,this.userId));
    }

    private static List<Double> orderedScores(Map<String, Double> scoresPerType){
        List<Double> orderedScores=new ArrayList<>();
        for(EquineQuestionTypes quesType: EquineQuestionTypes.values()){
            if(scoresPerType.get(quesType.toString())==null){
                orderedScores.add(-1.0);
            }
            else {
                orderedScores.add(scoresPerType.get(quesType.toString()));
            }
        }

        return orderedScores;//ordered list of scores
    }

    public static int calcLevel(Map<String, Double> scoresPerType) {
        List<Double> orderedScores = orderedScores(scoresPerType);
        int level = 1;//sets score to one

        if (orderedScores.get(0) < 75)
            return level;//if user has score less than 75 on plane , returns level 1

        else {
            for(int i = 0; i < orderedScores.size()-1; i++) {

                if (orderedScores.get(i) > 74 && orderedScores.get(i) < 100) {//if score is less than 100 and greater than 74, adds a level
                    level = level + 1;
                    return level;//returns level in this case
                }

                else if (orderedScores.get(i) == 100)
                    level = level + 2;//if score is 100, adds 2 to level/skips a level
            }

            return level;
        }
    }

    public Map<EquineQuestionTypes, String> calcKnowledgeEstimateStringsByType(int numOfRecentResponsesToConsider){
        return userResponseSet.calcKnowledgeEstimateStringsByType(numOfRecentResponsesToConsider);
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null)
            return false;
        if(!StudentModel.class.isAssignableFrom(otherObj.getClass()))
            return false;
        StudentModel other = (StudentModel) otherObj;
        return this.getUserId().equals(other.getUserId())
                && this.getUserQuestionSet().equals(other.getUserQuestionSet())
                && this.getUserResponseSet().equals(other.getUserResponseSet());
    }

    public void addQuestion(Question q) {
        getUserQuestionSet().addQuestion(q);
    }
}

package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;
import edu.ithaca.dragon.par.pedagogicalModel.LevelMessageInformation;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.*;

public class StudentModel {

    private String userId;
    private UserQuestionSet userQuestionSet;
    private UserResponseSet userResponseSet;
    private LevelMessageInformation levelMessageInformation;

    public StudentModel(String userId, List<Question> questions){
        this.userId = userId;
        this.userQuestionSet = UserQuestionSet.buildNewUserQuestionSetFromQuestions(userId, questions);
        this.userResponseSet = new UserResponseSet(userId);
        this.levelMessageInformation = new LevelMessageInformation();
    }

    public StudentModel(String userId, UserQuestionSet userQuestionSet, UserResponseSet userResponseSet){
        this.userId = userId;
        this.userQuestionSet = userQuestionSet;
        this.userResponseSet = userResponseSet;
        this.levelMessageInformation = new LevelMessageInformation();
    }

    public void imageTaskResponseSubmitted(ImageTaskResponseOOP imageTaskResponses, QuestionPool questions, int questionCountPerTypeForAnalysis){
        userResponseSet.addAllResponses(createUserResponseObj(imageTaskResponses,questions,this.userId));
        setPreviousLevel(levelMessageInformation.getCurrentLevel());
        setCurrentLevel(LevelTaskGenerator.calcLevel(calcKnowledgeEstimateByType(questionCountPerTypeForAnalysis)));
    }

    public static List<ResponsesPerQuestion> createUserResponseObj(ImageTaskResponseOOP imageTaskResponses, QuestionPool questions, String userId){
        List<ResponsesPerQuestion> userResponse = new ArrayList<>();
        // TODO why are these declared outside of the for loop
        ResponsesPerQuestion response;
        Question ques;
        for(int i = 0; i< imageTaskResponses.getTaskQuestionIds().size(); i++){
            ques=questions.getQuestionFromId(imageTaskResponses.getTaskQuestionIds().get(i));//finds question in QuestionPool creates a question object
            response=new ResponsesPerQuestion(userId,ques, imageTaskResponses.getResponseTexts().get(i));//creates new response object
            userResponse.add(response);
        }
        return userResponse;
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

    public String getUserId(){
        return userId;
    }

    public void addQuestion(Question q) {
        userQuestionSet.addQuestion(q);
    }

    public UserQuestionSet getUserQuestionSet() {
        return userQuestionSet;
    }

    public int getSeenQuestionCount(){
        return userQuestionSet.getTopLevelSeenQuestions().size();
    }

    public int getUnseenQuestionCount(){
        return userQuestionSet.getTopLevelUnseenQuestions().size();
    }

    public void increaseTimesSeen(String questionId){
        userQuestionSet.increaseTimesSeen(questionId);
    }

    public UserResponseSet getUserResponseSet() {
        return userResponseSet;
    }

    public Map<EquineQuestionTypes, String> calcKnowledgeEstimateStringsByType(int numOfRecentResponsesToConsider){
        return userResponseSet.calcKnowledgeEstimateStringsByType(numOfRecentResponsesToConsider);
    }

    public int getResponseCount(){
        return userResponseSet.getUserResponsesSize();
    }

    public int countTotalResponsesFromUserResponseSet(){
        return userResponseSet.countTotalResponses();
    }

    public double calcKnowledgeEstimate(){
        return userResponseSet.calcKnowledgeEstimate();
    }

    public Map<String,List<QuestionCount>> questionCountsByTypeMap (){
        return userQuestionSet.questionCountsByTypeMap();
    }

    public Map<String,Double> calcKnowledgeEstimateByType(int numOfRecentResponsesToConsider){
        return userResponseSet.calcKnowledgeEstimateByType(numOfRecentResponsesToConsider);
    }

    public int getPreviousLevel() {
        return levelMessageInformation.getPreviousLevel();
    }

    public void setPreviousLevel(int levelIn) {
        levelMessageInformation.setPreviousLevel(levelIn);
    }
    public int getCurrentLevel() {
        return levelMessageInformation.getCurrentLevel();
    }

    public void setCurrentLevel(int levelIn) {
        levelMessageInformation.setCurrentLevel(levelIn);
    }
}

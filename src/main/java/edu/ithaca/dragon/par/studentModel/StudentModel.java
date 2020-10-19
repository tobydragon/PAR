package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Double.parseDouble;

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

    public void imageTaskResponseSubmitted(ImageTaskResponseOOP imageTaskResponses, QuestionPool questions){
        userResponseSet.addAllResponses(createUserResponseObj(imageTaskResponses,questions,this.userId));
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

    /**
     *
     * @return percent of all answers correct, or a negative percent if no answers have been given yet
     */
    public double calcPercentAnswersCorrect(){
        List<ResponsesPerQuestion> responses = getUserResponseSet().getResponsesPerQuestionList();
        if (responses.size() == 0) {
            throw new ArithmeticException("No responses given yet");
        }
        double countRight = 0.0;
        for(ResponsesPerQuestion responseObject: responses){
            for (QuestionResponse response : responseObject.getAllResponses()){
                //if the correct answer and given answer are the same.
                if (response.getResponseText().equalsIgnoreCase(getUserQuestionSet().getQuestionCountFromId(responseObject.getQuestionId()).getQuestion().getCorrectAnswer())){
                    countRight += 1;
                }
            }
        }
        return countRight/getUserResponseSet().getAllResponseCount()*100;
    }


    public List<QuestionCount> calcQuestionsWrong(){
        List<QuestionCount> incorrectQuestions = new ArrayList<>();
        //uses questionCount objects to keep track of all incorrect questions and
        // how many times they've been answered incorrectly. timesseen = timesWrong
        for (ResponsesPerQuestion questionResponse : userResponseSet.getResponsesPerQuestionList()){
            QuestionCount question = new QuestionCount(questionResponse.getQuestion());
            for(QuestionResponse response: questionResponse.getAllResponses()){
                if(!response.getResponseText().equalsIgnoreCase(questionResponse.getQuestion().getCorrectAnswer())){
                    question.increaseTimesSeen();
                }
            }
            if (question.getTimesSeen()>0){
                incorrectQuestions.add(question);
            }
        }
        return incorrectQuestions;
    }


}

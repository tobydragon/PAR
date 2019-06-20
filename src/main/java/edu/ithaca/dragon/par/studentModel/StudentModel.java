package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTaskResponse;

import java.util.ArrayList;
import java.util.List;

public class StudentModel {

    private String userId;
    private UserQuestionSet userQuestionSet;
    private UserResponseSet userResponseSet;

    public StudentModel(String userId, List<Question> questions){
        this.userId = userId;
        this.userQuestionSet = new UserQuestionSet(userId, questions);
        this.userResponseSet = new UserResponseSet(userId);
    }

    public UserQuestionSet getUserQuestionSet() {
        return userQuestionSet;
    }

    public void givenQuestion(String questionId){
        userQuestionSet.givenQuestion(questionId);
    }


    public int getSeenQuestionCount(){
        return userQuestionSet.getLenOfSeenQuestions();
    }

    public int getUnseenQuestionCount(){
        return userQuestionSet.getLenOfUnseenQuestions();
    }

    public int getResponseCount(){
        return userResponseSet.getUserResponsesSize();
    }

    public String getUserId(){
        return userId;
    }

    /**
     * Creates a list of UserResponse objects
     * @param imageTaskResponses: response submitted by the user
     * @param questions: QuestionPool object
     * @param userId: user ID
     * @return: List of UserResponse objects
     */
    private static List<UserResponse> createUserResponseObj(ImageTaskResponse imageTaskResponses, QuestionPool questions,String userId){
        List<UserResponse> userResponses=new ArrayList<>();
        UserResponse response; Question ques;
        for(int i=0;i<imageTaskResponses.getTaskQuestionIds().size();i++){
            ques=questions.getQuestionFromId(imageTaskResponses.getTaskQuestionIds().get(i));//finds question in QuestionPool creates a question object
            response=new UserResponse(userId,ques,imageTaskResponses.getResponseTexts().get(i));//creates new response object
            userResponses.add(response);
        }
        return userResponses;
    }

    /**
     * Adds all responses from the imageTaskResponses to the UserResponseSet
     * @param imageTaskResponses: response submitted by the user
     * @param questions: QuestionPool object
     */
    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponses, QuestionPool questions){
        userResponseSet.addAllResponses(createUserResponseObj(imageTaskResponses,questions,this.userId));
    }
}

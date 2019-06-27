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
//TODO:HAVE SCORE WORK WITH TYPE
    public List<Double> calcScore(){
        return userResponseSet.knowledgeCalc();
    }

    /**
     * Creates a list of ResponsesPerQuestion objects
     * @param imageTaskResponses: response submitted by the user
     * @param questions: QuestionPool object
     * @param userId: user ID
     * @return: List of ResponsesPerQuestion objects
     */
    private static List<ResponsesPerQuestion> createUserResponseObj(ImageTaskResponse imageTaskResponses, QuestionPool questions, String userId){
        List<ResponsesPerQuestion> userRespons =new ArrayList<>();
        ResponsesPerQuestion response; Question ques;
        for(int i=0;i<imageTaskResponses.getTaskQuestionIds().size();i++){
            ques=questions.getQuestionFromId(imageTaskResponses.getTaskQuestionIds().get(i));//finds question in QuestionPool creates a question object
            response=new ResponsesPerQuestion(userId,ques,imageTaskResponses.getResponseTexts().get(i));//creates new response object
            userRespons.add(response);
        }
        return userRespons;
    }

    /**
     * Adds all responses from the imageTaskResponses to the UserResponseSet
     * @param imageTaskResponses: response submitted by the user
     * @param questions: QuestionPool object
     */
    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponses, QuestionPool questions){
        userResponseSet.addAllResponses(createUserResponseObj(imageTaskResponses,questions,this.userId));
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
}

package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;

public class UserResponse {
    private String questionId;
    private String userId;
    private String responseText;
    private Question question;

    public UserResponse(String userIdIn, Question questionIn,String responseIn){
        this.userId=userIdIn;
        this.question=questionIn;
        this.questionId=questionIn.getId();
        this.responseText=responseIn;
    }

    /**
     * checks if user response is correct or not
     * @return true or false
     */
    public boolean checkResponse(){
        if(responseText.equals(question.getCorrectAnswer())){
            return true;
        }
        else {
            return false;
        }
    }

    public String getUserId(){
        return userId;
    }
    public String getQuestionId(){
        return questionId;
    }
}


package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domain.Question;

public class UserResponse {
    private String questionId;
    private String userId;
    private String responseText;
    private Question question;
    // private boolean correct;
//userId probably should only be in UserResponseSet
//**makes sense because a new UserResponseSet should be generated with each student
//or are the questions being asked for a specific userId?

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
        if(responseText==question.getCorrectAnswer()) return true;
        else return false;
    }

    public String getUserId(){
        return userId;
    }
    public String getQuestionId(){
        return questionId;
    }
}


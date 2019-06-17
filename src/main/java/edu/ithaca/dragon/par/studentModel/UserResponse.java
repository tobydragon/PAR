package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTaskResponse;

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

    public void setQuestionId(String questionIdIn){this.questionId=questionIdIn;}
    public String getQuestionId(){
        return questionId;
    }

    public void setUserId(String userIdIn){this.userId=userIdIn;}
    public String getUserId(){
        return userId;
    }

    public void setResponseText(String responseTextIn){this.responseText=responseTextIn;}
    public String getResponseText(){return responseText;}



    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!UserResponse.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        UserResponse other = (UserResponse) otherObj;
        return this.getQuestionId().equals(other.getQuestionId())
                && this.getResponseText().equals(other.getResponseText())
                && this.getUserId().equals(other.getUserId());
    }

}


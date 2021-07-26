package edu.ithaca.dragon.par.comm;

public class StudentResponseAction {
    public String studentId;
    public String questionId;
    public String responseText;
    
    public StudentResponseAction(String studentId, String questionId, String responseText){
        this.studentId = studentId;
        this.questionId = questionId;
        this.responseText = responseText;
    }

    public StudentResponseAction(){}

}

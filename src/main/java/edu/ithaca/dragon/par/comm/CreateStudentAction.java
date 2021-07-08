package edu.ithaca.dragon.par.comm;

public class CreateStudentAction {
    public String studentId;
    public String cohortId;

    public CreateStudentAction(){}
    public CreateStudentAction(String studentId, String cohortId){
        this.studentId = studentId;
        this.cohortId = cohortId;
    }
}

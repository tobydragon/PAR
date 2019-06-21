package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;
import edu.ithaca.dragon.par.studentModel.UserResponseSet;

public class StudentModelRecord {
    private String userId;
    private UserQuestionSetRecord userQuestionSetRecord;
    private UserResponseSetRecord userResponseSetRecord;

    public StudentModelRecord(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserQuestionSetRecord getUserQuestionSetRecord() {
        return userQuestionSetRecord;
    }

    public void setUserQuestionSetRecord(UserQuestionSetRecord userQuestionSetRecord) {
        this.userQuestionSetRecord = userQuestionSetRecord;
    }

    public UserResponseSetRecord getUserResponseSetRecord() {
        return userResponseSetRecord;
    }

    public void setUserResponseSetRecord(UserResponseSetRecord userResponseSetRecord) {
        this.userResponseSetRecord = userResponseSetRecord;
    }

    public StudentModelRecord(StudentModel studentModel){
        this.userId = studentModel.getUserId();
        this.userQuestionSetRecord = new UserQuestionSetRecord(studentModel.getUserQuestionSet());
        this.userResponseSetRecord = new UserResponseSetRecord(studentModel.getUserResponseSet());
    }

    public StudentModel buildStudentModel(QuestionPool questionPool){
        StudentModel studentModel = new StudentModel(userId, userQuestionSetRecord.buildUserQuestionSet(questionPool), userResponseSetRecord.buildUserResponseSet(questionPool));
        return studentModel;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null)
            return false;
        if(!StudentModelRecord.class.isAssignableFrom(otherObj.getClass()))
            return false;
        StudentModelRecord other = (StudentModelRecord) otherObj;
        return this.getUserId().equals(other.getUserId())
                && this.getUserQuestionSetRecord().equals(other.getUserQuestionSetRecord())
                && this.getUserResponseSetRecord().equals(other.getUserResponseSetRecord());
    }
}

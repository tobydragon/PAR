package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

public class StudentData {

    private String studentId;
    private int level;
    private int totalAnswersGiven;
    //private double timeSpent; //minutes?

    /**
     * stores a student's current level, total answers ever given, and their id
     * @param student
     */
    public StudentData(StudentModel student){
        studentId = student.getUserId();
        level = LevelTaskGenerator.calcLevel(student.calcKnowledgeEstimateByType(4));
        //TODO: is there any way to keep the numOfResponsesToConsider in sync with levelTaskGenerator?
        totalAnswersGiven = student.getAllResponseCount();

    }

    /**
     * updates level and total answers given
     * @throws  IllegalArgumentException if the given student model's ID doesn't match that of the student data
     * @param student
     */
    public void updateData(StudentModel student){
        if (!student.getUserId().equals(studentId)){
            throw new IllegalArgumentException("Wrong student model for student data");
        }
        level = LevelTaskGenerator.calcLevel(student.calcKnowledgeEstimateByType(4));
        totalAnswersGiven = student.getAllResponseCount();
    }

    //getters
    public String getStudentId() {
        return studentId;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalAnswersGiven() {
        return totalAnswersGiven;
    }
}

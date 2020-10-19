package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.studentModel.*;

import java.util.ArrayList;
import java.util.List;

public class StudentData {

    private String studentId;
    private int level;
    private int totalAnswersGiven;
    private double percentAnswersCorrect;
    private double percentWrongFirstTime;
    private double percentRightAfterWrongFirstTime;
    private List<QuestionCount> questionsWrong;

//private double timeSpent; //minutes?

    /**
     * stores a student's current level, total answers ever given, a list of questions they got wrong, and their id
     * @param student
     */
    public StudentData(StudentModel student){
        studentId = student.getUserId();
        level = LevelTaskGenerator.calcLevel(student.calcKnowledgeEstimateByType(4));
        //TODO: is there any way to keep the numOfResponsesToConsider in sync with levelTaskGenerator?
        totalAnswersGiven = student.getUserResponseSet().getAllResponseCount();
        percentWrongFirstTime = student.getUserResponseSet().calcPercentWrongFirstTime();
        percentRightAfterWrongFirstTime = student.getUserResponseSet().calcPercentLastAnswerRightAfterWrong();
        questionsWrong = student.calcQuestionsWrong();
        try {
            percentAnswersCorrect = student.calcPercentAnswersCorrect();
        }
        catch(ArithmeticException e){
            percentAnswersCorrect = -1.0;
        }
    }


    /**
     * updates level, total answers given, and the list of incorrect questions
     * @throws  IllegalArgumentException if the given student model's ID doesn't match that of the student data
     * @param student
     */
    public void updateData(StudentModel student){
        if (!student.getUserId().equals(studentId)){
            throw new IllegalArgumentException("Wrong student model for student data");
        }
        level = LevelTaskGenerator.calcLevel(student.calcKnowledgeEstimateByType(4));
        totalAnswersGiven = student.getUserResponseSet().getAllResponseCount();
        percentRightAfterWrongFirstTime = student.getUserResponseSet().calcPercentLastAnswerRightAfterWrong();
        percentWrongFirstTime = student.getUserResponseSet().calcPercentWrongFirstTime();
        questionsWrong = student.calcQuestionsWrong();
        try {
            percentAnswersCorrect = student.calcPercentAnswersCorrect();
        }
        catch(ArithmeticException e){
            percentAnswersCorrect = -1.0;
        }
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

    public double getPercentAnswersCorrect(){
        return percentAnswersCorrect;
    }

    public double getPercentWrongFirstTime() {
        return percentWrongFirstTime;
    }

    public double getPercentRightAfterWrongFirstTime() {
        return percentRightAfterWrongFirstTime;
    }

    public List<QuestionCount> getQuestionsWrong () { return questionsWrong; }
}

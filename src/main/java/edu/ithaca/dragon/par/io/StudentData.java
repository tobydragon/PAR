package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.studentModel.QuestionResponse;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.UserResponseSet;

import java.util.List;

public class StudentData {

    private String studentId;
    private int level;
    private int totalAnswersGiven;
    private double percentAnswersCorrect;
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
        try {
            percentAnswersCorrect = calcPercentAnswersCorrect(student);
        }
        catch(ArithmeticException e){
            percentAnswersCorrect = -1.0;
        }
    }

    /**
     *
     * @return percent of all answers correct, or a negative percent if no answers have been given yet
     */
    private double calcPercentAnswersCorrect(StudentModel studentModel){
        List<ResponsesPerQuestion> responses = studentModel.getUserResponseSet().getResponsesPerQuestionList();
        if (responses.size() == 0) {
            throw new ArithmeticException("No responses given yet");
        }
        double countRight = 0.0;
        for(ResponsesPerQuestion responseObject: responses){
            for (QuestionResponse response : responseObject.getAllResponses()){
                //if the correct answer and given answer are the same.
                if (response.getResponseText().equalsIgnoreCase(studentModel.getUserQuestionSet().getQuestionCountFromId(responseObject.getQuestionId()).getQuestion().getCorrectAnswer())){
                    countRight += 1;
                }
            }
        }
        return countRight/studentModel.getAllResponseCount()*100;
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
        try {
            percentAnswersCorrect = calcPercentAnswersCorrect(student);
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
}

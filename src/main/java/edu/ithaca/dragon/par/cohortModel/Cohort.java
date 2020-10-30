package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;

public class Cohort {
    private final TaskGenerator taskGenerator;
    private final List<String> studentIDs;
    private final MessageGenerator messageGenerator;
    private final QuestionPool questionPool;

    public Cohort(TaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator, QuestionPool questionPool){
        this.taskGenerator = taskGenerator;
        this.studentIDs = studentIDs;
        this.messageGenerator = messageGenerator;
        this.questionPool = questionPool;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    public TaskGenerator getTaskGenerator() {
        return taskGenerator;
    }

    public MessageGenerator getMessageGenerator(){ return messageGenerator; }

    public QuestionPool getQuestionPool(){ return questionPool; }

    public void addStudent(String studentID) {
        if(!studentIDs.contains(studentID)){
            studentIDs.add(studentID);
        }
    }

}

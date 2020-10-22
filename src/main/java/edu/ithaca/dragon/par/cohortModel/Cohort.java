package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;

public class Cohort {
    private final TaskGenerator taskGenerator;
    private final List<String> studentIDs;
    private final MessageGenerator messageGenerator;

    public Cohort(TaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator){
        this.taskGenerator = taskGenerator;
        this.studentIDs = studentIDs;
        this.messageGenerator = messageGenerator;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    public TaskGenerator getTaskGenerator() {
        return taskGenerator;
    }

    public MessageGenerator getMessageGenerator(){ return messageGenerator; }

    public void addStudent(String studentID) {
        if(!studentIDs.contains(studentID)){
            studentIDs.add(studentID);
        }
    }

}

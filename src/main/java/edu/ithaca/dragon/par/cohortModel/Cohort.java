package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.List;

public class Cohort {
    private TaskGenerator taskGenerator;
    private MessageGenerator messageGenerator;
    private List<StudentModel> students;

    //TODO should a MessageGenerator be held if it is just a utility class?
    public Cohort(TaskGenerator taskGenerator, MessageGenerator messageGenerator, List<StudentModel> students){
        this.taskGenerator =  taskGenerator;
        this.students = students;
        this.messageGenerator = messageGenerator;
    }

    public void setStudents(List<StudentModel> students) {
        this.students = students;
    }

    public List<StudentModel> getStudents() {
        return students;
    }

    public void setTaskGenerator(TaskGenerator taskGenerator) {
        this.taskGenerator = taskGenerator;
    }

    public TaskGenerator getTaskGenerator() {
        return taskGenerator;
    }

    public void setMessageGenerator(MessageGenerator messageGenerator) {
        this.messageGenerator = messageGenerator;
    }

    public MessageGenerator getMessageGenerator() {
        return messageGenerator;
    }
}

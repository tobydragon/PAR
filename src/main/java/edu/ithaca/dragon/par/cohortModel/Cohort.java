package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.List;

public class Cohort {
    private TaskGenerator taskGenerator;
    private List<String> studentIDs;

    public Cohort(TaskGenerator taskGenerator, List<String> studentIDs){
        this.taskGenerator =  taskGenerator;
        this.studentIDs = studentIDs;
    }

    public void setStudents(List<String> studentIDs) {
        this.studentIDs = studentIDs;
    }

    public List<String> getStudents() {
        return studentIDs;
    }

    public void setTaskGenerator(TaskGenerator taskGenerator) {
        this.taskGenerator = taskGenerator;
    }

    public TaskGenerator getTaskGenerator() {
        return taskGenerator;
    }
}

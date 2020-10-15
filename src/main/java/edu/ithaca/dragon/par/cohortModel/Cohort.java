package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.List;

public class Cohort {
    private TaskGenerator taskGenerator;
    private List<StudentModel> students;

    public Cohort(TaskGenerator taskGenerator, List<StudentModel> students){
        this.taskGenerator =  taskGenerator;
        this.students = students;
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
}

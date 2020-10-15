package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.ArrayList;
import java.util.List;

public class Cohort {
    private TaskGenerator taskGenerator;
    private List<String> studentIDs;

    public Cohort(TaskGenerator taskGenerator){
        this.taskGenerator =  taskGenerator;
        this.studentIDs = new ArrayList<>();
    }

    public Cohort(TaskGenerator taskGenerator, List<String> studentIDs){
        this.taskGenerator = taskGenerator;
        this.studentIDs = studentIDs;
    }

    //TODO add student, remove student, delete setStudents, delete setTaskGenerator
    public void setStudents(List<String> studentIDs) {
        this.studentIDs = studentIDs;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    public void setTaskGenerator(TaskGenerator taskGenerator) {
        this.taskGenerator = taskGenerator;
    }

    public TaskGenerator getTaskGenerator() {
        return taskGenerator;
    }

    public void addStudent(String studentID) {
        if(!studentIDs.contains(studentID)){
            studentIDs.add(studentID);
        }
    }
}

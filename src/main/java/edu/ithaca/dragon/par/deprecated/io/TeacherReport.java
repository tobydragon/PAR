package edu.ithaca.dragon.par.deprecated.io;

import java.util.ArrayList;
import java.util.List;

public class TeacherReport {
    private List<StudentReport> studentReports;

    public TeacherReport(){
        this.studentReports=new ArrayList<>();
    }

    public TeacherReport(List<StudentReport> studentReportsIn){
        this.studentReports=studentReportsIn;
    }

    public List<StudentReport> getStudentReports() {
        return studentReports;
    }

    public void setStudentReports(List<StudentReport> studentReports) {
        this.studentReports = studentReports;
    }
}

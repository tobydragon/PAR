package edu.ithaca.dragon.par.io;

import java.util.BitSet;
import java.util.List;

public class StudentDataAnalyzer {
    List<StudentData> studentDataList;
    public StudentDataAnalyzer(List<StudentData> studentDataListIn){
        studentDataList = studentDataListIn;
    }

    public void addStudentData(StudentData sdToAdd){
        if (sdToAdd == null){
            throw new IllegalArgumentException("Student data is null");
        }
        studentDataList.add(sdToAdd);
    }

    public List<StudentData> getStudentDataList() {
        return studentDataList;
    }
}

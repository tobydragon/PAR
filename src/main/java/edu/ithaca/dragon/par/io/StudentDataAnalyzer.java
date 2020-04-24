package edu.ithaca.dragon.par.io;

import java.util.BitSet;
import java.util.List;

public class StudentDataAnalyzer {
    List<StudentData> studentDataList;
    public StudentDataAnalyzer(List<StudentData> studentDataListIn){
        studentDataList = studentDataListIn;
    }

    public List<StudentData> getStudentDataList() {
        return studentDataList;
    }
}

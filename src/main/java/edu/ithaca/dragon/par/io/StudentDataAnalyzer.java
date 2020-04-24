package edu.ithaca.dragon.par.io;

import java.util.List;

public class StudentDataAnalyzer {
    List<StudentData> studentDataList;

    /**
     * creates StudentDataAnalyzer object
     * @param studentDataListIn
     */
    public StudentDataAnalyzer(List<StudentData> studentDataListIn){
        studentDataList = studentDataListIn;
    }

    /**
     * Adds StudentData object to studentDataList
     * @throws IllegalArgumentException if the StudentData object is null
     * @param sdToAdd
     */
    public void addStudentData(StudentData sdToAdd){
        if (sdToAdd == null){
            throw new IllegalArgumentException("Student data is null");
        }
        studentDataList.add(sdToAdd);
    }

    /**
     * Gets student from studentDataList
     * @throws IllegalArgumentException if studentId doesn't match any of the StudentData objects in the list
     * @param studentId
     * @return student data object
     */
    public StudentData getStudentData(String studentId){
        for (StudentData studentData : studentDataList){
            if (studentData.getStudentId().equals(studentId)){
                return studentData;
            }
        }
        throw new IllegalArgumentException("Student data not found");
    }

    /**
     * Removes student from studentDataList
     * @throws IllegalArgumentException if studentId doesn't match any of the StudentData objects in the list
     * @param studentId
     */
    public void removeStudentData(String studentId) throws IllegalArgumentException{
        studentDataList.remove(getStudentData(studentId));
    }

    /**
     * @throws ArithmeticException if list is empty (because getting an average of 0 numbers is impossible)
     * @return average level across all students
     */
    public double calcAverageLevel(){
        if (studentDataList.size() == 0){
            throw new ArithmeticException("No student data found, cannot calculate average level");
        }
        else{
            double average = 0.0;
            for (StudentData studentData : studentDataList){
                average += studentData.getLevel();
            }
            return average/studentDataList.size();
        }
    }

    //calculate all students average totalAnswersGiven
    /**
     * @throws ArithmeticException if list is empty (because getting an average of 0 numbers is impossible)
     * @return average totalAnswersGiven across all students
     */
    public double calcAverageTotalAnswersGiven(){
        if (studentDataList.size() == 0){
            throw new ArithmeticException("No student data found, cannot calculate average total answers given");
        }
        else{
            double average = 0.0;
            for (StudentData studentData : studentDataList){
                average += studentData.getTotalAnswersGiven();
            }
            return average/studentDataList.size();
        }
    }

    //calculate average totalAnswersGiven given level


    public List<StudentData> getStudentDataList() {
        return studentDataList;
    }
}

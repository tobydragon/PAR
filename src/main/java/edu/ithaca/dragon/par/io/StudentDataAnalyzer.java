package edu.ithaca.dragon.par.io;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

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
    public double calcAverageTotalAnswers(){
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

    /**
     * @throws ArithmeticException if list is empty(because getting an average of 0 numbers is impossible)
     * @throws ArithmeticException if no StudentData of that level exists
     * @return average totalAnswersGiven across all students
     */
    public double calcAverageTotalAnswersGivenLevel(int level){
        if (studentDataList.size() == 0){
            throw new ArithmeticException("No student data found, cannot calculate average total answers given");
        }
        if (getListGivenLevel(level).size() == 0){
            throw new IllegalArgumentException("No student data found for this level, cannot calculate average total answers given");
        }
        else{
            double average = 0.0;
            int numOfStudentData = 0;
            for (StudentData studentData : studentDataList){
                if (studentData.getLevel() == level) {
                    average += studentData.getTotalAnswersGiven();
                    numOfStudentData += 1;
                }
            }
            return average/numOfStudentData;
        }
    }

    /**
     * @throws IllegalArgumentException if there are no StudentData objects of the given level
     * @return list of all StudentData objects of a certain level
     */

    public List<StudentData> getListGivenLevel(int level) {
        List<StudentData> studentsOfGivenLevel = new ArrayList<>();
        for (StudentData studentData : studentDataList){
            if (studentData.getLevel() == level) {
                studentsOfGivenLevel.add(studentData);
            }
        }
        if (studentsOfGivenLevel.size() == 0){
            throw new IllegalArgumentException("No students of given level");
        }
        return studentsOfGivenLevel;
    }

    public List<StudentData> getStudentDataList() {
        return studentDataList;
    }


    public void writeStudentDataFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = { "Student Id", "Level", "Total Answers Given", "Percent of Correct Responses", "Percent of Questions Wrong the First Time", "Percent of Questions Right After Wrong the First Time"};
            writer.writeNext(header);

            //inserts row per student
            for (StudentData studentData : studentDataList){
                String [] studentRow = {studentData.getStudentId(), Integer.toString(studentData.getLevel()), Integer.toString(studentData.getTotalAnswersGiven()), Double.toString(studentData.getPercentAnswersCorrect()), Double.toString(studentData.getPercentWrongFirstTime()), Double.toString(studentData.getPercentRightAfterWrongFirstTime())};
                writer.writeNext(studentRow);
            }
            String [] divider = {""};
            writer.writeNext(divider);

            String [] overallStats = {"Averages:", Double.toString(calcAverageLevel()), Double.toString(calcAverageTotalAnswers()), "-3", "-2", "2653863"};
            writer.writeNext(overallStats);

            //close writer
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

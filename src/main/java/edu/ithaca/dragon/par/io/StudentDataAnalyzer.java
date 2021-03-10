package edu.ithaca.dragon.par.io;

import com.opencsv.CSVWriter;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentDataAnalyzer {
    List<StudentData> studentDataList;
    double averageLevel;
    double averageTotalAnswers;
    double averagePercentCorrectResponses;
    double averagePercentWrongFirstTime;
    double averagePercentRightAfterWrongFirstTime;
    List<QuestionCount> mostIncorrectQuestions;




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
     * Updates the student data analyzer to hold the most updated statistics
     */
    public void calcStatistics(){
        averageLevel = calcAverageLevel();
        averageTotalAnswers = calcAverageTotalAnswers();
        averagePercentCorrectResponses = calcAveragePercentCorrectResponses();
        averagePercentWrongFirstTime = calcAveragePercentWrongFirstTime();
        averagePercentRightAfterWrongFirstTime = calcAveragePercentRightAfterWrongFirstTime();
        mostIncorrectQuestions = findMostIncorrectQuestions(5);
        //TODO: who decides how many questions to find? Is this like the window size of 4?
        // Or should it be up to the instructor?
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
     * @return average totalAnswersGiven across all students of a certain level
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

    /**
     * @throws ArithmeticException if there are no StudentData objects
     * @return average percent students have correctly answered questions
     */
    public Double calcAveragePercentCorrectResponses(){
        if (studentDataList.size() == 0){
            throw new ArithmeticException("No student data found, cannot calculate average total answers given");
        }
        else{
            double average = 0.0;
            for (StudentData studentData : studentDataList) {
                //student actually has responses
                if (studentData.getPercentAnswersCorrect() >= 0) {
                    average += studentData.getPercentAnswersCorrect();
                }
            }
            return average/studentDataList.size();
        }
    }

    /**
     * @throws ArithmeticException if there are no studentData objects
     * @return percent of the time that students answered the question incorrectly on their first try
     */
    public Double calcAveragePercentWrongFirstTime(){
        if (studentDataList.size() == 0){
            throw new ArithmeticException("No student data found, cannot calculate average total answers given");
        }
        else{
            double average = 0.0;
            for (StudentData studentData : studentDataList){
                if (studentData.getPercentAnswersCorrect() >= 0) {
                    average += studentData.getPercentWrongFirstTime();
                }
            }
            return average/studentDataList.size();
        }
    }

    /**
     * @throws ArithmeticException if there are no studentData objects
     * @return percent of time that questions were answered correctly after initially getting it wrong
     */
    public Double calcAveragePercentRightAfterWrongFirstTime(){
        if (studentDataList.size() == 0){
            throw new ArithmeticException("No student data found, cannot calculate average total answers given");
        }
        else{
            double average = 0.0;
            for (StudentData studentData : studentDataList){
                if (studentData.getPercentAnswersCorrect() >= 0) {
                    average += studentData.getPercentRightAfterWrongFirstTime();
                }
            }
            return average/studentDataList.size();
        }
    }

    /**
     * @return a list of the top incorrect questions and the number of times it was answered incorrectly.
     */
    public List<QuestionCount> findMostIncorrectQuestions(int numOfQuestions){
        if(numOfQuestions<0){
            throw new IllegalArgumentException("Cannot generate a negative number of incorrect questions");
        }


        List<QuestionCount> consolidatedList = new ArrayList<>();
        for(StudentData sd: studentDataList){
            for (QuestionCount qcStudent: sd.getQuestionsWrong()){
                boolean found = false;
                for (QuestionCount qcConsolidated: consolidatedList){
                    if (qcStudent.getQuestion().getId().equals(qcConsolidated.getQuestion().getId())){
                        found = true;
                        qcConsolidated.setTimesAttempted(qcConsolidated.getTimesAttempted()+qcStudent.getTimesAttempted());
                    }
                }
                if(!found){
                    consolidatedList.add(qcStudent);
                }
            }
        }

        if(numOfQuestions>consolidatedList.size()){
            return consolidatedList; //already small enough.
        }


        List<QuestionCount> topQCs = new ArrayList<>();
        for(QuestionCount currQC: consolidatedList){
            topQCs.add(currQC);
            if(topQCs.size()>numOfQuestions){
                int lowestIndex = 0;
                int currIndex = 0;
                int lowestAttempted = topQCs.get(0).getTimesAttempted();
                for(QuestionCount currTopQC: topQCs){
                    if (currTopQC.getTimesAttempted()<lowestAttempted){
                        lowestAttempted = currTopQC.getTimesAttempted();
                        lowestIndex = currIndex;
                    }

                    currIndex++;
                }
                topQCs.remove(lowestIndex);
            }
        }
        return topQCs;
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

            String [] overallStats = {"Averages:", Double.toString(calcAverageLevel()), Double.toString(calcAverageTotalAnswers()), Double.toString(calcAveragePercentCorrectResponses()), Double.toString(calcAveragePercentWrongFirstTime()), Double.toString(calcAveragePercentRightAfterWrongFirstTime())};
            writer.writeNext(overallStats);
            writer.writeNext(divider);

            String [] note = {"-1 denotes no information for calculation. Student still included in average calculation"};
            writer.writeNext(note);

            writer.writeNext(divider);
            writer.writeNext(divider);
            writer.writeNext(divider);
            String [] header2 = {"Top Incorrect Questions- ID", "Top Incorrect Questions- Question Text", "Number of Incorrect Responses"};
            boolean works = false;
            int numOfQuestions = 10;
            List<QuestionCount> qc = new ArrayList<>();
            while (!works){
                try{
                    qc = findMostIncorrectQuestions(5);
                    works = true;
                }
                catch (Exception e){
                    numOfQuestions-=1;
                    if (numOfQuestions==0){
                        works = true;
                    }
                }
            }
            writer.writeNext(header2);
            for(QuestionCount currQC: qc){
                String [] currLine = {currQC.getQuestion().getId(), currQC.getQuestion().getQuestionText(), Integer.toString(currQC.getTimesAttempted())};
                writer.writeNext(currLine);
            }

            if (qc.size()==0){
                String[] line = {"Zero questions have been answered incorrectly so far"};
                writer.writeNext(line);
            }

            //close writer
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        StudentDataAnalyzer sda = new StudentDataAnalyzer(new ArrayList<>());

        //change the QP to be the QP of the student files.
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());


        //replace folder path here
        File dir = new File("src/test/resources/author/sda_practice/");
        String[] pathnames = dir.list();
        for (String pathname : pathnames) {
            //replace folder path here
            StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/sda_practice/" + pathname, StudentModelRecord.class);
            StudentModel student = smr.buildStudentModel(myQP);
            StudentData sd = new StudentData(student);
            sda.addStudentData(sd);
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("What would you like your filename to be?");
        String fileName = scan.nextLine();
        if(!fileName.contains(".csv")){
            fileName = fileName + ".csv";
        }
        sda.writeStudentDataFile(fileName);

        //TODO: try catch for topIncorrectQuestions
        // return as many as available
        // use on cohort

    }

    public double getAverageLevel() {
        return averageLevel;
    }

    public double getAverageTotalAnswers() {
        return averageTotalAnswers;
    }

    public double getAveragePercentCorrectResponses() {
        return averagePercentCorrectResponses;
    }

    public double getAveragePercentWrongFirstTime() {
        return averagePercentWrongFirstTime;
    }

    public double getAveragePercentRightAfterWrongFirstTime() {
        return averagePercentRightAfterWrongFirstTime;
    }

    public List<QuestionCount> getMostIncorrectQuestions() {
        return mostIncorrectQuestions;
    }

}

package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParServer {

    private QuestionPool questionPool;
    private Map<String, StudentModel> studentModelMap;
    private Datastore datastore;

    public ParServer(Datastore datastore) throws IOException{
        this.questionPool = new QuestionPool(datastore);
        this.datastore = datastore;
        studentModelMap = new HashMap<>();
    }

    public ImageTask nextImageTask(String userId) throws IOException{
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        ImageTask imageTask =  TaskGenerator.makeTask(currentStudent);

        if (imageTask != null){
            return imageTask;
        }
        else {
            throw new RuntimeException("No image task given for userId:" + userId);
        }
    }

    public ImageTask nextImageTaskSingle(String userId) throws IOException{
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        Question initialQuestion = TaskGenerator.getInitialQuestionForTask(currentStudent);
        ImageTask imageTask =  new ImageTask(initialQuestion.getImageUrl(), Arrays.asList(initialQuestion));
        if (imageTask != null){
            return imageTask;
        }
        else {
            throw new RuntimeException("No image task given for userId:" + userId);
        }
    }


    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse, String userId) throws IOException{
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        currentStudent.imageTaskResponseSubmitted(imageTaskResponse,questionPool);

        //save this response to file
        datastore.saveStudentModel(studentModelMap.get(userId));
    }


    public double calcScore(String userId) throws IOException{
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        return  currentStudent.calcScore();
    }

    public static String sendNewImageTaskResponse(ImageTaskResponse response) throws IOException{
        //update the data store with the new imageTaskResponse
        return null;
    }

    //Side effect: if a new model is created, it is added to the given studentModelMap

    /**
     *
     * @param studentModelMap
     * @param userId
     * @return a StudentModel corresponding to the given userId that is also in the studentModelMap
     * @post if there was no corresponding StudentModel, a new on will be created and added to the map
     */
    public static StudentModel getOrCreateStudentModel(Map<String, StudentModel> studentModelMap, String userId, Datastore datastore) throws IOException{
        //try to find the student in the map
        StudentModel studentModel = studentModelMap.get(userId);

        //if the student wasn't in the map, try to load from file
        if (studentModel == null){
            studentModel = datastore.loadStudentModel(userId);
        }

        //the student didn't have a file, create a new student
        if(studentModel == null){
            studentModel = new StudentModel(userId, datastore.loadQuestions());

        }
        //add the student to the map
        studentModelMap.put(userId, studentModel);

        return studentModel;
    }

    public void logout(String userId){
        studentModelMap.remove(userId);
    }
}

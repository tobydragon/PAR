package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.pedagogicalModel.ImageTaskChooser;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParServer {

    private QuestionPool questionPool;
    private Map<String, StudentModel> studentModelMap;

    public ParServer(QuestionPool questionPool){
        this.questionPool = questionPool;
        studentModelMap = new HashMap<>();
    }

    public ImageTask nextImageTask(String userId){
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, questionPool);
        ImageTask imageTask =  ImageTaskChooser.nextImageTaskSingle(currentStudent.getUserQuestionSet(), 1);
        if (imageTask != null){
            return imageTask;
        }
        else {
            throw new RuntimeException("No image task given for userId:" + userId);
        }
    }

    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse){

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
    public static StudentModel getOrCreateStudentModel(Map<String, StudentModel> studentModelMap, String userId, QuestionPool questionPool){
        StudentModel studentModel = studentModelMap.get(userId);
        if (studentModel == null){
            studentModel = new StudentModel(userId, questionPool.getAllQuestions());
            studentModelMap.put(userId, studentModel);
        }
        return studentModel;
    }
}

package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.StudentModelDatastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.UserResponseSet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParServer {

    protected QuestionPool questionPool;
    private StudentModelDatastore studentModelDatastore;
    private Integer windowSizeOverride;

    public ParServer(StudentModelDatastore studentModelDatastore) throws IOException {
        this.questionPool = new QuestionPool(studentModelDatastore.getAllQuestions());
        this.studentModelDatastore = studentModelDatastore;
        this.windowSizeOverride = null;

        checkIfWindowSizeIsValid(questionPool);
    }

    public ParServer(StudentModelDatastore studentModelDatastore, int windowSizeOverride) throws IOException {
        this.questionPool = new QuestionPool(studentModelDatastore.getAllQuestions());
        this.studentModelDatastore = studentModelDatastore;
        this.windowSizeOverride = windowSizeOverride;

        checkIfWindowSizeIsValid(questionPool);
    }

    //side effects: generating an image task will  mark previously unseen questions as seen as well as
    //increase the number of times seen for already seen questions. marking unseen questions as seen involves
    //moving an unseen question from an unseen list to a seen list as well as adding an integer (1) to the times
    //seen arraylist.
    public ImageTask nextImageTask(String userId) throws IOException {
        StudentModel currentStudent = studentModelDatastore.getStudentModel(userId);
        ImageTask imageTask = TaskGenerator.makeTask(currentStudent);

        if (imageTask != null) {
            return imageTask;
        } else {
            throw new RuntimeException("No image task given for userId:" + userId);
        }
    }

    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse, String userId) throws IOException {
        StudentModel currentStudent = studentModelDatastore.getStudentModel(userId);
        currentStudent.imageTaskResponseSubmitted(imageTaskResponse, questionPool);

        studentModelDatastore.imageTaskResponseSubmitted(currentStudent, imageTaskResponse);
    }

    public double calcScore(String userId) throws IOException {
        StudentModel currentStudent = studentModelDatastore.getStudentModel(userId);
        return currentStudent.knowledgeScore();
    }

    public static String sendNewImageTaskResponse(ImageTaskResponse response) throws IOException {
        //update the data store with the new imageTaskResponse
        return null;
    }

    public Map<String, Double> calcScoreByType(String userId) throws IOException {
        StudentModel currentStudent = studentModelDatastore.getStudentModel(userId);
        return currentStudent.knowledgeScoreByType();
    }

    public Map<EquineQuestionTypes, String> knowledgeBaseEstimate(String userId) throws IOException {
        StudentModel currentStudent = studentModelDatastore.getStudentModel(userId);
        return currentStudent.generateKnowledgeBaseMap();
    }

    public void logout(String userId) throws IOException{
        studentModelDatastore.logout(userId);
    }

    public void checkIfWindowSizeIsValid(QuestionPool questionPool) {
        if (windowSizeOverride == null) {
            if (!questionPool.checkWindowSize(UserResponseSet.windowSize)) {
                throw new RuntimeException("The windownSize is too small for the given questionPool");
            }
        } else {
            if (!questionPool.checkWindowSize(windowSizeOverride)) {
                throw new RuntimeException("The windownSize (Overriden) is too small for the given questionPool");
            }
        }
    }
}


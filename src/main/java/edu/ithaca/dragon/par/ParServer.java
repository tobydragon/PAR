package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.UserResponseSet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParServer {

    protected QuestionPool questionPool;
    private Map<String, StudentModel> studentModelMap;
    private Datastore datastore;
    private Integer windowSizeOverride;

    public ParServer(Datastore datastore) throws IOException {
        this.questionPool = new QuestionPool(datastore.loadQuestions());
        this.datastore = datastore;
        studentModelMap = new HashMap<>();
        this.windowSizeOverride = null;

        checkIfWindowSizeIsValid(questionPool);
    }

    public ParServer(Datastore datastore, int windowSizeOverride) throws IOException {
        this.questionPool = new QuestionPool(datastore.loadQuestions());
        this.datastore = datastore;
        studentModelMap = new HashMap<>();
        this.windowSizeOverride = windowSizeOverride;

        checkIfWindowSizeIsValid(questionPool);
    }

    //side effects: generating an image task will  mark previously unseen questions as seen as well as
    //increase the number of times seen for already seen questions. marking unseen questions as seen involves
    //moving an unseen question from an unseen list to a seen list as well as adding an integer (1) to the times
    //seen arraylist.
    public ImageTask nextImageTask(String userId) throws IOException {
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        ImageTask imageTask = TaskGenerator.makeTask(currentStudent);

        if (imageTask != null) {
            return imageTask;
        } else {
            throw new RuntimeException("No image task given for userId:" + userId);
        }
    }

    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse, String userId) throws IOException {
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        currentStudent.imageTaskResponseSubmitted(imageTaskResponse, questionPool);

        //save this response to file
        datastore.saveStudentModel(studentModelMap.get(userId));
    }

    public double calcScore(String userId) throws IOException {
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        return currentStudent.knowledgeScore();
    }

    public static String sendNewImageTaskResponse(ImageTaskResponse response) throws IOException {
        //update the data store with the new imageTaskResponse
        return null;
    }

    //Side effect: if a new model is created, it is added to the given studentModelMap
    /**
     * @param studentModelMap
     * @param userId
     * @return a StudentModel corresponding to the given userId that is also in the studentModelMap
     * @post if there was no corresponding StudentModel, a new on will be created and added to the map
     */
    public static StudentModel getOrCreateStudentModel(Map<String, StudentModel> studentModelMap, String userId, Datastore datastore) throws IOException {
        //try to find the student in the map
        StudentModel studentModel = studentModelMap.get(userId);

        //if the student wasn't in the map, try to load from file
        if (studentModel == null) {
            studentModel = datastore.loadStudentModel(userId);
        }

        //the student didn't have a file, create a new student
        if (studentModel == null) {
            studentModel = new StudentModel(userId, datastore.loadQuestions());

            //if the student didn't have a file, create a new student
            if (studentModel == null) {
                studentModel = new StudentModel(userId, datastore.loadQuestions());
            }
            //add the student to the map
            studentModelMap.put(userId, studentModel);
        }
        return studentModel;
    }

    public void logout(String userId) {
        studentModelMap.remove(userId);
    }

    public Map<String, Double> calcScoreByType(String userId) throws IOException {
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        return currentStudent.knowledgeScoreByType();
    }

    public Map<EquineQuestionTypes, String> knowledgeBaseEstimate(String userId) throws IOException {
        StudentModel currentStudent = getOrCreateStudentModel(studentModelMap, userId, datastore);
        return currentStudent.generateKnowledgeBaseMap();
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


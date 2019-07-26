package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.Question;
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
import java.util.List;
import java.util.Map;

public class ParServer {

    private StudentModelDatastore studentModelDatastore;

    public ParServer(StudentModelDatastore studentModelDatastore) {
        this.studentModelDatastore = studentModelDatastore;
    }

    //side effects: generating an image task will  mark previously unseen questions as seen as well as
    //increase the number of times seen for already seen questions.
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
        studentModelDatastore.imageTaskResponseSubmitted(userId, imageTaskResponse);
    }

    public double calcScore(String userId) throws IOException {
        StudentModel currentStudent = studentModelDatastore.getStudentModel(userId);
        return currentStudent.knowledgeScore();
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

    public void addQuestions(List<Question> questions) throws IOException{
        studentModelDatastore.addQuestions(questions);
    }
}


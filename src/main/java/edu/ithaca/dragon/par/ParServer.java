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
    private ImageTaskChooser imageTaskChooser;
    private Map<String, StudentModel> studentModelMap;

    ParServer(QuestionPool questionPool, ImageTaskChooser imageTaskChooser){
        this.questionPool = questionPool;
        this.imageTaskChooser = imageTaskChooser;
        studentModelMap = new HashMap<>();
    }

    public ImageTask nextImageTask(String userId){
        return null;
    }

    public void imageTaskResponseSubmitted(ImageTaskResponse imageTaskResponse){

    }

    public static String sendNewImageTaskResponse(ImageTaskResponse response) throws IOException{
        //update the data store with the new imageTaskResponse
        return null;
    }

    public static StudentModel getOrCreateStudentModel(String userId){
        return null;
    }
}

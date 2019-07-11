package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JsonSpringDatastore implements Datastore {

    public JsonSpringDatastore (String currentQuestionPoolLocation, String defaultQuestionPoolLocation, String currentStudentModelDir){

    }

    @Override
    public List<Question> loadQuestions() throws IOException {
        return null;
    }

    @Override
    public List<StudentModel> loadStudentModels() throws IOException {
        return null;
    }

    @Override
    public StudentModel loadStudentModel(String userId) throws IOException {
        return null;
    }

    @Override
    public void saveStudentModels(Collection<StudentModel> studentModelsIn) throws IOException {

    }

    @Override
    public void saveStudentModel(StudentModel studentModel) throws IOException {

    }
}

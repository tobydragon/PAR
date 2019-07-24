package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface StudentModelDatastore {

    List<Question> getAllQuestions() throws IOException;

    StudentModel getStudentModel(String userId) throws IOException;

    public void addQuestions(List<Question> questions) throws IOException;

    void imageTaskResponseSubmitted(String userId, ImageTaskResponse imageTaskResponse) throws IOException;

    void logout(String userId) throws IOException;
}

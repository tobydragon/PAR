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

    void saveStudentModels(Collection<StudentModel> studentModelsIn) throws IOException;

    void saveStudentModel(StudentModel studentModel) throws IOException;


}

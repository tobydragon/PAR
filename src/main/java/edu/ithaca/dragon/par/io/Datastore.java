package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface Datastore {

    List<Question> loadQuestions() throws IOException;

    List<StudentModel> loadStudentModels() throws IOException;

    void saveStudentModels(Collection<StudentModel> studentModelsIn) throws IOException;

    void setStudentModelFilePath(String newStudentModelFilePath);

    void setQuestionFilePath(String newQuestionFilePath);

    void saveStudentModel(StudentModel studentModel) throws IOException;
}

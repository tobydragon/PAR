package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CohortTest {

    @Test
    public void constructorTest() throws IOException {
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        //instantiate list of student models
        List<Question> questionList = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions()).getAllQuestions();
        StudentModel testStudent1 = new StudentModel("testStudent1", questionList);
        StudentModel testStudent2 = new StudentModel("testStudent1", questionList);
        StudentModel testStudent3 = new StudentModel("testStudent1", questionList);
        List<StudentModel> studentList = new ArrayList<>();

        studentList.add(testStudent1);
        studentList.add(testStudent2);
        studentList.add(testStudent3);

        Cohort cohort = new Cohort(taskGenerator, studentList);

        assertEquals(cohort.getTaskGenerator(), taskGenerator);
        assertEquals(cohort.getStudents(), studentList);
    }
}

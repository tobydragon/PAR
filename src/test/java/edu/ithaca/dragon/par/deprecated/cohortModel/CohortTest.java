package edu.ithaca.dragon.par.deprecated.cohortModel;

import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.deprecated.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.deprecated.pedagogicalModel.LevelMessageGenerator;
import edu.ithaca.dragon.par.deprecated.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.deprecated.pedagogicalModel.TaskGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CohortTest {

    @Test
    public void constructorTest() throws IOException {
        //task generator for both types
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());


        //instantiate list of students
        List<String> studentList = new ArrayList<>();
        studentList.add("testStudent1");
        studentList.add("testStudent2");
        studentList.add("testStudent3");

        //construct a cohort with studentIDs when created
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json").getAllQuestions());
        Cohort cohort = new Cohort("c1", taskGenerator, studentList, new LevelMessageGenerator(), questionPool);

        assertEquals("c1", cohort.getCohortId());
        assertEquals(taskGenerator, cohort.getTaskGenerator());
        assertEquals(studentList, cohort.getStudentIDs());
        assertEquals(questionPool, cohort.getQuestionPool());
    }
}

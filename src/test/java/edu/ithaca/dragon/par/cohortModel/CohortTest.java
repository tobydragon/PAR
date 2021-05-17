package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        Cohort cohort = new Cohort("c1", taskGenerator, studentList, new LevelMessageGenerator(), questionPool);

        assertEquals("c1", cohort.getCohortId());
        assertEquals(taskGenerator, cohort.getTaskGenerator());
        assertEquals(studentList, cohort.getStudentIDs());
        assertEquals(questionPool, cohort.getQuestionPool());
    }
}

package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.OrderedTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.RandomTaskGenerator;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CohortDatastoreTest {

    @Test
    public void addCohortTest() throws IOException {
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);

        //first method signature (taskGenerator)
        CohortDatastore cohortDatastore = new CohortDatastore();
        assertEquals(cohortDatastore.getNumberCohorts(), 0);

        //add cohort to empty map
        cohortDatastore.addCohort(new RandomTaskGenerator());
        assertEquals(cohortDatastore.getNumberCohorts(), 1);

        //add cohort to map with multiple entries (10)
        for (int i = 0; i < 3; i++){
            cohortDatastore.addCohort(new RandomTaskGenerator());
            cohortDatastore.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()));
            cohortDatastore.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList));
        }

        assertEquals(cohortDatastore.getNumberCohorts(), 10);

        //second method signature (taskGenerator and students)
        CohortDatastore cohortDatastore2 = new CohortDatastore();
        assertEquals(cohortDatastore2.getNumberCohorts(), 0);

        List<String> studentIDs = new ArrayList<>();
        studentIDs.add("testStudent1");
        studentIDs.add("testStudent2");
        studentIDs.add("testStudent3");

        //add cohort to empty map
        cohortDatastore2.addCohort(new RandomTaskGenerator(), studentIDs);
        assertEquals(cohortDatastore2.getNumberCohorts(), 1);

        //add cohort to map with multiple entries (10)
        for (int i = 0; i < 3; i++){
            cohortDatastore2.addCohort(new RandomTaskGenerator(), studentIDs);
            cohortDatastore2.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs);
            cohortDatastore2.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs);
        }

        assertEquals(cohortDatastore2.getNumberCohorts(), 10);
    }

}

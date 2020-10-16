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
        assertEquals(0, cohortDatastore.getNumberCohorts());

        //second method signature (taskGenerator and students)
        CohortDatastore cohortDatastore2 = new CohortDatastore();
        assertEquals(0, cohortDatastore2.getNumberCohorts());

        List<String> studentIDs = new ArrayList<>();
        studentIDs.add("testStudent1");
        studentIDs.add("testStudent2");
        studentIDs.add("testStudent3");

        //add cohort to empty map
        cohortDatastore2.addCohort(new RandomTaskGenerator(), studentIDs);
        assertEquals(1, cohortDatastore2.getNumberCohorts());

        //add cohort to map with multiple entries (10)
        for (int i = 0; i < 3; i++){
            cohortDatastore2.addCohort(new RandomTaskGenerator(), studentIDs);
            cohortDatastore2.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs);
            cohortDatastore2.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs);
        }

        assertEquals(10, cohortDatastore2.getNumberCohorts());
    }

}

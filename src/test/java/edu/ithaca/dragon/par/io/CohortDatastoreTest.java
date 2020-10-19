package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
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

    @Test
    public void getTaskGeneratorFromStudentID() throws IOException {
        CohortDatastore cohortDatastore = new CohortDatastore();
        RandomTaskGenerator randomTaskGenerator = new RandomTaskGenerator();
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
        OrderedTaskGenerator orderedTaskGenerator = new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList);
        LevelTaskGenerator levelTaskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        //get from empty map
        assertNull(cohortDatastore.getTaskGeneratorFromStudentID("testStudent1"));

        //get from map with one cohort
        List<String> studentIDs1 = new ArrayList<>();
        studentIDs1.add("testStudent1");
        studentIDs1.add("testStudent2");
        studentIDs1.add("testStudent3");

        cohortDatastore.addCohort(randomTaskGenerator, studentIDs1);
        assertEquals(randomTaskGenerator, cohortDatastore.getTaskGeneratorFromStudentID("testStudent2"));

        //get from map with one cohort with student that doesnt exist
        assertNull(cohortDatastore.getTaskGeneratorFromStudentID("testStudent4"));

        //get from map with multiple cohorts
        List<String> studentIDs2 = new ArrayList<>();
        studentIDs2.add("testStudent4");
        studentIDs2.add("testStudent5");
        studentIDs2.add("testStudent6");

        cohortDatastore.addCohort(orderedTaskGenerator, studentIDs2);
        List<String> studentIDs3 = new ArrayList<>();
        studentIDs3.add("testStudent7");
        studentIDs3.add("testStudent8");
        studentIDs3.add("testStudent9");

        cohortDatastore.addCohort(levelTaskGenerator, studentIDs3);
        assertEquals(orderedTaskGenerator, cohortDatastore.getTaskGeneratorFromStudentID("testStudent5"));
        assertEquals(levelTaskGenerator, cohortDatastore.getTaskGeneratorFromStudentID("testStudent9"));

        //get from map with multiple cohorts with student that doesnt exist
        assertNull(cohortDatastore.getTaskGeneratorFromStudentID("testStudent10"));

    }

    @Test
    public void makeCohortDatastoreFromCohortRecords() throws IOException {
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);

        List<String> studentIDs1 = new ArrayList<>();
        studentIDs1.add("testStudent1");
        studentIDs1.add("testStudent2");
        studentIDs1.add("testStudent3");

        List<String> studentIDs2 = new ArrayList<>();
        studentIDs2.add("testStudent4");
        studentIDs2.add("testStudent5");
        studentIDs2.add("testStudent6");

        List<String> studentIDs3 = new ArrayList<>();
        studentIDs3.add("testStudent7");

        //one CohortRecord
        List<CohortRecord> listToConvert = new ArrayList<>();
        Cohort cohort1 = new Cohort(new RandomTaskGenerator(), studentIDs1);
        listToConvert.add(CohortRecord.makeCohortRecordFromCohort(cohort1));

        CohortDatastore cohortDatastore = CohortDatastore.makeCohortDatastoreFromCohortRecords(listToConvert);
        assert cohortDatastore != null;
        assertEquals(1, cohortDatastore.getNumberCohorts());
        assertEquals(3, cohortDatastore.getTotalNumberStudents());

        //many CohortRecords (3)
        Cohort cohort2 = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs2);
        listToConvert.add(CohortRecord.makeCohortRecordFromCohort(cohort2));

        Cohort cohort3 = new Cohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs3);
        listToConvert.add(CohortRecord.makeCohortRecordFromCohort(cohort3));

        assertEquals(3, cohortDatastore.getNumberCohorts());
        assertEquals(7, cohortDatastore.getTotalNumberStudents());

    }

}

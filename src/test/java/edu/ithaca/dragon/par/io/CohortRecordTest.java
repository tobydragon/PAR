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

public class CohortRecordTest {

    @Test
    public void makeCohortFromCohortRecordTest() {
        List<String> testStudents = new ArrayList<>();
        testStudents.add("testStudent1");
        testStudents.add("testStudent2");
        testStudents.add("testStudent3");

        //empty student list
        CohortRecord emptyCohortRecord = new CohortRecord("RandomTaskGenerator", new ArrayList<>());
        Cohort cohort = CohortRecord.makeCohortFromCohortRecord(emptyCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof RandomTaskGenerator);
        assertEquals(new ArrayList<>(), cohort.getStudentIDs());

        //level task
        CohortRecord levelCohortRecord = new CohortRecord("LevelTaskGenerator", testStudents);
        cohort = CohortRecord.makeCohortFromCohortRecord(levelCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof LevelTaskGenerator);
        assertEquals(testStudents, cohort.getStudentIDs());

        //ordered task
        CohortRecord orderedCohortRecord = new CohortRecord("OrderedTaskGenerator", testStudents);
        cohort = CohortRecord.makeCohortFromCohortRecord(orderedCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof OrderedTaskGenerator);
        assertEquals(testStudents, cohort.getStudentIDs());

        //random task
        CohortRecord randomCohortRecord = new CohortRecord("RandomTaskGenerator", testStudents);
        cohort = CohortRecord.makeCohortFromCohortRecord(randomCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof RandomTaskGenerator);
        assertEquals(testStudents, cohort.getStudentIDs());
    }

    @Test
    public void makeCohortRecordFromCohortTest() throws IOException {
        RandomTaskGenerator randomTaskGenerator = new RandomTaskGenerator();
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
        OrderedTaskGenerator orderedTaskGenerator = new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList);
        LevelTaskGenerator levelTaskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        List<String> testStudents = new ArrayList<>();
        testStudents.add("testStudent1");
        testStudents.add("testStudent2");
        testStudents.add("testStudent3");

        //empty student list
        Cohort emptyCohort = new Cohort(randomTaskGenerator, new ArrayList<>());
        CohortRecord cohortRecord = CohortRecord.makeCohortRecordFromCohort(emptyCohort);
        assert cohortRecord != null;
        assertEquals(emptyCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("RandomTaskGenerator", cohortRecord.getTaskGeneratorType());

        //level task
        Cohort levelCohort = new Cohort(levelTaskGenerator, testStudents);
        cohortRecord = CohortRecord.makeCohortRecordFromCohort(levelCohort);
        assert cohortRecord != null;
        assertEquals(levelCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("LevelTaskGenerator", cohortRecord.getTaskGeneratorType());

        //ordered task
        Cohort orderedCohort = new Cohort(orderedTaskGenerator, testStudents);
        cohortRecord = CohortRecord.makeCohortRecordFromCohort(orderedCohort);
        assert cohortRecord != null;
        assertEquals(orderedCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("OrderedTaskGenerator", cohortRecord.getTaskGeneratorType());

        //random task
        Cohort randomCohort = new Cohort(randomTaskGenerator, testStudents);
        cohortRecord = CohortRecord.makeCohortRecordFromCohort(randomCohort);
        assert cohortRecord != null;
        assertEquals(randomCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("RandomTaskGenerator", cohortRecord.getTaskGeneratorType());

    }
}

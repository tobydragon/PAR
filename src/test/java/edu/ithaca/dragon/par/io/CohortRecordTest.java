package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.pedagogicalModel.*;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CohortRecordTest {

    @Test
    public void makeCohortFromCohortRecordTest() throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<String> testStudents = new ArrayList<>();
        testStudents.add("testStudent1");
        testStudents.add("testStudent2");
        testStudents.add("testStudent3");

        //empty student list
        CohortRecord emptyCohortRecord = new CohortRecord("RandomTaskGenerator", new ArrayList<>(), "SilentMessageGenerator", questionPool);
        Cohort cohort = CohortRecord.makeCohortFromCohortRecord(emptyCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof RandomTaskGenerator);
        assertTrue(cohort.getMessageGenerator() instanceof  SilentMessageGenerator);
        assertEquals(new ArrayList<>(), cohort.getStudentIDs());
        assertEquals(questionPool, cohort.getQuestionPool());

        //level task
        CohortRecord levelCohortRecord = new CohortRecord("LevelTaskGenerator", testStudents, "LevelMessageGenerator", questionPool);
        cohort = CohortRecord.makeCohortFromCohortRecord(levelCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof LevelTaskGenerator);
        assertTrue(cohort.getMessageGenerator() instanceof LevelMessageGenerator);
        assertEquals(testStudents, cohort.getStudentIDs());
        assertEquals(questionPool, cohort.getQuestionPool());

        //ordered task
        CohortRecord orderedCohortRecord = new CohortRecord("OrderedTaskGenerator", testStudents, "SilentMessageGenerator", questionPool, "src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json");
        cohort = CohortRecord.makeCohortFromCohortRecord(orderedCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof OrderedTaskGenerator);
        assertTrue(cohort.getMessageGenerator() instanceof SilentMessageGenerator);
        assertEquals(testStudents, cohort.getStudentIDs());
        assertEquals(questionPool, cohort.getQuestionPool());

        //random task
        CohortRecord randomCohortRecord = new CohortRecord("RandomTaskGenerator", testStudents, "SilentMessageGenerator", questionPool);
        cohort = CohortRecord.makeCohortFromCohortRecord(randomCohortRecord);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof RandomTaskGenerator);
        assertTrue(cohort.getMessageGenerator() instanceof SilentMessageGenerator);
        assertEquals(testStudents, cohort.getStudentIDs());
        assertEquals(questionPool, cohort.getQuestionPool());
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
        Cohort emptyCohort = new Cohort(randomTaskGenerator, new ArrayList<>(), new SilentMessageGenerator(), questionPool);
        CohortRecord cohortRecord = CohortRecord.makeCohortRecordFromCohort(emptyCohort);
        assert cohortRecord != null;
        assertEquals(emptyCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("RandomTaskGenerator", cohortRecord.getTaskGeneratorType());
        assertEquals("SilentMessageGenerator", cohortRecord.getMessageGeneratorType());
        assertEquals(questionPool, cohortRecord.getQuestionPool());

        //level task
        Cohort levelCohort = new Cohort(levelTaskGenerator, testStudents, new LevelMessageGenerator(), questionPool);
        cohortRecord = CohortRecord.makeCohortRecordFromCohort(levelCohort);
        assert cohortRecord != null;
        assertEquals(levelCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("LevelTaskGenerator", cohortRecord.getTaskGeneratorType());
        assertEquals("LevelMessageGenerator", cohortRecord.getMessageGeneratorType());
        assertEquals(questionPool, cohortRecord.getQuestionPool());

        //ordered task
        Cohort orderedCohort = new Cohort(orderedTaskGenerator, testStudents, new SilentMessageGenerator(), questionPool);
        cohortRecord = CohortRecord.makeCohortRecordFromCohort(orderedCohort);
        assert cohortRecord != null;
        assertEquals(orderedCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("OrderedTaskGenerator", cohortRecord.getTaskGeneratorType());
        assertEquals("SilentMessageGenerator", cohortRecord.getMessageGeneratorType());
        assertEquals(questionPool, cohortRecord.getQuestionPool());

        //random task
        Cohort randomCohort = new Cohort(randomTaskGenerator, testStudents, new SilentMessageGenerator(), questionPool);
        cohortRecord = CohortRecord.makeCohortRecordFromCohort(randomCohort);
        assert cohortRecord != null;
        assertEquals(randomCohort.getStudentIDs(), cohortRecord.getStudentIDs());
        assertEquals("RandomTaskGenerator", cohortRecord.getTaskGeneratorType());
        assertEquals("SilentMessageGenerator", cohortRecord.getMessageGeneratorType());
        assertEquals(questionPool, cohortRecord.getQuestionPool());

    }

    @Test
    public void cohortRecordsToFromJsonTest() throws IOException {
        List<String> testStudents = new ArrayList<>();
        testStudents.add("testStudent1");
        testStudents.add("testStudent2");
        testStudents.add("testStudent3");
        testStudents.add("masteredStudent");
        testStudents.add("buckmank");
        testStudents.add("s2");
        testStudents.add("s1");
        testStudents.add("testUser111");

        List<String> testStudents2 = new ArrayList<>();
        testStudents2.add("testStudent4");
        testStudents2.add("testStudent5");
        testStudents2.add("testStudent6");

        List<String> testStudents3 = new ArrayList<>();
        testStudents3.add("testStudent7");
        testStudents3.add("testStudent8");
        testStudents3.add("testStudent9");



        //create list of cohort records
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<CohortRecord> originalCohortRecords = new ArrayList<>();
        //empty student list
        CohortRecord emptyRecord = new CohortRecord("RandomTaskGenerator", new ArrayList<>(), "SilentMessageGenerator", questionPool);
        originalCohortRecords.add(emptyRecord);
        //level task
        CohortRecord levelRecord = new CohortRecord("LevelTaskGenerator", testStudents, "LevelMessageGenerator", questionPool);
        originalCohortRecords.add(levelRecord);
        //ordered task
        CohortRecord orderedRecord = new CohortRecord("OrderedTaskGenerator", testStudents2, "SilentMessageGenerator", questionPool, "src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoListTest1.json");
        originalCohortRecords.add(orderedRecord);
        //random task
        CohortRecord randomRecord = new CohortRecord("RandomTaskGenerator", testStudents3, "SilentMessageGenerator", questionPool);
        originalCohortRecords.add(randomRecord);

        //write to json
        JsonIoUtil jsonIoUtil = new JsonIoUtil(new JsonIoHelperDefault());
        jsonIoUtil.toFile("src/test/resources/author/CohortRecordsToFromJsonTest.json", originalCohortRecords);
        //read from json
        List<CohortRecord> testCohortRecords = jsonIoUtil.listFromFile("src/test/resources/author/CohortRecordsToFromJsonTest.json", CohortRecord.class);
        //assert two lists are equal
        assertEquals(originalCohortRecords.size(), testCohortRecords.size());
        for (int i = 0; i <originalCohortRecords.size(); i++){
            assertEquals(testCohortRecords.get(i), originalCohortRecords.get(i));
        }
    }

    //generate CohortDatastore JSON file for production code
    public static void main(String[] args) throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/main/resources/author/defaultQuestionPool.json").getAllQuestions());
        List<CohortRecord> toFile = new ArrayList<>();

        List<String> levelStudentIDs = new ArrayList<>();
        levelStudentIDs.add("testStudent1");
        levelStudentIDs.add("testStudent2");
        levelStudentIDs.add("testStudent3");
        toFile.add(new CohortRecord("LevelTaskGenerator", levelStudentIDs, "LevelMessageGenerator", questionPool));

        List<String> orderedStudentIDs = new ArrayList<>();
        orderedStudentIDs.add("testStudent4");
        orderedStudentIDs.add("testStudent5");
        orderedStudentIDs.add("testStudent6");
        toFile.add(new CohortRecord("OrderedTaskGenerator", orderedStudentIDs, "SilentMessageGenerator", questionPool, "src/main/resources/author/orderedQuestionInfoList/currentOrderedQuestionInfoList.json"));

        List<String> randomStudentIDs = new ArrayList<>();
        randomStudentIDs.add("testStudent7");
        randomStudentIDs.add("testStudent8");
        randomStudentIDs.add("testStudent9");
        toFile.add(new CohortRecord("RandomTaskGenerator", randomStudentIDs, "SilentMessageGenerator", questionPool));

        JsonIoHelper jsonIoHelper = new JsonIoHelperDefault();
        JsonIoUtil jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        jsonIoUtil.toFile("src/main/resources/author/defaultCohortDatastore.json", toFile);
    }
}

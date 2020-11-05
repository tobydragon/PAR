package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
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

        //level attach task
        CohortRecord lCR = new CohortRecord("LevelTaskGeneratorAttachment", testStudents, "LevelMessageGeneratorAttachment", questionPool);
        cohort = CohortRecord.makeCohortFromCohortRecord(lCR);
        assert cohort != null;
        assertTrue(cohort.getTaskGenerator() instanceof LevelTaskGeneratorAttachment);
        assertEquals(testStudents, cohort.getStudentIDs());

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
        //level attachment task
        CohortRecord levelARecord = new CohortRecord("LevelTaskGeneratorAttachment", testStudents, "LevelMessageGeneratorAttachment", questionPool);
        originalCohortRecords.add(levelARecord);
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

    @Test
    public void createQuestionOrderedInfoListTest() throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPool.json").getAllQuestions());

        List<QuestionOrderedInfo> toTest = OrderedTaskGenerator.createDefaultQuestionOrderedInfoList(questionPool, true);
        assertEquals(questionPool.getAllQuestions().size(), toTest.size());
        for (int i = 0; i < toTest.size(); i++){
            assertEquals(questionPool.getAllQuestions().get(i).getId(), toTest.get(i).getQuestionID());
            assertTrue(toTest.get(i).isIncludesFollowup());
        }

        //default with new method
        List<QuestionOrderedInfo> toPassToOTG1 = CohortRecord.createQuestionOrderedInfoList("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", questionPool);
        OrderedTaskGenerator testOTG1 = new OrderedTaskGenerator(questionPool, toPassToOTG1);
        assertEquals(questionPool.getAllQuestions().size(), testOTG1.getQuestionOrderedInfoList().size());
        for (int i = 0; i < toTest.size(); i++){
            assertEquals(questionPool.getAllQuestions().get(i).getId(), toTest.get(i).getQuestionID());
            assertTrue(toTest.get(i).isIncludesFollowup());
        }

        // test for file with different order/ followup inclusions
        JsonIoUtil jsonIoUtil = new JsonIoUtil(new JsonIoHelperDefault());
        List<QuestionOrderedInfo> toPassToOTG2 = CohortRecord.createQuestionOrderedInfoList("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoListTest1.json", questionPool);
        OrderedTaskGenerator testOTG2 = new OrderedTaskGenerator(questionPool, toPassToOTG2);
        assertNotNull(testOTG2.getQuestionOrderedInfoList());
        List<QuestionOrderedInfo> checkCustom = jsonIoUtil.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoListTest1.json", QuestionOrderedInfo.class);
        List<QuestionOrderedInfo> fromOTG2 = testOTG2.getQuestionOrderedInfoList();
        assertEquals(checkCustom.size(), fromOTG2.size());
        for (int i = 0; i < checkCustom.size(); i++){
            assertEquals(checkCustom.get(i).getQuestionID(), fromOTG2.get(i).getQuestionID());
            assertEquals(checkCustom.get(i).isIncludesFollowup(), fromOTG2.get(i).isIncludesFollowup());
        }

        // test for non existent file; creates default with true followup and prints warning
        List<QuestionOrderedInfo> toPassToOTG3 = CohortRecord.createQuestionOrderedInfoList("src/test/resources/author/orderedQuestionInfo/fileDoesNotExist.json", questionPool);
        OrderedTaskGenerator testOTG3 = new OrderedTaskGenerator(questionPool, toPassToOTG3);
        assertNotNull(testOTG3.getQuestionOrderedInfoList());
        List<QuestionOrderedInfo> checkDoNotExist = OrderedTaskGenerator.createDefaultQuestionOrderedInfoList(questionPool, true);
        List<QuestionOrderedInfo> fromOTG3 = testOTG3.getQuestionOrderedInfoList();
        assertEquals(checkDoNotExist.size(), fromOTG3.size());
        for(int i = 0; i < checkDoNotExist.size(); i++){
            assertEquals(checkDoNotExist.get(i).getQuestionID(), fromOTG3.get(i).getQuestionID());
            assertEquals(checkDoNotExist.get(i).isIncludesFollowup(), fromOTG3.get(i).isIncludesFollowup());
        }
    }

    @Test
    public void overwriteCohortDatastoreFileTest() throws IOException {
        JsonIoUtil jsonIoUtil = new JsonIoUtil(new JsonIoHelperDefault());
        //OVERWRITE TEST FILES FIRST; OTHERWISE TESTS FAIL
        List<CohortRecord> reset = jsonIoUtil.listFromFile("src/test/resources/author/resetCohortDatastoreTestFiles.json", CohortRecord.class);
        jsonIoUtil.toFile("src/test/resources/author/defaultCohortDatastore.json", reset);
        jsonIoUtil.toFile("src/test/resources/author/currentCohortDatastore.json", reset);

        //read in 2 cohort datastores- 1 not altered for comparison, 1 to alter
        List<CohortRecord> testCohortRecords = jsonIoUtil.listFromFile("src/test/resources/author/defaultCohortDatastore.json", CohortRecord.class);
        JSONCohortDatastore toReference = CohortRecord.makeCohortDatastoreFromCohortRecords(testCohortRecords, "src/test/resources/author/defaultCohortDatastore.json");
        JSONCohortDatastore toTest = CohortRecord.makeCohortDatastoreFromCohortRecords(testCohortRecords, "src/test/resources/author/currentCohortDatastore.json");

        //ask for TaskGenerator of studentID that does not exist
        assertFalse(toTest.isStudentIDInDatastore("taskGeneratorStudent"));
        TaskGenerator taskGeneratorRequested = toTest.getTaskGeneratorFromStudentID("taskGeneratorStudent");
        assertTrue (taskGeneratorRequested instanceof LevelTaskGenerator);
        assertNotEquals(toTest.getMasterCohortList(), toReference.getMasterCohortList());
        assertTrue(toTest.getDefaultCohort().getStudentIDs().contains("taskGeneratorStudent"));
        assertEquals(1, toTest.getDefaultCohort().getStudentIDs().size());

        String toTestFilename = toTest.getCohortDatastoreFilename();
        List<CohortRecord> toTestFilenameRecords = jsonIoUtil.listFromFile(toTestFilename, CohortRecord.class);
        JSONCohortDatastore fromFilename = CohortRecord.makeCohortDatastoreFromCohortRecords(toTestFilenameRecords, toTestFilename);
        List<Cohort> toTestList = toTest.getMasterCohortList();
        List<Cohort> fromFilenameList = toReference.getMasterCohortList();
        assertEquals(toTestList.size(), fromFilenameList.size());
        for (int i = 0; i < toTest.getMasterCohortList().size(); i++){
            assertEquals(toTestList.get(i).getTaskGenerator().getClass(), fromFilenameList.get(i).getTaskGenerator().getClass());
            assertEquals(toTestList.get(i).getMessageGenerator().getClass(), fromFilenameList.get(i).getMessageGenerator().getClass());
            assertEquals(toTestList.get(i).getStudentIDs(), fromFilenameList.get(i).getStudentIDs());
            assertEquals(toTestList.get(i).getQuestionPool(), fromFilenameList.get(i).getQuestionPool());
            assertEquals(toTestList.get(i).getQuestionOrderedInfoFilename(), fromFilenameList.get(i).getQuestionOrderedInfoFilename());

        }
        assertNotEquals(toReference.getMasterCohortList(), fromFilename.getMasterCohortList());

        //ask for MessageGenerator of StudentID that does not exist
        assertFalse(toTest.isStudentIDInDatastore("messageGeneratorStudent"));
        MessageGenerator messageGeneratorRequested = toTest.getMessageGeneratorFromStudentID("messageGeneratorStudent");
        assertTrue(messageGeneratorRequested instanceof LevelMessageGenerator);
        assertNotEquals(toReference.getMasterCohortList(), toTest.getMasterCohortList());
        assertTrue(toTest.getDefaultCohort().getStudentIDs().contains("messageGeneratorStudent"));
        assertEquals(2, toTest.getDefaultCohort().getStudentIDs().size());

        toTestFilename = toTest.getCohortDatastoreFilename();
        toTestFilenameRecords = jsonIoUtil.listFromFile(toTestFilename, CohortRecord.class);
        fromFilename = CohortRecord.makeCohortDatastoreFromCohortRecords(toTestFilenameRecords, toTestFilename);
        toTestList = toTest.getMasterCohortList();
        fromFilenameList = toReference.getMasterCohortList();
        assertEquals(toTestList.size(), fromFilenameList.size());
        for (int i = 0; i < toTest.getMasterCohortList().size(); i++){
            assertEquals(toTestList.get(i).getTaskGenerator().getClass(), fromFilenameList.get(i).getTaskGenerator().getClass());
            assertEquals(toTestList.get(i).getMessageGenerator().getClass(), fromFilenameList.get(i).getMessageGenerator().getClass());
            assertEquals(toTestList.get(i).getStudentIDs(), fromFilenameList.get(i).getStudentIDs());
            assertEquals(toTestList.get(i).getQuestionPool(), fromFilenameList.get(i).getQuestionPool());
            assertEquals(toTestList.get(i).getQuestionOrderedInfoFilename(), fromFilenameList.get(i).getQuestionOrderedInfoFilename());

        }
        assertNotEquals(toReference.getMasterCohortList(), fromFilename.getMasterCohortList());
    }

    //generate CohortDatastore JSON file for production code
    public static void main(String[] args) throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/main/resources/author/defaultQuestionPool.json").getAllQuestions());
        List<CohortRecord> toFile = new ArrayList<>();

        //default cohort
        toFile.add(new CohortRecord("LevelTaskGenerator", new ArrayList<>(), "LevelMessageGenerator", questionPool));

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


        List<String> attachStudentIDs = new ArrayList<>();
        attachStudentIDs.add("testStudent10");
        attachStudentIDs.add("testStudent11");
        attachStudentIDs.add("testStudent12");
        toFile.add(new CohortRecord("LevelTaskGeneratorAttachment", attachStudentIDs, "LevelMessageGeneratorAttachment", questionPool));

        JsonIoHelper jsonIoHelper = new JsonIoHelperDefault();
        JsonIoUtil jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        jsonIoUtil.toFile("src/main/resources/author/defaultCohortDatastore.json", toFile);
    }
}

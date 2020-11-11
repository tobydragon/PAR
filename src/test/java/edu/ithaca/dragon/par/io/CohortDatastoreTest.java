package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.cohortModel.Cohort;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.pedagogicalModel.*;
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
        String testCohortDatastoreFilename = "src/test/resources/author/currentCohortDatastore.json";

        //first method signature (taskGenerator)
        JSONCohortDatastore cohortDatastore = new JSONCohortDatastore(testCohortDatastoreFilename, new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>(), new LevelMessageGenerator(), questionPool));
        assertEquals(1, cohortDatastore.getNumberCohorts());

        //second method signature (taskGenerator and students)
        JSONCohortDatastore cohortDatastore2 = new JSONCohortDatastore(testCohortDatastoreFilename, new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>(), new LevelMessageGenerator(), questionPool));
        assertEquals(1, cohortDatastore2.getNumberCohorts());

        List<String> studentIDs = new ArrayList<>();
        studentIDs.add("testStudent1");
        studentIDs.add("testStudent2");
        studentIDs.add("testStudent3");

        //add cohort to empty map
        cohortDatastore2.addCohort(new RandomTaskGenerator(), studentIDs, new SilentMessageGenerator(), questionPool);
        assertEquals(2, cohortDatastore2.getNumberCohorts());

        //add cohort to map with multiple entries (10)
        for (int i = 0; i < 3; i++){
            cohortDatastore2.addCohort(new RandomTaskGenerator(), studentIDs, new SilentMessageGenerator(), questionPool);
            cohortDatastore2.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs, new LevelMessageGenerator(), questionPool);
            cohortDatastore2.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs, new SilentMessageGenerator(), questionPool);
        }

        assertEquals(11, cohortDatastore2.getNumberCohorts());
    }

    @Test
    public void getTaskGeneratorFromStudentID() throws IOException {
        String testCohortDatastoreFilename = "src/test/resources/author/currentCohortDatastore.json";
        RandomTaskGenerator randomTaskGenerator = new RandomTaskGenerator();
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
        OrderedTaskGenerator orderedTaskGenerator = new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList);
        LevelTaskGenerator levelTaskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());
        JSONCohortDatastore cohortDatastore = new JSONCohortDatastore(testCohortDatastoreFilename, new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>(), new LevelMessageGenerator(), questionPool));

        //get from empty map
        assertTrue(cohortDatastore.getTaskGeneratorFromStudentID("testStudent1") instanceof LevelTaskGenerator);

        //get from map with one cohort
        List<String> studentIDs1 = new ArrayList<>();
        studentIDs1.add("testStudent1");
        studentIDs1.add("testStudent2");
        studentIDs1.add("testStudent3");

        cohortDatastore.addCohort(randomTaskGenerator, studentIDs1, new SilentMessageGenerator(), questionPool);
        assertEquals(randomTaskGenerator, cohortDatastore.getTaskGeneratorFromStudentID("testStudent2"));

        //get from map with one cohort with student that doesnt exist
        assertTrue(cohortDatastore.getTaskGeneratorFromStudentID("testStudent4") instanceof LevelTaskGenerator);

        //get from map with multiple cohorts
        List<String> studentIDs2 = new ArrayList<>();
        studentIDs2.add("testStudent4");
        studentIDs2.add("testStudent5");
        studentIDs2.add("testStudent6");

        cohortDatastore.addCohort(orderedTaskGenerator, studentIDs2, new SilentMessageGenerator(), questionPool);
        List<String> studentIDs3 = new ArrayList<>();
        studentIDs3.add("testStudent7");
        studentIDs3.add("testStudent8");
        studentIDs3.add("testStudent9");

        cohortDatastore.addCohort(levelTaskGenerator, studentIDs3, new LevelMessageGenerator(), questionPool);
        assertEquals(orderedTaskGenerator, cohortDatastore.getTaskGeneratorFromStudentID("testStudent5"));
        assertEquals(levelTaskGenerator, cohortDatastore.getTaskGeneratorFromStudentID("testStudent9"));

        //get from map with multiple cohorts with student that doesnt exist
        assertTrue(cohortDatastore.getTaskGeneratorFromStudentID("testStudent10") instanceof LevelTaskGenerator);

    }

    @Test
    public void makeCohortDatastoreFromCohortRecords() throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        String testCohortDatastoreFilename = "src/test/resources/author/currentCohortDatastore.json";

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
        Cohort cohort1 = new Cohort(new RandomTaskGenerator(), studentIDs1, new SilentMessageGenerator(), questionPool);
        listToConvert.add(CohortRecord.makeCohortRecordFromCohort(cohort1));

        JSONCohortDatastore cohortDatastore = CohortRecord.makeCohortDatastoreFromCohortRecords(listToConvert, testCohortDatastoreFilename);
        assertEquals(1, cohortDatastore.getNumberCohorts());
        assertEquals(3, cohortDatastore.getTotalNumberStudents());

        //many CohortRecords (3)
        Cohort cohort2 = new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs2, new LevelMessageGenerator(), questionPool);
        listToConvert.add(CohortRecord.makeCohortRecordFromCohort(cohort2));

        List<QuestionOrderedInfo> toPassToOTG = CohortRecord.createQuestionOrderedInfoList("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", questionPool);
        Cohort cohort3 = new Cohort(new OrderedTaskGenerator(questionPool, toPassToOTG), studentIDs3, new SilentMessageGenerator(), questionPool, "src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json");
        listToConvert.add(CohortRecord.makeCohortRecordFromCohort(cohort3));
        cohortDatastore = CohortRecord.makeCohortDatastoreFromCohortRecords(listToConvert, testCohortDatastoreFilename);

        assertEquals(3, cohortDatastore.getNumberCohorts());
        assertEquals(7, cohortDatastore.getTotalNumberStudents());

    }

    @Test
    public void makeCohortRecordsFromCohortDatastoreTest() throws IOException {
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
        String testCohortDatastoreFilename = "src/test/resources/author/currentCohortDatastore.json";

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

        //empty CohortDatastore
        JSONCohortDatastore cohortDatastore = new JSONCohortDatastore(testCohortDatastoreFilename, new Cohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), new ArrayList<>(), new LevelMessageGenerator(), questionPool));
        List<CohortRecord> cohortRecords = CohortRecord.makeCohortRecordsFromCohortDatastore(cohortDatastore);
        assertEquals(1, cohortRecords.size());

        //one cohort in CohortDatastore
        cohortDatastore.addCohort(new RandomTaskGenerator(), studentIDs1, new SilentMessageGenerator(), questionPool);
        cohortRecords = CohortRecord.makeCohortRecordsFromCohortDatastore(cohortDatastore);
        assertEquals(2, cohortRecords.size());
        assertEquals(TaskGeneratorType.randomTaskGenerator, cohortRecords.get(1).getTaskGeneratorType());
        assertEquals(studentIDs1, cohortRecords.get(1).getStudentIDs());
        assertEquals(questionPool, cohortRecords.get(1).getQuestionPool());

        //multiple cohorts in CohortDatastore (3)
        cohortDatastore.addCohort(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()), studentIDs2, new LevelMessageGenerator(), questionPool);
        cohortDatastore.addCohort(new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList), studentIDs3, new SilentMessageGenerator(), questionPool);
        cohortRecords = CohortRecord.makeCohortRecordsFromCohortDatastore(cohortDatastore);
        assertEquals(4, cohortRecords.size());

        assertEquals(TaskGeneratorType.randomTaskGenerator, cohortRecords.get(1).getTaskGeneratorType());
        assertEquals(studentIDs1, cohortRecords.get(1).getStudentIDs());
        assertEquals(questionPool, cohortRecords.get(1).getQuestionPool());
        assertEquals(MessageGeneratorType.silentMessageGenerator, cohortRecords.get(1).getMessageGeneratorType());

        assertEquals(TaskGeneratorType.levelTaskGenerator, cohortRecords.get(2).getTaskGeneratorType());
        assertEquals(studentIDs2, cohortRecords.get(2).getStudentIDs());
        assertEquals(questionPool, cohortRecords.get(2).getQuestionPool());
        assertEquals(MessageGeneratorType.levelMessageGenerator, cohortRecords.get(2).getMessageGeneratorType());

        assertEquals(TaskGeneratorType.orderedTaskGenerator, cohortRecords.get(3).getTaskGeneratorType());
        assertEquals(studentIDs3, cohortRecords.get(3).getStudentIDs());
        assertEquals(questionPool, cohortRecords.get(3).getQuestionPool());
        assertEquals(MessageGeneratorType.silentMessageGenerator, cohortRecords.get(3).getMessageGeneratorType());
    }
}

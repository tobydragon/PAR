package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.*;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParStudentAndAuthorServerTest {

    @Test
    void transferAuthoredQuestionsToStudentServerTest(@TempDir Path tempDir) throws IOException {
        Path currentQuestionPath = tempDir.resolve("currentAuthorQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        JsonAuthorDatastore jsonAuthorDatastore = new JsonAuthorDatastore(
                currentQuestionPath.toString(), "src/test/resources/author/SampleQuestionPool.json",
                currentQuestionTemplatePath.toString(), "src/test/resources/author/DemoQuestionPoolTemplate.json",
                tempDir.resolve("currentAuthorModel.json").toString(), new JsonIoHelperDefault());
        assertEquals(15, jsonAuthorDatastore.getAllAuthoredQuestions().size());

        Path currentStudentModelDir = tempDir.resolve("students");
        assertTrue(new File(currentStudentModelDir.toString()).mkdir());
        JsonStudentModelDatastore jsonStudentDatastore = new JsonStudentModelDatastore(
                tempDir.resolve("currentQuestions.json").toString(),
                "src/test/resources/author/DemoQuestionPoolFollowup.json",
                new JsonIoHelperDefault(),
                currentStudentModelDir.toString());
        assertEquals(47, jsonStudentDatastore.getAllQuestions().size());

        ParStudentAndAuthorServer parStudentAndAuthorServer = new ParStudentAndAuthorServer(jsonStudentDatastore, jsonAuthorDatastore);
        parStudentAndAuthorServer.transferAuthoredQuestionsToStudentServer();

        assertEquals(0, jsonAuthorDatastore.getAllAuthoredQuestions().size());
        assertEquals(62, jsonStudentDatastore.getAllQuestions().size());

        //load again from file to ensure changes are kept
        jsonAuthorDatastore = new JsonAuthorDatastore(currentQuestionPath.toString(), currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString());
        jsonStudentDatastore = new JsonStudentModelDatastore(tempDir.resolve("currentQuestions.json").toString(), currentStudentModelDir.toString());

        assertEquals(0, jsonAuthorDatastore.getAllAuthoredQuestions().size());
        assertEquals(62, jsonStudentDatastore.getAllQuestions().size());
    }

    @Test
    void windowSizeTests(@TempDir Path tempDir) throws IOException {
        Path currentQuestionPath = tempDir.resolve("currentAuthorQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        JsonAuthorDatastore jsonAuthorDatastore = new JsonAuthorDatastore(
                currentQuestionPath.toString(), "src/test/resources/author/SampleQuestionPool.json",
                currentQuestionTemplatePath.toString(), "src/test/resources/author/DemoQuestionPoolTemplate.json",
                tempDir.resolve("currentAuthorModel.json").toString(), new JsonIoHelperDefault());

        Path currentStudentModelDir = tempDir.resolve("students");
        assertTrue(new File(currentStudentModelDir.toString()).mkdir());
        JsonStudentModelDatastore jsonStudentDatastore = new JsonStudentModelDatastore(
                tempDir.resolve("currentQuestions.json").toString(),
                "src/test/resources/author/SampleQuestionsEmpty.json",
                new JsonIoHelperDefault(),
                currentStudentModelDir.toString());

        ParStudentAndAuthorServer parStudentAndAuthorServer = new ParStudentAndAuthorServer(jsonStudentDatastore, jsonAuthorDatastore);
        Map<EquineQuestionTypes, String> estStrings = parStudentAndAuthorServer.calcKnowledgeEstimateStringsByType("no one");
        assertEquals(4, estStrings.size());
        assertEquals("", estStrings.get(EquineQuestionTypes.plane));
        assertEquals("", estStrings.get(EquineQuestionTypes.attachment));

        parStudentAndAuthorServer.transferAuthoredQuestionsToStudentServer();
        estStrings = parStudentAndAuthorServer.calcKnowledgeEstimateStringsByType("no one");

        assertEquals("____", estStrings.get(EquineQuestionTypes.plane));
        assertEquals("____", estStrings.get(EquineQuestionTypes.attachment));

    }

    @Test
    public void nextImageTaskTest(@TempDir Path tempDir) throws IOException{
        Path currentStudentModelDir = tempDir.resolve("students");
        assertTrue(new File(currentStudentModelDir.toString()).mkdir());
        JsonStudentModelDatastore jsonStudentDatastore = new JsonStudentModelDatastore(
                tempDir.resolve("currentQuestions.json").toString(),
                "src/test/resources/author/SampleQuestionPool3.json",
                new JsonIoHelperDefault(),
                currentStudentModelDir.toString());

        ParStudentAndAuthorServer parStudentAndAuthorServer = new ParStudentAndAuthorServer(jsonStudentDatastore, null);

        ImageTask nextTask = parStudentAndAuthorServer.nextImageTask("s1");
        ImageTask intendedFirstTask = new JsonIoUtil(new JsonIoHelperDefault()).fromFile("src/test/resources/author/nextImageTaskTest1.json", ImageTask.class);
        assertEquals(intendedFirstTask, nextTask);

        nextTask = parStudentAndAuthorServer.nextImageTask("s2");
        assertEquals(intendedFirstTask, nextTask);

        nextTask = parStudentAndAuthorServer.nextImageTask("s1");

        assertNotNull(nextTask);
        ImageTask intendedLastTask = new JsonIoUtil(new JsonIoHelperDefault()).fromFile("src/test/resources/author/nextImageTaskTest2.json", ImageTask.class);
        assertEquals(intendedLastTask, nextTask);

        nextTask = parStudentAndAuthorServer.nextImageTask("s2");
        assertNotNull(nextTask);
        assertEquals(intendedLastTask, nextTask);

    }

    @Test
    public void imageTaskResponseSubmittedAndCalcScoreTest(@TempDir Path tempDir) throws IOException{

        Path currentStudentModelDir = tempDir.resolve("students");
        assertTrue(new File(currentStudentModelDir.toString()).mkdir());
        JsonStudentModelDatastore jsonStudentDatastore = new JsonStudentModelDatastore(
                tempDir.resolve("currentQuestions.json").toString(),
                "src/test/resources/author/SampleQuestionPool3.json",
                new JsonIoHelperDefault(),
                currentStudentModelDir.toString());

        ParStudentAndAuthorServer parStudentAndAuthorServer = new ParStudentAndAuthorServer(jsonStudentDatastore, null);

        ImageTaskResponseImp1 responseSet2=new ImageTaskResponseImp1("response1", Arrays.asList("PlaneQ1", "PlaneQ2", "PlaneQ3", "PlaneQ4", "PlaneQ5", "StructureQ1", "StructureQ2", "StructureQ3", "StructureQ4", "StructureQ5", "ZoneQ1", "ZoneQ2", "ZoneQ3", "ZoneQ4", "ZoneQ5"),Arrays.asList("Latera", "Transvers", "Latera", "Latera", "Transvers", "bone", "ligament", "tendon", "bone", "tumor", "3c", "1b", "3c", "2a", "2b"));
        ImageTaskResponseImp1 responseSet3=new ImageTaskResponseImp1("response1", Arrays.asList("PlaneQ1", "PlaneQ2", "PlaneQ3", "PlaneQ4", "PlaneQ5", "StructureQ1", "StructureQ2", "StructureQ3", "StructureQ4", "StructureQ5", "ZoneQ1", "ZoneQ2", "ZoneQ3", "ZoneQ4", "ZoneQ5"),Arrays.asList("I'm","bad","student","I'm","bad","student","I'm","bad","student","I'm","bad","student","I'm","bad","student"));
        List<ImageTaskResponseImp1> responsesFromFile = new JsonIoUtil(new JsonIoHelperDefault()).listFromFile("src/test/resources/author/SampleResponses.json", ImageTaskResponseImp1.class);

        //star student
        ImageTaskResponseImp1 imageTaskResponseImp1 = responsesFromFile.get(0);
        imageTaskResponseImp1.setUserId("s1");
        parStudentAndAuthorServer.submitImageTaskResponse(imageTaskResponseImp1);//gives responses from response file
        assertEquals("100.000",parStudentAndAuthorServer.calcOverallKnowledgeEstimate("s1"));


        responseSet2.setUserId("s2");
        parStudentAndAuthorServer.submitImageTaskResponse(responseSet2);//gives responses from responseSet2 that contains some right and wrong answers
        assertEquals("83.333",parStudentAndAuthorServer.calcOverallKnowledgeEstimate("s2"));
        //score should stay the same even when the correct answers are entered afterwards since they were all answered in a
        //time window that didnt exceed 30 seconds
        imageTaskResponseImp1.setUserId("s2");
        parStudentAndAuthorServer.submitImageTaskResponse(imageTaskResponseImp1);
        assertEquals("83.333",parStudentAndAuthorServer.calcOverallKnowledgeEstimate("s2"));
        parStudentAndAuthorServer.submitImageTaskResponse(imageTaskResponseImp1);
        assertEquals("83.333",parStudentAndAuthorServer.calcOverallKnowledgeEstimate("s2"));


        //score should go way down since they had it, but then got it wrong within 30 seconds
        imageTaskResponseImp1.setUserId("s3");
        parStudentAndAuthorServer.submitImageTaskResponse(responsesFromFile.get(0));//gives responses from file
        assertEquals("100.000",parStudentAndAuthorServer.calcOverallKnowledgeEstimate("s3"));
        responseSet3.setUserId("s3");
        parStudentAndAuthorServer.submitImageTaskResponse(responseSet3);
        assertEquals("0.000",parStudentAndAuthorServer.calcOverallKnowledgeEstimate("s3"));

    }
}
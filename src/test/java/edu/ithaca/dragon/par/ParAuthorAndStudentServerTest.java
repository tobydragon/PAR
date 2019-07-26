package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.io.JsonAuthorDatastore;
import edu.ithaca.dragon.par.io.JsonStudentModelDatastore;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class ParAuthorAndStudentServerTest {

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

        ParAuthorAndStudentServer parAuthorAndStudentServer = new ParAuthorAndStudentServer(jsonStudentDatastore, jsonAuthorDatastore);
        parAuthorAndStudentServer.transferAuthoredQuestionsToStudentServer();

        assertEquals(0, jsonAuthorDatastore.getAllAuthoredQuestions().size());
        assertEquals(62, jsonStudentDatastore.getAllQuestions().size());

        //load again from file to ensure changes are kept
        jsonAuthorDatastore = new JsonAuthorDatastore(currentQuestionPath.toString(), currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString());
        jsonStudentDatastore = new JsonStudentModelDatastore(tempDir.resolve("currentQuestions.json").toString(), currentStudentModelDir.toString());

        assertEquals(0, jsonAuthorDatastore.getAllAuthoredQuestions().size());
        assertEquals(62, jsonStudentDatastore.getAllQuestions().size());
    }
}
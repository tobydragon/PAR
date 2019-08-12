package edu.ithaca.dragon.par.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class QuestionPoolDatastoreTest {

    @Test
    public void interfaceSetup(@TempDir Path tempDir) throws IOException {
        //Test the Json Implementation
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Files.copy(Paths.get("src/test/resources/author/DemoQuestionPoolFollowup.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        QuestionPoolDatastore jsonQuestionPoolDatastore = new JsonQuestionPoolDatastore(currentQuestionPath.toString());
        interfaceTest(jsonQuestionPoolDatastore);

        //TODO: Test the hibernate implementation
    }

    public void interfaceTest(QuestionPoolDatastore questionPoolDatastore){

    }

}

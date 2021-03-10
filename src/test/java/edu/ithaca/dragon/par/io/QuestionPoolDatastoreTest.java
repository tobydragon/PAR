package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public void interfaceTest(QuestionPoolDatastore questionPoolDatastore) throws IOException{
        int numOfQuestions = questionPoolDatastore.getAllQuestions().size();

        //add question
        questionPoolDatastore.addQuestion(new Question("NewQuestion", "NewQuestion", "Plane", "A", Arrays.asList("A", "B"), "...", Arrays.asList(new Question("NewFollowup", "NewFollowup", "Plane", "B", Arrays.asList("A", "B", "C"), "...")), "feedback"));
        assertEquals(numOfQuestions+1, questionPoolDatastore.getAllQuestions().size());

        //test the persistence of the datastore

    }

}

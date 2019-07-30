package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonQuestionPoolDatastoreTest {

    @Test
    void removeAllQuestionsTest(@TempDir Path tempDir) throws IOException {
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Files.copy(Paths.get("src/test/resources/author/DemoQuestionPoolFollowup.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        JsonQuestionPoolDatastore jsonQuestionPoolDatastore = new JsonQuestionPoolDatastore(currentQuestionPath.toString());
        assertEquals(47, jsonQuestionPoolDatastore.getAllQuestions().size());
        List<Question> questions = jsonQuestionPoolDatastore.removeAllQuestions();
        assertEquals(47, questions.size());
        assertEquals(0, jsonQuestionPoolDatastore.getAllQuestions().size());
        //check it was written to file successfully
        jsonQuestionPoolDatastore = new JsonQuestionPoolDatastore(currentQuestionPath.toString());
        assertEquals(0, jsonQuestionPoolDatastore.getAllQuestions().size());
    }
}
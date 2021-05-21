package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionResponseOOPTest {

    @Test
    public void toAndFromJsonTest(@TempDir Path tempDir) throws IOException {
        QuestionResponseOOP questionResponseOOP = new QuestionResponseOOP("Q1", "A");
        Path newQuestionResponseOOPPath = tempDir.resolve("questionResponseOOP.json");
        JsonUtil.toJsonFile(newQuestionResponseOOPPath.toString(), questionResponseOOP);

        QuestionResponseOOP questionResponseOOP1 = JsonUtil.fromJsonFile(newQuestionResponseOOPPath.toString(), QuestionResponseOOP.class);
        assertEquals(questionResponseOOP.questionId, questionResponseOOP1.questionId);
        assertEquals(questionResponseOOP.responseText, questionResponseOOP1.responseText);
    }
}

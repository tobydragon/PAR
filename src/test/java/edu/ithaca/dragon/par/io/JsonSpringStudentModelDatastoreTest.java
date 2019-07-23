package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.springio.JsonSpringStudentModelDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonSpringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSpringStudentModelDatastoreTest {

    @Test
    public void copyDefaultQuestionsTest(@TempDir Path tempDir) throws IOException{
        Path currentQuestionFile = tempDir.resolve("CurrentTestQuestionFile.json");
        assertFalse(currentQuestionFile.toFile().exists());

        File studentDir = tempDir.resolve("testStudents").toFile();
        studentDir.mkdir();
        StudentModelDatastore studentModelDatastore = new JsonSpringStudentModelDatastore(currentQuestionFile.toString(), "author/DemoQuestionPool.json", studentDir.toString());

        assertTrue(currentQuestionFile.toFile().exists());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());
    }

    @Test
    public void useCurrentQuestionsTest(@TempDir Path tempDir) throws IOException{
        StudentModelDatastore studentModelDatastore = new JsonSpringStudentModelDatastore("src/test/resources/author/DemoQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());
    }

}

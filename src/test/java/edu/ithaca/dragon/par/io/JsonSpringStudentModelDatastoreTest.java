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

        assertEquals(0, studentModelDatastore.loadStudentModels().size());
    }

    @Test
    public void useCurrentQuestionsTest(@TempDir Path tempDir) throws IOException{
        StudentModelDatastore studentModelDatastore = new JsonSpringStudentModelDatastore("src/test/resources/author/DemoQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());
        assertEquals(0, studentModelDatastore.loadStudentModels().size());
    }

    @Test
    public void saveAndLoadStudentModelsTest(@TempDir Path tempDir) throws IOException{
        QuestionPool questionPool = new QuestionPool(JsonSpringUtil.listFromClassPathJson("author/SampleQuestionPool.json", Question.class));
        List<StudentModel> studentModels = Arrays.asList(JsonSpringUtil.fromClassPathJson("author/students/TestUser100.json", StudentModelRecord.class).buildStudentModel(questionPool),
                JsonSpringUtil.fromClassPathJson("author/students/TestUser101.json", StudentModelRecord.class).buildStudentModel(questionPool));

        StudentModelDatastore studentModelDatastore = new JsonSpringStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(0, studentModelDatastore.loadStudentModels().size());
        studentModelDatastore.saveStudentModels(studentModels);
        assertEquals(2, studentModelDatastore.loadStudentModels().size());
        assertNotNull(studentModelDatastore.loadStudentModel("TestUser100"));

        //read it into a new structure and check it
        studentModelDatastore = new JsonSpringStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(2, studentModelDatastore.loadStudentModels().size());
        assertNotNull(studentModelDatastore.loadStudentModel("TestUser101"));

        //try to load in a non-existing file
        StudentModel notAUser = studentModelDatastore.loadStudentModel("ThisIsNotAValidUserId");
        assertNull(notAUser);
    }

}

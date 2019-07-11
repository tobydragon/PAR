package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
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

public class JsonSpringDatastoreTest {

    @Test
    public void copyDefaultQuestionsTest(@TempDir Path tempDir) throws IOException{
        Path currentQuestionFile = tempDir.resolve("CurrentTestQuestionFile.json");
        assertFalse(currentQuestionFile.toFile().exists());

        File studentDir = tempDir.resolve("testStudents").toFile();
        studentDir.mkdir();
        Datastore datastore = new JsonSpringDatastore(currentQuestionFile.toString(), "author/DemoQuestionPool.json", studentDir.toString());

        assertTrue(currentQuestionFile.toFile().exists());
        assertEquals(47, datastore.loadQuestions().size());

        assertEquals(0, datastore.loadStudentModels().size());
    }

    @Test
    public void useCurrentQuestionsTest(@TempDir Path tempDir) throws IOException{
        Datastore datastore = new JsonSpringDatastore("src/test/resources/author/DemoQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(47, datastore.loadQuestions().size());
        assertEquals(0, datastore.loadStudentModels().size());
    }

    @Test
    public void saveAndLoadStudentModelsTest(@TempDir Path tempDir) throws IOException{
        QuestionPool questionPool = new QuestionPool(JsonSpringUtil.listFromClassPathJson("author/SampleQuestionPool.json", Question.class));
        List<StudentModel> studentModels = Arrays.asList(JsonSpringUtil.fromClassPathJson("author/students/TestUser100.json", StudentModelRecord.class).buildStudentModel(questionPool),
                JsonSpringUtil.fromClassPathJson("author/students/TestUser101.json", StudentModelRecord.class).buildStudentModel(questionPool));

        Datastore datastore = new JsonSpringDatastore("src/test/resources/author/SampleQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(0, datastore.loadStudentModels().size());
        datastore.saveStudentModels(studentModels);
        assertEquals(2, datastore.loadStudentModels().size());
        assertNotNull(datastore.loadStudentModel("TestUser100"));

        //read it into a new structure and check it
        datastore = new JsonSpringDatastore("src/test/resources/author/SampleQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(2, datastore.loadStudentModels().size());
        assertNotNull(datastore.loadStudentModel("TestUser101"));

        //try to load in a non-existing file
        StudentModel notAUser = datastore.loadStudentModel("ThisIsNotAValidUserId");
        assertNull(notAUser);
    }

}

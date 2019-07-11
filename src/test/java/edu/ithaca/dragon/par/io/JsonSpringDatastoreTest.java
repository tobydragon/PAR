package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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
        assertEquals(60, datastore.loadQuestions().size());

        assertEquals(0, datastore.loadStudentModels().size());
    }

    @Test
    public void useCurrentQuestionsTest(@TempDir Path tempDir) throws IOException{
        Datastore datastore = new JsonSpringDatastore("author/DemoQuestionPool.json", "bad path", tempDir.toString());
        assertEquals(60, datastore.loadQuestions().size());
        assertEquals(0, datastore.loadStudentModels().size());
    }

    @Test
    public void badPathsTest(@TempDir Path tempDir) throws IOException {
        //TODO
//        //throws exception when Datastore does not have a studentModelFilePath
//        Datastore datastoreA = new JsonSpringDatastore("author/TestSpringDatastore", );
//        assertThrows(IOException.class, datastoreA::loadStudentModels);
//
//        List<StudentModel> studentModels = new ArrayList<>();
//        assertThrows(IOException.class, () -> datastoreA.saveStudentModels(studentModels));
//
//        //throws exception when the given studentModelFilePath does not exist
//        Datastore datastoreB = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json", "ThisDoesNotExist.json");
//        assertThrows(IOException.class, datastoreB::loadStudentModels);
    }

    @Test
    public void saveAndLoadIndividualStudentTest() throws IOException{
        //TODO
//        Datastore datastore = new JsonSpringDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");
//
//        //load an existing file and make sure it exists
//        StudentModel testUser100 = datastore.loadStudentModel("TestUser100");
//        assertEquals("TestUser100", testUser100.getUserId());
//
//        //try to load in a non-existing file
//        StudentModel notAUser = datastore.loadStudentModel("ThisIsNotAValidUserId");
//        assertNull(notAUser);
    }

    @Test
    public void saveAndLoadStudentModelTest(@TempDir Path tempDir) throws IOException{
        //TODO
//        //load in student models form file
//        Datastore datastoreFromFile = new JsonSpringDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");
//        List<StudentModel> studentModelList = datastoreFromFile.loadStudentModels();
//
//        //verify studentModels from files
//        assertEquals(2, studentModelList.size());
//
//        //save them to new datastore
//        Datastore datastoreToFile = new JsonSpringDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
//        datastoreToFile.saveStudentModels(studentModelList);
//
//        //load them back in to a different datastore
//        Datastore datastore2 = new JsonSpringDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
//        List<StudentModel> studentModelList2 = datastore2.loadStudentModels();
//
//        //verify studentModels
//        assertEquals(2, studentModelList2.size());
    }


}

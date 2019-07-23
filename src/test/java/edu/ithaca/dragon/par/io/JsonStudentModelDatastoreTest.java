package edu.ithaca.dragon.par.io;


import edu.ithaca.dragon.par.studentModel.StudentModel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonStudentModelDatastoreTest {

    @Test
    public void loadBadStudentModelTest() throws IOException{
        //throws exception when Datastore does not have a studentModelFilePath
        Datastore datastoreA = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json");
        assertThrows(IOException.class, datastoreA::loadStudentModels);

        List<StudentModel> studentModels = new ArrayList<>();
        assertThrows(IOException.class, () -> datastoreA.saveStudentModels(studentModels));

        //throws exception when the given studentModelFilePath does not exist
        Datastore datastoreB = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "ThisDoesNotExist.json");
        assertThrows(IOException.class, datastoreB::loadStudentModels);
    }

    @Test
    public void loadAndSaveStudentModelTest(@TempDir Path tempDir) throws IOException{

        //load in student models form file
        Datastore datastoreFromFile = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");
        List<StudentModel> studentModelList = datastoreFromFile.loadStudentModels();

        //verify studentModels from files
        assertEquals(2, studentModelList.size());

        //save them to new datastore
        Datastore datastoreToFile = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
        datastoreToFile.saveStudentModels(studentModelList);

        //load them back in to a different datastore
        Datastore datastore2 = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
        List<StudentModel> studentModelList2 = datastore2.loadStudentModels();

        //verify studentModels
        assertEquals(2, studentModelList2.size());
    }

    @Test
    public void loadIndividualStudentTest() throws IOException{
        Datastore datastore = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");

        //load an existing file and make sure it exists
        StudentModel testUser100 = datastore.loadStudentModel("TestUser100");
        assertEquals("TestUser100", testUser100.getUserId());

        //try to load in a non-existing file
        StudentModel notAUser = datastore.loadStudentModel("ThisIsNotAValidUserId");
        assertNull(notAUser);
    }
}

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
        //throws exception when StudentModelDatastore does not have a studentModelFilePath
        StudentModelDatastore studentModelDatastoreA = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", null);
        assertThrows(IOException.class, studentModelDatastoreA::loadStudentModels);

        List<StudentModel> studentModels = new ArrayList<>();
        assertThrows(IOException.class, () -> studentModelDatastoreA.saveStudentModels(studentModels));

        //throws exception when the given studentModelFilePath does not exist
        StudentModelDatastore studentModelDatastoreB = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "ThisDoesNotExist.json");
        assertThrows(IOException.class, studentModelDatastoreB::loadStudentModels);
    }

    @Test
    public void loadAndSaveStudentModelTest(@TempDir Path tempDir) throws IOException{

        //load in student models form file
        StudentModelDatastore studentModelDatastoreFromFile = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");
        List<StudentModel> studentModelList = studentModelDatastoreFromFile.loadStudentModels();

        //verify studentModels from files
        assertEquals(2, studentModelList.size());

        //save them to new datastore
        StudentModelDatastore studentModelDatastoreToFile = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
        studentModelDatastoreToFile.saveStudentModels(studentModelList);

        //load them back in to a different datastore
        StudentModelDatastore studentModelDatastore2 = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
        List<StudentModel> studentModelList2 = studentModelDatastore2.loadStudentModels();

        //verify studentModels
        assertEquals(2, studentModelList2.size());
    }

    @Test
    public void loadIndividualStudentTest() throws IOException{
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");

        //load an existing file and make sure it exists
        StudentModel testUser100 = studentModelDatastore.loadStudentModel("TestUser100");
        assertEquals("TestUser100", testUser100.getUserId());

        //try to load in a non-existing file
        StudentModel notAUser = studentModelDatastore.loadStudentModel("ThisIsNotAValidUserId");
        assertNull(notAUser);
    }
}

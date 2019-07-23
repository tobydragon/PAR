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
        assertNull(studentModelDatastoreA.getStudentModel("asd"));
    }

    @Test
    public void loadIndividualStudentTest() throws IOException{
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");

        //load an existing file and make sure it exists
        StudentModel testUser100 = studentModelDatastore.getStudentModel("TestUser100");
        assertEquals("TestUser100", testUser100.getUserId());

        //try to load in a non-existing file
        StudentModel notAUser = studentModelDatastore.getStudentModel("ThisIsNotAValidUserId");
        assertNull(notAUser);
    }
}

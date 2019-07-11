package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.StudentModelTest;
import edu.ithaca.dragon.util.JsonSpringUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class JsonDatastoreTest {
    @Test
    public void createQuestionPoolTest() throws IOException{
        //create QuestionPool with JsonDatastore
        QuestionPool myQP = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        //get all questions and check them
        List<Question> myQPList = myQP.getAllQuestions();
        assertTrue(myQPList.size() == 15);
    }

    @Test
    public void loadBadStucentModelTest() throws IOException{
        //throws exception when Datastore does not have a studentModelFilePath
        Datastore datastoreA = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json");
        assertThrows(IOException.class, datastoreA::loadStudentModels);

        List<StudentModel> studentModels = new ArrayList<>();
        assertThrows(IOException.class, () -> datastoreA.saveStudentModels(studentModels));

        //throws exception when the given studentModelFilePath does not exist
        Datastore datastoreB = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json", "ThisDoesNotExist.json");
        assertThrows(IOException.class, datastoreB::loadStudentModels);
    }

    @Test
    public void loadAndSaveStudentModelTest(@TempDir Path tempDir) throws IOException{

        //load in student models form file
        Datastore datastoreFromFile = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");
        List<StudentModel> studentModelList = datastoreFromFile.loadStudentModels();

        //verify studentModels from files
        assertEquals(2, studentModelList.size());

        //save them to new datastore
        Datastore datastoreToFile = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
        datastoreToFile.saveStudentModels(studentModelList);

        //load them back in to a different datastore
        Datastore datastore2 = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
        List<StudentModel> studentModelList2 = datastore2.loadStudentModels();

        //verify studentModels
        assertEquals(2, studentModelList2.size());
    }

    @Test
    public void loadIndividualStudentTest() throws IOException{
        Datastore datastore = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");

        //load an existing file and make sure it exists
        StudentModel testUser100 = datastore.loadStudentModel("TestUser100");
        assertEquals("TestUser100", testUser100.getUserId());

        //try to load in a non-existing file
        StudentModel notAUser = datastore.loadStudentModel("ThisIsNotAValidUserId");
        assertEquals(null, notAUser);
    }
}

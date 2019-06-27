package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.StudentModelTest;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
    public void loadAndSaveStudentModelTest() throws IOException{
        //throws exception when Datastore does not have a studentModelFilePath
        Datastore datastoreA = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json");
        try{
            datastoreA.loadStudentModels();
            fail();
        } catch(Exception e1){

        }
        try{
            List<StudentModel> studentModels = new ArrayList<>();
            datastoreA.saveStudentModels(studentModels);
            fail();
        } catch(Exception e1){

        }

        //load in student models
        Datastore datastore = new JsonDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/students");
        List<StudentModel> studentModelList = datastore.loadStudentModels();

        //check out the studentModels
        assertEquals(2, studentModelList.size());

        fail("Save studentModels back to their files!");











    }
}

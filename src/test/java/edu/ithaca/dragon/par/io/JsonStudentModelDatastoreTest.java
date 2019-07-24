package edu.ithaca.dragon.par.io;


import edu.ithaca.dragon.par.authorModel.ParAuthoringServer;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Test
    public void addQuestionsTest(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentStudentModelsPath = tempDir.resolve("currentStudentModels.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/SampleQuestionPool.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/students"), currentStudentModelsPath, StandardCopyOption.REPLACE_EXISTING);

        Question q = new Question("Question1", "What is this question?", "Good", "A very good one", Arrays.asList("A very good one", "A great one", ":("), "/images/AnImage");
        Question q2 = new Question("Question2", "What is a question?", "Question", "Something important", Arrays.asList("Something important", ":)", ">:/"), "/images/aBetterImage");
        Question q3= new Question("Question3", "What is my purpose?", "Rhetorical", "To be a part of a list for testing", Arrays.asList("Something important", "N/A", "To be a part of a list for testing"), "/images/theBestImage");
        List<Question> questions = new ArrayList<>();
        questions.add(q);
        questions.add(q2);
        questions.add(q3);

        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore(currentQuestionPath.toString(), currentStudentModelsPath.toString());
        assertEquals(15, studentModelDatastore.getStudentModel("TestUser100").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(15, studentModelDatastore.getStudentModel("TestUser101").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(15, studentModelDatastore.getStudentModel("TestUser102").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(15, studentModelDatastore.getAllQuestions().size());

        studentModelDatastore.addQuestions(questions);
        assertEquals(18, studentModelDatastore.getStudentModel("TestUser100").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(18, studentModelDatastore.getStudentModel("TestUser101").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(18, studentModelDatastore.getStudentModel("TestUser102").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(18, studentModelDatastore.getAllQuestions().size());

        assertThrows(RuntimeException.class, ()->{studentModelDatastore.addQuestions(questions);});

    }
}

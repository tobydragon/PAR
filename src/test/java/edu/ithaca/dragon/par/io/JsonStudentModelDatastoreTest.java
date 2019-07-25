package edu.ithaca.dragon.par.io;


import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
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
    public void loadIndividualStudentTest() throws IOException{
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");

        //load an existing file and make sure it exists
        StudentModel testUser100 = studentModelDatastore.getStudentModel("TestUser100");
        assertEquals("TestUser100", testUser100.getUserId());

        //try to load in a non-existing file
        StudentModel testUser103 = studentModelDatastore.getStudentModel("TestUser103");
        assertEquals("TestUser103", testUser103.getUserId());
    }

    @Test
    public void getOrCreateStudentModelTest(@TempDir Path tempDir) throws IOException{
        JsonStudentModelDatastore jsonStudentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", tempDir.toString());
        Path newStudentPath = tempDir.resolve("TestUser100.json");
        Files.copy(Paths.get("src/test/resources/author/students/TestUser100.json"), newStudentPath, StandardCopyOption.REPLACE_EXISTING);

        //load a user that already has a file
        StudentModel studentModel1 = jsonStudentModelDatastore.getOrCreateStudentModel("TestUser100");
        assertEquals("TestUser100", studentModel1.getUserId());

        //load a user that does not have a file
        StudentModel studentModel2 = jsonStudentModelDatastore.getOrCreateStudentModel("NewUser1");
        assertEquals("NewUser1", studentModel2.getUserId());
        Path path1 = Paths.get("src/test/resources/author/students/NewUser1");
        assertFalse(Files.exists(path1)); //a file should not have have been written until an imageTask is submitted

        //make a change to a user, log them out, then reload them to see if changes were saved
        assertEquals(0, studentModel1.getSeenQuestionCount());
        TaskGenerator.makeTask(studentModel1);
        jsonStudentModelDatastore.imageTaskResponseSubmitted(studentModel1.getUserId(), new ImageTaskResponse("TestUser100", Arrays.asList("PlaneQ1"), Arrays.asList("longitudinal")));
        assertEquals(1, studentModel1.getSeenQuestionCount());
        //TODO: assert that studentModel.responseSet got updated when imageTaskResponseSubmitted was called
        jsonStudentModelDatastore.logout("TestUser100");
        StudentModel studentModel3 = jsonStudentModelDatastore.getOrCreateStudentModel("TestUser100");
        assertEquals(1, studentModel3.getSeenQuestionCount());
    }

    @Test
    public void loadAllStudentsTest() throws IOException{
        JsonStudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/SampleQuestionPool.json", "src/test/resources/author/students");
        List<String> usernames = studentModelDatastore.loadAllStudents();
        assertEquals(3, usernames.size());
        assertEquals("TestUser100", usernames.get(0));
        assertEquals("TestUser101", usernames.get(1));
        assertEquals("TestUser102", usernames.get(2));
    }

    @Test
    public void addQuestionsTest(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentStudentModelsPath = tempDir.resolve("currentStudentModels");
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

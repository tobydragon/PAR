package edu.ithaca.dragon.par.io;


import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import edu.ithaca.dragon.util.JsonIoHelperDefault;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
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
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json", "src/test/resources/author/students");

        //load an existing file and make sure it exists
        StudentModel testUser100 = studentModelDatastore.getStudentModel("TestUser100");
        assertEquals("TestUser100", testUser100.getUserId());

        //try to load in a non-existing file
        StudentModel testUser103 = studentModelDatastore.getStudentModel("TestUser103");
        assertEquals("TestUser103", testUser103.getUserId());
    }

    @Test
    public void getOrCreateStudentModelTest(@TempDir Path tempDir) throws IOException{
        JsonStudentModelDatastore jsonStudentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json", tempDir.toString());
        Path newStudentPath = tempDir.resolve("TestUser100.json");
        Files.copy(Paths.get("src/test/resources/author/students/TestUser100.json"), newStudentPath, StandardCopyOption.REPLACE_EXISTING);

        //load a user that already has a file
        StudentModel studentModel1 = jsonStudentModelDatastore.getOrCreateStudentModel("TestUser100");
        assertEquals("TestUser100", studentModel1.getUserId());
        assertEquals(2, studentModel1.getSeenQuestionCount());
        assertEquals(1, studentModel1.getResponseCount());

        //load a user that does not have a file
        StudentModel studentModel2 = jsonStudentModelDatastore.getOrCreateStudentModel("NewUser1");
        assertEquals("NewUser1", studentModel2.getUserId());
        assertEquals(0, studentModel2.getSeenQuestionCount());

        //a file should not have have been written until an imageTask is submitted
        assertFalse(Files.exists(tempDir.resolve("NewUser1.json")));
        TaskGenerator.makeTask(studentModel2);
        assertEquals(1, studentModel2.getSeenQuestionCount());
        jsonStudentModelDatastore.imageTaskResponseSubmitted(studentModel2.getUserId(), new ImageTaskResponse("NewUser1", Arrays.asList("plane./images/demoEquine04.jpg"), Arrays.asList("longitudinal")));
        assertEquals(1, studentModel2.getResponseCount());
        //a file should now been written
        assertTrue(Files.exists(tempDir.resolve("NewUser1.json")));

        //make a change to a user, log them out, then reload them to see if changes were saved
        TaskGenerator.makeTask(studentModel1);
        assertEquals(3, studentModel1.getSeenQuestionCount());
        jsonStudentModelDatastore.imageTaskResponseSubmitted(studentModel1.getUserId(), new ImageTaskResponse("TestUser100", Arrays.asList("plane./images/demoEquine10.jpg"), Arrays.asList("longitudinal")));
        assertEquals(2, studentModel1.getResponseCount());

        jsonStudentModelDatastore.logout("TestUser100");
        StudentModel studentModel3 = jsonStudentModelDatastore.getOrCreateStudentModel("TestUser100");
        assertEquals(3, studentModel3.getSeenQuestionCount());
        assertEquals(2, studentModel3.getResponseCount());
    }

    @Test
    public void getAllSavedStudentIdsTest() throws IOException{
        List<String> usernames = JsonStudentModelDatastore.getAllSavedStudentIds("src/test/resources/author/students");
        assertEquals(3, usernames.size());
        assertEquals("TestUser100", usernames.get(0));
        assertEquals("TestUser101", usernames.get(1));
        assertEquals("TestUser102", usernames.get(2));
    }

    @Test
    public void addQuestionsTest(@TempDir Path tempDir) throws IOException{
        Path currentStudentModelsPath = tempDir.resolve("currentStudentModels");
        assertTrue(new File(currentStudentModelsPath.toString()).mkdir());
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore(
                tempDir.resolve("currentQuestions.json").toString(),
                "src/test/resources/author/DemoQuestionPoolFollowup.json",
                new JsonIoHelperDefault(),
                currentStudentModelsPath.toString());
        assertEquals(47, studentModelDatastore.getStudentModel("TestUser100").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(47, studentModelDatastore.getStudentModel("TestUser101").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(47, studentModelDatastore.getStudentModel("TestUser102").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());

        Question q = new Question("Question1", "What is this question?", "Good", "A very good one", Arrays.asList("A very good one", "A great one", ":("), "/images/AnImage");
        Question q2 = new Question("Question2", "What is a question?", "Question", "Something important", Arrays.asList("Something important", ":)", ">:/"), "/images/aBetterImage");
        Question q3= new Question("Question3", "What is my purpose?", "Rhetorical", "To be a part of a list for testing", Arrays.asList("Something important", "N/A", "To be a part of a list for testing"), "/images/theBestImage");
        List<Question> questions = new ArrayList<>();
        questions.add(q);
        questions.add(q2);
        questions.add(q3);

        studentModelDatastore.addQuestions(questions);

        assertEquals(50, studentModelDatastore.getStudentModel("TestUser100").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(50, studentModelDatastore.getStudentModel("TestUser101").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(50, studentModelDatastore.getStudentModel("TestUser102").getUserQuestionSet().getQuestionCounts().size());
        assertEquals(50, studentModelDatastore.getAllQuestions().size());

        assertThrows(RuntimeException.class, ()->{studentModelDatastore.addQuestions(questions);});
    }

    @Test
    public void checkWindowSizeTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool3.json").getAllQuestions());
        //This qp has 5 of each question

        assertFalse(JsonStudentModelDatastore.isWindowSizeTooBig(0, qp.getAllQuestions()));
        assertFalse(JsonStudentModelDatastore.isWindowSizeTooBig(1, qp.getAllQuestions()));
        assertFalse(JsonStudentModelDatastore.isWindowSizeTooBig(5, qp.getAllQuestions()));
        assertTrue(JsonStudentModelDatastore.isWindowSizeTooBig(6, qp.getAllQuestions()));
        assertTrue(JsonStudentModelDatastore.isWindowSizeTooBig(10, qp.getAllQuestions()));

        QuestionPool qp2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPool2.json").getAllQuestions());
        //qp2 has 10 plane, 27 struct, 6 attachment, and 10 zone

        assertFalse(JsonStudentModelDatastore.isWindowSizeTooBig(0, qp2.getAllQuestions()));
        assertFalse(JsonStudentModelDatastore.isWindowSizeTooBig(5, qp2.getAllQuestions()));
        assertFalse(JsonStudentModelDatastore.isWindowSizeTooBig(6, qp2.getAllQuestions()));
        assertTrue(JsonStudentModelDatastore.isWindowSizeTooBig(10, qp2.getAllQuestions()));
        assertTrue(JsonStudentModelDatastore.isWindowSizeTooBig(27, qp2.getAllQuestions()));
        assertTrue(JsonStudentModelDatastore.isWindowSizeTooBig(100, qp2.getAllQuestions()));
    }
}

package edu.ithaca.dragon.par.io;


import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
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
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

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
        taskGenerator.makeTask(studentModel2, 4);
        assertEquals(1, studentModel2.getSeenQuestionCount());
        jsonStudentModelDatastore.submitImageTaskResponse(studentModel2.getUserId(), new ImageTaskResponseOOP("NewUser1", Arrays.asList("plane./images/demoEquine04.jpg"), Arrays.asList("longitudinal")));
        assertEquals(1, studentModel2.getResponseCount());
        //a file should now been written
        assertTrue(Files.exists(tempDir.resolve("NewUser1.json")));

        //make a change to a user, log them out, then reload them to see if changes were saved
        taskGenerator.makeTask(studentModel1, 4);
        assertEquals(2, studentModel1.getSeenQuestionCount());
        jsonStudentModelDatastore.submitImageTaskResponse(studentModel1.getUserId(), new ImageTaskResponseOOP("TestUser100", Arrays.asList("plane./images/demoEquine10.jpg"), Arrays.asList("longitudinal")));

        assertEquals(2, studentModel1.getResponseCount());

        jsonStudentModelDatastore.logout("TestUser100");
        StudentModel studentModel3 = jsonStudentModelDatastore.getOrCreateStudentModel("TestUser100");
        assertEquals(2, studentModel3.getSeenQuestionCount());
        assertEquals(2, studentModel3.getResponseCount());
    }

    @Test
    public void getAllSavedStudentIdsTest() throws IOException{
        List<String> usernames = JsonStudentModelDatastore.getAllSavedStudentIds("src/test/resources/author/students");
        assertEquals(7, usernames.size());
        assertTrue(usernames.contains("TestUser100"));
        assertTrue(usernames.contains("TestUser101"));
        assertTrue(usernames.contains("TestUser102"));
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

        //TODO: see implementation for details
//        assertThrows(RuntimeException.class, ()->{studentModelDatastore.addQuestions(questions);});
    }

    @Test
    public void calcMinQuestionCountPerTypeTest() throws IOException{
        List<Question> questions = new JsonIoUtil(new JsonIoHelperDefault()).listfromReadOnlyFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        //one zone
        assertEquals(1, JsonStudentModelDatastore.calcMinQuestionCountPerType(questions.subList(0,6)));
        //min two of everything
        assertEquals(2, JsonStudentModelDatastore.calcMinQuestionCountPerType(questions.subList(0,11)));
        //10 images
        assertEquals(10, JsonStudentModelDatastore.calcMinQuestionCountPerType(questions));
        //less followups than anything else
        questions = new JsonIoUtil(new JsonIoHelperDefault()).listfromReadOnlyFile("src/test/resources/author/DemoQuestionPoolFewFollowups.json", Question.class);
        assertEquals(7, JsonStudentModelDatastore.calcMinQuestionCountPerType(questions));

        assertEquals(0, JsonStudentModelDatastore.calcMinQuestionCountPerType(new ArrayList<>()));
    }

    @Test
    public void copyDefaultQuestionsTest(@TempDir Path tempDir) throws IOException{
        Path currentQuestionFile = tempDir.resolve("CurrentTestQuestionFile.json");
        assertFalse(currentQuestionFile.toFile().exists());

        File studentDir = tempDir.resolve("testStudents").toFile();
        studentDir.mkdir();
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore(currentQuestionFile.toString(), "src/test/resources/author/DemoQuestionPool.json", new JsonIoHelperDefault(), studentDir.toString());

        assertTrue(currentQuestionFile.toFile().exists());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());
    }

    @Test
    public void useCurrentQuestionsTest(@TempDir Path tempDir) throws IOException{
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/DemoQuestionPool.json", "bad path", new JsonIoHelperDefault(), tempDir.toString());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());
    }
}

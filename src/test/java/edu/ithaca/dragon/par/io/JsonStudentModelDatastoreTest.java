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
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/questionPools/TestFullQP.json", "src/test/resources/author/students");

        //load an existing file and make sure it exists
        StudentModel student = studentModelDatastore.getStudentModel("PSaASTestUser");
        assertEquals("PSaASTestUser", student.getUserId());

        //try to load in a non-existing file
        StudentModel uhh = studentModelDatastore.getStudentModel("uhh");
        assertEquals("uhh", uhh.getUserId());
    }

    @Test
    public void getOrCreateStudentModelTest(@TempDir Path tempDir) throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        JsonStudentModelDatastore jsonStudentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/questionPools/testFullQP.json", tempDir.toString());
        Path newStudentPath = tempDir.resolve("buckmank.json");
        Files.copy(Paths.get("src/test/resources/author/students/buckmank.json"), newStudentPath, StandardCopyOption.REPLACE_EXISTING);

        //load a user that already has a file
        StudentModel studentModel1 = jsonStudentModelDatastore.getOrCreateStudentModel("buckmank");
        assertEquals("buckmank", studentModel1.getUserId());
        assertEquals(20, studentModel1.getAttemptedQuestionCount());
        assertEquals(20, studentModel1.getResponseCount());

        //load a user that does not have a file
        StudentModel studentModel2 = jsonStudentModelDatastore.getOrCreateStudentModel("NewUser1");
        assertEquals("NewUser1", studentModel2.getUserId());
        assertEquals(0, studentModel2.getAttemptedQuestionCount());

        //a file should not have have been written until an imageTask is submitted
        assertFalse(Files.exists(tempDir.resolve("NewUser1.json")));
        ImageTask it = taskGenerator.makeTask(studentModel2, 4);

        for(Question currQ: it.getTaskQuestions()) {
            studentModel2.increaseTimesAttempted(currQ.getId());
        }

        assertEquals(1, studentModel2.getAttemptedQuestionCount());
        jsonStudentModelDatastore.submitImageTaskResponse(studentModel2.getUserId(), new ImageTaskResponseOOP("NewUser1", Arrays.asList("324-plane-./images/metacarpal56.jpg"), Arrays.asList("longitudinal")), 4);
        assertEquals(1, studentModel2.getResponseCount());
        //a file should now been written
        assertTrue(Files.exists(tempDir.resolve("NewUser1.json")));

        //make a change to a user, log them out, then reload them to see if changes were saved
        it = taskGenerator.makeTask(studentModel1, 4);

        for(Question currQ: it.getTaskQuestions()) {
            studentModel1.increaseTimesAttempted(currQ.getId());
        }

        assertEquals(24, studentModel1.getAttemptedQuestionCount());
        jsonStudentModelDatastore.submitImageTaskResponse(studentModel1.getUserId(), new ImageTaskResponseOOP("buckmank", Arrays.asList("324-plane-./images/metacarpal56.jpg"), Arrays.asList("longitudinal")), 4);

        assertEquals(20, studentModel1.getResponseCount());

        jsonStudentModelDatastore.logout("buckmank");
        StudentModel studentModel3 = jsonStudentModelDatastore.getOrCreateStudentModel("buckmank");
        assertEquals(24, studentModel3.getAttemptedQuestionCount());
        assertEquals(20, studentModel3.getResponseCount());
    }

    @Test
    public void getAllSavedStudentIdsTest() throws IOException{
        List<String> usernames = JsonStudentModelDatastore.getAllSavedStudentIds("src/test/resources/author/students");
        assertEquals(6, usernames.size());
        assertTrue(usernames.contains("masteredStudent"));
        assertTrue(usernames.contains("notMasteredStudent"));
        assertTrue(usernames.contains("buckmank"));
        assertTrue(usernames.contains("PSaASTestUser"));

        assertFalse(usernames.contains("TestUser100"));
    }

    @Test
    public void addQuestionsTest(@TempDir Path tempDir) throws IOException{
        Path currentStudentModelsPath = tempDir.resolve("currentStudentModels");
        assertTrue(new File(currentStudentModelsPath.toString()).mkdir());
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore(
                tempDir.resolve("currentQuestions.json").toString(),
                "src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json",
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
        List<Question> questions = new JsonIoUtil(new JsonIoHelperDefault()).listfromReadOnlyFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        //one zone
        assertEquals(1, JsonStudentModelDatastore.calcMinQuestionCountPerType(questions.subList(0,6)));
        //min two of everything
        assertEquals(2, JsonStudentModelDatastore.calcMinQuestionCountPerType(questions.subList(0,11)));
        //10 images
        assertEquals(10, JsonStudentModelDatastore.calcMinQuestionCountPerType(questions));

        assertEquals(0, JsonStudentModelDatastore.calcMinQuestionCountPerType(new ArrayList<>()));
    }

    @Test
    public void copyDefaultQuestionsTest(@TempDir Path tempDir) throws IOException{
        Path currentQuestionFile = tempDir.resolve("CurrentTestQuestionFile.json");
        assertFalse(currentQuestionFile.toFile().exists());

        File studentDir = tempDir.resolve("testStudents").toFile();
        studentDir.mkdir();
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore(currentQuestionFile.toString(), "src/test/resources/author/questionPools/DemoQuestionPool.json", new JsonIoHelperDefault(), studentDir.toString());

        assertTrue(currentQuestionFile.toFile().exists());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());
    }

    @Test
    public void useCurrentQuestionsTest(@TempDir Path tempDir) throws IOException{
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/questionPools/DemoQuestionPool.json", "bad path", new JsonIoHelperDefault(), tempDir.toString());
        assertEquals(47, studentModelDatastore.getAllQuestions().size());
    }
}

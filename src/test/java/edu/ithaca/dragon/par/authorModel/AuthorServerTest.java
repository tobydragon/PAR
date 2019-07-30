package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.io.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorServerTest {

    @Test
    public void imageTaskResponseSubmittedTest(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/DemoQuestionPoolTemplate.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        assertEquals(0, pas.getQuestionCount());
        assertEquals(47, pas.getQuestionTemplateCount());

        ImageTaskResponse imageTaskResponse1 = new ImageTaskResponse("User1", Arrays.asList("plane./images/demoEquine14.jpg", "structure0./images/demoEquine14.jpg", "AttachQ1", "AttachQ2"), Arrays.asList("longitudinal", "Superficial digital flexor tendon", "AttachType1", "AttachType2"));
        pas.imageTaskResponseSubmitted(imageTaskResponse1);

        assertEquals(45, pas.getQuestionTemplateCount());
        assertEquals(2, pas.getQuestionCount());
        assertEquals(2, pas.findTopLevelQuestionById("structure0./images/demoEquine14.jpg").getFollowupQuestions().size());
        assertEquals("longitudinal", pas.findTopLevelQuestionById("plane./images/demoEquine14.jpg").getCorrectAnswer());

        ImageTaskResponse imageTaskResponse2 = new ImageTaskResponse("User1", Arrays.asList("structure2./images/demoEquine14.jpg", "structure3./images/demoEquine14.jpg", "zone./images/demoEquine14.jpg"), Arrays.asList("Metacarple Bone 3", "Superficial digital flexor tendon", "3a"));
        pas.imageTaskResponseSubmitted(imageTaskResponse2);

        assertEquals(42, pas.getQuestionTemplateCount());
        assertEquals(5, pas.getQuestionCount());

        pas.imageTaskResponseSubmitted(imageTaskResponse1);
        assertEquals(42, pas.getQuestionTemplateCount());
        assertEquals(5, pas.getQuestionCount());
    }

    @Test
    public void nextImageTaskTemplate(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/DemoQuestionPoolTemplate.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        ImageTask imageTask = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        ImageTask imageTask2 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask2.getTaskQuestions().size());
        assertEquals("./images/demoEquine02.jpg", imageTask2.getImageUrl());

        ImageTask imageTask3 = pas.nextImageTaskTemplate();
        assertEquals(4, imageTask3.getTaskQuestions().size());
        assertEquals("./images/demoEquine13.jpg", imageTask3.getImageUrl());

        ImageTask imageTask4 = pas.nextImageTaskTemplate();
        assertEquals(3, imageTask4.getTaskQuestions().size());
        assertEquals("./images/demoEquine04.jpg", imageTask4.getImageUrl());

        ImageTask imageTask5 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask5.getTaskQuestions().size());
        assertEquals("./images/demoEquine10.jpg", imageTask5.getImageUrl());

        ImageTask imageTask6 = pas.nextImageTaskTemplate();
        assertEquals(4, imageTask6.getTaskQuestions().size());
        assertEquals("./images/demoEquine11.jpg", imageTask6.getImageUrl());

        ImageTask imageTask7 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask7.getTaskQuestions().size());
        assertEquals("./images/demoEquine05.jpg", imageTask7.getImageUrl());

        ImageTask imageTask8 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask8.getTaskQuestions().size());
        assertEquals("./images/demoEquine09.jpg", imageTask8.getImageUrl());

        ImageTask imageTask9 = pas.nextImageTaskTemplate();
        assertEquals(3, imageTask9.getTaskQuestions().size());
        assertEquals("./images/demoEquine37.jpg", imageTask9.getImageUrl());

        ImageTask imageTask10 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask10.getTaskQuestions().size());
        assertEquals("./images/demoEquine32.jpg", imageTask10.getImageUrl());

        //At this point, every single question has been seen. The system successfully loops.

        ImageTask imageTask11 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask11.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask11.getImageUrl());

        ImageTask imageTask12 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask12.getTaskQuestions().size());
        assertEquals("./images/demoEquine02.jpg", imageTask12.getImageUrl());
    }

    @Test
    public void endOfTasksTest(@TempDir Path tempDir) throws IOException{
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/DemoQuestionPoolTemplateSmall.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        ImageTask imageTask = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());
        assertEquals(5, imageTask.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponse("Author1", Arrays.asList("plane./images/demoEquine14.jpg", "structure0./images/demoEquine14.jpg", "AttachQ1", "AttachQ2"), Arrays.asList("longitudinal", "Superficial digital flexor endon", "Type1", "Type2")));

        ImageTask imageTask2 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine04.jpg", imageTask2.getImageUrl());
        assertEquals(3, imageTask2.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponse("Author1", Arrays.asList("plane./images/demoEquine04.jpg", "structure0./images/demoEquine04.jpg", "AttachQ5", "zone./images/demoEquine04.jpg"), Arrays.asList("longitudinal", "Superficial digital flexor endon", "Type1", "3c")));

        ImageTask imageTask3 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask3.getImageUrl());
        assertEquals(3, imageTask3.getTaskQuestions().size());

        ImageTask imageTask4 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask4.getImageUrl());
        assertEquals(3, imageTask3.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponse("Author1", Arrays.asList("structure2./images/demoEquine14.jpg","structure3./images/demoEquine14.jpg", "zone./images/demoEquine14.jpg"), Arrays.asList("Suspensory ligament (branches)", "Proximal sesamoid bones", "1A")));

        //no more template questions
        ImageTask imageTask5 = pas.nextImageTaskTemplate();
        assertEquals("noMoreQuestions", imageTask5.getImageUrl());
        assertEquals(0, imageTask5.getTaskQuestions().size());
    }

}

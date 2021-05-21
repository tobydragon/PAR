package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.deprecated.io.*;
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

public class AuthorServerTest {

    @Test
    public void imageTaskResponseSubmittedTest(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/questionPools/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/questionPools/DemoQuestionPoolTemplate.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        assertEquals(0, pas.getQuestionCount());
        assertEquals(47, pas.getQuestionTemplateCount());

        ImageTaskResponseOOP imageTaskResponseImp11 = new ImageTaskResponseOOP("User1", Arrays.asList("plane./images/demoEquine14.jpg", "structure0./images/demoEquine14.jpg", "AttachQ1", "AttachQ2"), Arrays.asList("longitudinal", "Superficial digital flexor tendon", "AttachType1", "AttachType2"));
        pas.imageTaskResponseSubmitted(imageTaskResponseImp11);

        assertEquals(45, pas.getQuestionTemplateCount());
        assertEquals(2, pas.getQuestionCount());
        assertEquals(2, pas.findTopLevelQuestionById("structure0./images/demoEquine14.jpg").getFollowupQuestions().size());
        assertEquals("longitudinal", pas.findTopLevelQuestionById("plane./images/demoEquine14.jpg").getCorrectAnswer());

        ImageTaskResponseOOP imageTaskResponseImp12 = new ImageTaskResponseOOP("User1", Arrays.asList("structure2./images/demoEquine14.jpg", "structure3./images/demoEquine14.jpg", "zone./images/demoEquine14.jpg"), Arrays.asList("Metacarple Bone 3", "Superficial digital flexor tendon", "3a"));
        pas.imageTaskResponseSubmitted(imageTaskResponseImp12);

        assertEquals(42, pas.getQuestionTemplateCount());
        assertEquals(5, pas.getQuestionCount());

        pas.imageTaskResponseSubmitted(imageTaskResponseImp11);
        assertEquals(42, pas.getQuestionTemplateCount());
        assertEquals(5, pas.getQuestionCount());
    }

    @Test
    public void nextImageTaskTemplateTest(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/questionPools/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/questionPools/DemoQuestionPoolTemplate.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

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
    public void getImageTaskTemplateTest(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/questionPools/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/questionPools/DemoQuestionPoolTemplate.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        ImageTask imageTask = pas.getImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        imageTask = pas.getImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        imageTask = pas.getImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        imageTask = pas.getImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        imageTask = pas.getImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        imageTask = pas.getImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        imageTask = pas.getImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());
    }

    @Test
    public void getImageTaskTemplateAndUpdateAuthorTimesAttemptedTest(@TempDir Path tempDir) throws IOException{
        //This test demonstrates that the combination of getImageTaskTemplate and
        //UpdateAuthorTimesAttempted works the same as nextImageTaskTemplate


        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/questionPools/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/questionPools/DemoQuestionPoolTemplate.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        ImageTask imageTask = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());

        List<String> ids = new ArrayList<>();
        for(Question q: imageTask.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask2 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask2.getTaskQuestions().size());
        assertEquals("./images/demoEquine02.jpg", imageTask2.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask2.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask3 = pas.nextImageTaskTemplate();
        assertEquals(4, imageTask3.getTaskQuestions().size());
        assertEquals("./images/demoEquine13.jpg", imageTask3.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask3.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask4 = pas.nextImageTaskTemplate();
        assertEquals(3, imageTask4.getTaskQuestions().size());
        assertEquals("./images/demoEquine04.jpg", imageTask4.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask4.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask5 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask5.getTaskQuestions().size());
        assertEquals("./images/demoEquine10.jpg", imageTask5.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask5.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask6 = pas.nextImageTaskTemplate();
        assertEquals(4, imageTask6.getTaskQuestions().size());
        assertEquals("./images/demoEquine11.jpg", imageTask6.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask6.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask7 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask7.getTaskQuestions().size());
        assertEquals("./images/demoEquine05.jpg", imageTask7.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask7.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask8 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask8.getTaskQuestions().size());
        assertEquals("./images/demoEquine09.jpg", imageTask8.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask8.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask9 = pas.nextImageTaskTemplate();
        assertEquals(3, imageTask9.getTaskQuestions().size());
        assertEquals("./images/demoEquine37.jpg", imageTask9.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask9.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask10 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask10.getTaskQuestions().size());
        assertEquals("./images/demoEquine32.jpg", imageTask10.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask10.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        //At this point, every single question has been seen. The system successfully loops.

        ImageTask imageTask11 = pas.nextImageTaskTemplate();
        assertEquals(5, imageTask11.getTaskQuestions().size());
        assertEquals("./images/demoEquine14.jpg", imageTask11.getImageUrl());

        ids = new ArrayList<>();
        for(Question q: imageTask11.getTaskQuestions()){
            ids.add(q.getId());
        }
        pas.updateAuthorTimesAttempted(ids);

        ImageTask imageTask12 = pas.nextImageTaskTemplate();
        assertEquals(6, imageTask12.getTaskQuestions().size());
        assertEquals("./images/demoEquine02.jpg", imageTask12.getImageUrl());
    }

    @Test
    public void endOfTasksTest(@TempDir Path tempDir) throws IOException{
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/questionPools/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/questionPools/DemoQuestionPoolTemplateSmall.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        ImageTask imageTask = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask.getImageUrl());
        assertEquals(5, imageTask.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponseOOP("Author1", Arrays.asList("plane./images/demoEquine14.jpg", "structure0./images/demoEquine14.jpg", "AttachQ1", "AttachQ2"), Arrays.asList("longitudinal", "Superficial digital flexor endon", "Type1", "Type2")));

        ImageTask imageTask2 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine04.jpg", imageTask2.getImageUrl());
        assertEquals(3, imageTask2.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponseOOP("Author1", Arrays.asList("plane./images/demoEquine04.jpg", "structure0./images/demoEquine04.jpg", "AttachQ5", "zone./images/demoEquine04.jpg"), Arrays.asList("longitudinal", "Superficial digital flexor endon", "Type1", "3c")));

        ImageTask imageTask3 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask3.getImageUrl());
        assertEquals(3, imageTask3.getTaskQuestions().size());

        ImageTask imageTask4 = pas.nextImageTaskTemplate();
        assertEquals("./images/demoEquine14.jpg", imageTask4.getImageUrl());
        assertEquals(3, imageTask3.getTaskQuestions().size());
        pas.imageTaskResponseSubmitted(new ImageTaskResponseOOP("Author1", Arrays.asList("structure2./images/demoEquine14.jpg","structure3./images/demoEquine14.jpg", "zone./images/demoEquine14.jpg"), Arrays.asList("Suspensory ligament (branches)", "Proximal sesamoid bones", "1A")));

        //no more template questions
        ImageTask imageTask5 = pas.nextImageTaskTemplate();
        assertEquals("noMoreQuestions", imageTask5.getImageUrl());
        assertEquals(0, imageTask5.getTaskQuestions().size());
    }

    @Test
    public void ignoreCaseForSubmittedAnswersTest(@TempDir Path tempDir) throws IOException{
        //make paths for copies of the files
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        //copy the files to use to the paths (these temp files will change as work is done)
        Files.copy(Paths.get("src/test/resources/author/questionPools/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/questionPools/DemoQuestionPoolTemplate.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        assertEquals(0, pas.getQuestionCount());
        assertEquals(47, pas.getQuestionTemplateCount());

        ImageTaskResponseOOP imageTaskResponse1 = new ImageTaskResponseOOP("User1", Arrays.asList("plane./images/demoEquine14.jpg", "structure0./images/demoEquine14.jpg", "AttachQ1", "AttachQ2"), Arrays.asList("LongiTudinal", "Superficial digital flexor tendon", "AttachType1", "AttachType2"));
        pas.imageTaskResponseSubmitted(imageTaskResponse1);

        //Even though a response was submitted with Caps, it should be stored all lower case
        assertEquals("longitudinal", pas.findTopLevelQuestionById("plane./images/demoEquine14.jpg").getCorrectAnswer());
    }

    @Test
    public void customQuestionSubmissionTest(@TempDir Path tempDir) throws IOException{
        //set up authorDatastore files and the AuthorServer
        Path currentQuestionPath = tempDir.resolve("currentQuestions.json");
        Path currentQuestionTemplatePath = tempDir.resolve("currentQuestionTemplates.json");
        Files.copy(Paths.get("src/test/resources/author/questionPools/SampleQuestionsEmpty.json"), currentQuestionPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/author/questionPools/DemoQuestionPoolTemplateWithCustom.json"), currentQuestionTemplatePath, StandardCopyOption.REPLACE_EXISTING);

        AuthorServer pas = new AuthorServer(new JsonAuthorDatastore(currentQuestionPath.toString(),
                currentQuestionTemplatePath.toString(), tempDir.resolve("currentAuthorModel.json").toString()));

        assertEquals(0, pas.getQuestionCount());
        assertEquals(48, pas.getQuestionTemplateCount());

        //submit an itr that has normal questions and a custom question
        QuestionResponseOOP qr1 = new QuestionResponseOOP("plane./images/demoEquine14.jpg", "longitudinal");
        QuestionResponseOOP qr2 = new QuestionResponseOOP("custom0./images/demoEquine14.jpg", "Is this a custom question?", "Yes, it is!");
        ImageTaskResponseOOP itr1 = new ImageTaskResponseOOP();
        itr1.setQuestionResponses(new ArrayList<>(Arrays.asList(qr1, qr2)));
        pas.imageTaskResponseSubmitted(itr1);

        assertEquals(2, pas.getQuestionCount());
        assertEquals(46, pas.getQuestionTemplateCount());
        assertEquals("On which plane is the ultrasound taken?", pas.findTopLevelQuestionById("plane./images/demoEquine14.jpg").getQuestionText());
        assertEquals("longitudinal", pas.findTopLevelQuestionById("plane./images/demoEquine14.jpg").getCorrectAnswer());
        assertEquals("Is this a custom question?", pas.findTopLevelQuestionById("custom0./images/demoEquine14.jpg").getQuestionText());
        assertEquals("yes, it is!", pas.findTopLevelQuestionById("custom0./images/demoEquine14.jpg").getCorrectAnswer());





        //TODO: this was written to test if already submitted questions could have their answers changed, and proves that they currently cannot
//        //submit a normal itr
//        ImageTaskResponseOOP itr1 = new ImageTaskResponseOOP("User1", Arrays.asList("plane./images/demoEquine14.jpg"), Arrays.asList("longitudinal"));
//        pas.imageTaskResponseSubmitted(itr1);
//        assertEquals(1, pas.getQuestionCount());
//        assertEquals(47, pas.getQuestionTemplateCount());
//        assertEquals("longitudinal", pas.findTopLevelQuestionById("plane./images/demoEquine14.jpg").getCorrectAnswer());
//
//        //re-answer a question and check if the answer has changed
//        ImageTaskResponseOOP itr2 = new ImageTaskResponseOOP("User1", Arrays.asList("plane./images/demoEquine14.jpg"), Arrays.asList("transverse"));
//        pas.imageTaskResponseSubmitted(itr2);
//        assertEquals(1, pas.getQuestionCount());
//        assertEquals(47, pas.getQuestionTemplateCount());
//        assertEquals("transverse", pas.findTopLevelQuestionById("plane./images/demoEquine14.jpg").getCorrectAnswer());


    }
}

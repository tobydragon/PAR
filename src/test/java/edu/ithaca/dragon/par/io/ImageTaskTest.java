package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ImageTaskTest {

    @Test
    public void toJsonAndBackTest() throws IOException {
        QuestionPool myQP = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        List<Question> myQPList = myQP.getAllQuestions();
        ImageTask testImageTask = new ImageTask("../static/images/equine02.jpg\"", myQPList);

        //write to JSON
        JsonUtil.toJsonFile("src/test/resources/autoGenerated/ImageTaskTest-toJsonAndBackTest.json", testImageTask);
        //read from JSON
        ImageTask testImageTaskIn = JsonUtil.fromJsonFile("src/test/resources/autoGenerated/ImageTaskTest-toJsonAndBackTest.json",ImageTask.class);
        //compare
        assertTrue(testImageTask.equals(testImageTaskIn));

        Path path = Paths.get("src/test/resources/autoGenerated/ImageTaskTest-toJsonAndBackTest.json");
        Assertions.assertTrue(Files.deleteIfExists(path));

    }
}

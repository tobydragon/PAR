package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageTaskResponseOOPTest {

    @Test
    public void findResponseToQuestionTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json").getAllQuestions());
        ImageTaskResponseOOP responseSet = new ImageTaskResponseOOP("response1", Arrays.asList("plane./images/demoEquine14.jpg","structure0./images/demoEquine14.jpg", "AttachQ1"),Arrays.asList("Lateral","bone","3c"));

        //test for sucsessfully getting the answer to a parent question
        assertEquals("Lateral", responseSet.findResponseToQuestion(qp.getQuestionFromId("plane./images/demoEquine14.jpg")));
        assertEquals("bone", responseSet.findResponseToQuestion(qp.getQuestionFromId("structure0./images/demoEquine14.jpg")));
        assertEquals("3c", responseSet.findResponseToQuestion(qp.getQuestionFromId("AttachQ1")));

        //getting null for a question that doesn't exist
        assertEquals(null, responseSet.findResponseToQuestion(new Question("notAValidQuestion", "notAValidQuestion", "notAValidQuestion", "notAValidQuestion", Arrays.asList("notAValidQuestion","notAValidQuestion2"), "notAValidQuestion")));
    }

    @Test
    public void toJsonAndBackTest(@TempDir Path tempDir) throws IOException {
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json").getAllQuestions());
        ImageTaskResponseOOP responseSet = new ImageTaskResponseOOP("response1", Arrays.asList("plane./images/demoEquine14.jpg","structure0./images/demoEquine14.jpg", "AttachQ1"),Arrays.asList("Lateral","bone","3c"));

        Path path = tempDir.resolve("ImageTaskResponseOOP.json");
        JsonUtil.toJsonFile(path.toString(), responseSet);

        ImageTaskResponseOOP newResponseSet = JsonUtil.fromJsonFile(path.toString(), ImageTaskResponseOOP.class);
        assertEquals(responseSet.getUserId(), newResponseSet.getUserId());
        assertEquals(responseSet.getResponseTexts().size(), newResponseSet.getResponseTexts().size());
        JsonUtil.toJsonFile("src/test/resources/author/DemoImageTaskResponseOOP.json", responseSet);
    }
}

package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageTaskResponseOOPTest {

    @Test
    public void findResponseToQuestionTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        ImageTaskResponseOOP responseSet = new ImageTaskResponseOOP("response1", Arrays.asList("plane./images/demoEquine14.jpg","structure0./images/demoEquine14.jpg", "AttachQ1"),Arrays.asList("Lateral","bone","3c"));

        //test for sucsessfully getting the answer to a parent question
        assertEquals("Lateral", responseSet.findResponseToQuestion(qp.getQuestionFromId("plane./images/demoEquine14.jpg")));
        assertEquals("bone", responseSet.findResponseToQuestion(qp.getQuestionFromId("structure0./images/demoEquine14.jpg")));
        assertEquals("3c", responseSet.findResponseToQuestion(qp.getQuestionFromId("AttachQ1")));

        //getting null for a question that doesn't exist
        assertEquals(null, responseSet.findResponseToQuestion(new Question("notAValidQuestion", "notAValidQuestion", "notAValidQuestion", "notAValidQuestion", Arrays.asList("notAValidQuestion","notAValidQuestion2"), "notAValidQuestion")));
    }
}

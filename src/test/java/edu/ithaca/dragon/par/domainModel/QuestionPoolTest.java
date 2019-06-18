package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class QuestionPoolTest {
    @Test
    public void getQuestionsFromUrlTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions.json"));
        List<Question> questionsFromUrl = qp.getQuestionsFromUrl("../static/images/demoEquine04.jpg");
        assertTrue(questionsFromUrl.size() == 5);
    }
}

package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class QuestionPoolTest {
    @Test
    public void getQuestionsFromUrlTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions2.json"));
        List<Question> questionsFromUrl = qp.getQuestionsFromUrl("../static/images/demoEquine04.jpg");
        assertTrue(questionsFromUrl.size() == 5);

        List<Question> questionsFromUrl2 = qp.getQuestionsFromUrl("../static/images/demoEquine02.jpg");
        assertTrue(questionsFromUrl2.size() == 2);

        List<Question> questionsFromUrl3 = qp.getQuestionsFromUrl("notAGoodUrl");
        assertTrue(questionsFromUrl3.size() == 0);
    }

    @Test
    public void getQuestionFromId() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions2.json"));
        Question questionFromId = qp.getQuestionFromId("PlaneQ3");
        assertTrue(questionFromId.getQuestionText().equals("On which plane is the ultrasound taken?"));
        assertTrue(questionFromId.getCorrectAnswer().equals("Lateral"));

        Question questionFromId2 = qp.getQuestionFromId("StructureQ4");
        assertTrue(questionFromId.getQuestionText().equals("On which plane is the ultrasound taken?"));
        assertTrue(questionFromId.getCorrectAnswer().equals("Lateral"));
    }
}

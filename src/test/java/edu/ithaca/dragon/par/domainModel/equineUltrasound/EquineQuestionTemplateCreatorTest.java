package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquineQuestionTemplateCreatorTest {

    @Test
    void createQuestionsForImageTest() throws IOException {
        List<Question> questions = EquineQuestionTemplateCreator.createQuestionsForImage("equineTest.jpg");
        assertEquals(6, questions.size());
        System.out.println(JsonUtil.toJsonString(questions));

    }
}
package edu.ithaca.dragon.par.deprecated.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.domain.Question;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EquineQuestionTemplateCreatorTest {

    @Test
    void createQuestionsForImageTest() throws IOException {
        List<Question> questions = EquineQuestionTemplateCreator.createQuestionsForImage("equineTest.jpg");
        assertEquals(6, questions.size());

    }

    @Test
    void createQuestionsForImageListTest(){
        List<Question> questions = EquineQuestionTemplateCreator.createQuestionsForImageList(Arrays.asList("equineTest1.jpg","equineTest2.jpg", "equineTest3.jpg" ));
        assertEquals(18, questions.size());
    }

    @Test
    void createAttachmentQuestionsListTest(){
        List<Question> questions = EquineQuestionTemplateCreator.createAttachmentQuestionsList("equineTest.jpg", "structure1-equineTest.jpg");
        assertEquals(2, questions.size());
    }
}
package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquineQuestionTemplateCreatorTest {

    @Test
    void createQuestionsForImageTest() throws IOException {
        List<Question> questions = EquineQuestionTemplateCreator.createQuestionsForImage("equineTest.jpg");
        assertEquals(6, questions.size());

        //Create Questions with null question and answer Text
        List<Question> questions2 = EquineQuestionTemplateCreator.createQuestionsForImage("equineTest2.jpg", 2);
        assertEquals(8, questions2.size());
        List<Question> questions3 = EquineQuestionTemplateCreator.createQuestionsForImage("equineTest3.jpg", 4);
        assertEquals(10, questions3.size());
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
        System.out.println(questions.get(0));
    }
}
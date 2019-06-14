package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;
import edu.ithaca.dragon.util.JsonUtil;


import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImageTaskChooserTest {

    @Test
    public void nextImageTaskTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("2", questionsFromFile);
        Question q1 = ImageTaskChooser.nextImageTask(q, 1);
        assertEquals(q1.getDifficulty(), 1);
        int len = q.getLenOfSeenQuestions();
        assertEquals(1, len);

        Question q2 = ImageTaskChooser.nextImageTask(q, 2);
        assertEquals(q2.getDifficulty(), 2);
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        Question q3 = ImageTaskChooser.nextImageTask(q, 3);
        assertNull(q3);
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        Question q4 = ImageTaskChooser.nextImageTask(q, 4);
        assertEquals(q4.getDifficulty(), 4);
        len = q.getLenOfSeenQuestions();
        assertEquals(3, len);

    }

}

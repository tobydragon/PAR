package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ImageTaskChooserTest {

    @Test
    public void nextImageTaskTest(){
        UserQuestionSet q = new UserQuestionSet("2");
        ImageTaskChooser task1 = new ImageTaskChooser(q, 1);
        Question q1 = task1.nextImageTask();
        assertEquals(q1.getDifficulty(), 1);
        int len = q.getLenOfSeenQuestions();
        assertEquals(1, len);

        ImageTaskChooser task2 = new ImageTaskChooser(q, 2);
        Question q2 = task2.nextImageTask();
        assertEquals(q2.getDifficulty(), 2);
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        ImageTaskChooser task3 = new ImageTaskChooser(q, 3);
        Question q3 = task3.nextImageTask();
        assertEquals(q3.getDifficulty(), 3);
        len = q.getLenOfSeenQuestions();
        assertEquals(3, len);

        ImageTaskChooser task4 = new ImageTaskChooser(q, 4);
        Question q4 = task4.nextImageTask();
        assertEquals(null, q4);
        len = q.getLenOfSeenQuestions();
        assertEquals(3, len);

    }

}

package edu.ithaca.dragon.par.deprecated.studentModel;

import edu.ithaca.dragon.par.domain.Question;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class QuestionCountTest {

    @Test
    public void timesAttemptedTest(){
        Question q1 = new Question("1","what plane?", "plane", "transverse", Arrays.asList("transverse", "longitudinal"), "/imageURL");
        QuestionCount qc1 = new QuestionCount(q1);

        assertEquals(0, qc1.getTimesAttempted());
        qc1.increaseTimesAttempted();
        assertEquals(1, qc1.getTimesAttempted());

        qc1.increaseTimesAttempted();
        qc1.increaseTimesAttempted();
        qc1.increaseTimesAttempted();
        qc1.increaseTimesAttempted();
        assertEquals(5, qc1.getTimesAttempted());
        assertEquals(5, qc1.getTimesAttempted());
        qc1.increaseTimesAttempted();
        assertEquals(6, qc1.getTimesAttempted());
    }

    @Test
    public void getQuestionTest(){
        Question q1 = new Question("1","what plane?", "plane", "transverse", Arrays.asList("transverse", "longitudinal"), "/imageURL");
        QuestionCount qc1 = new QuestionCount(q1);

        Question q2 = new Question("2","what plane?", "plane", "longitudinal", Arrays.asList("transverse", "longitudinal"), "/imageURL2");
        QuestionCount qc2 = new QuestionCount(q2);

        assertEquals(q1, qc1.getQuestion());
        assertEquals(q2, qc2.getQuestion());
    }

    @Test
    public void equalsTest(){
        Question q1 = new Question("1","what plane?", "plane", "transverse", Arrays.asList("transverse", "longitudinal"), "/imageURL");
        QuestionCount qc1 = new QuestionCount(q1);

        Question q2 = new Question("2","what plane?", "plane", "longitudinal", Arrays.asList("transverse", "longitudinal"), "/imageURL2");
        QuestionCount qc2 = new QuestionCount(q2);

        assertEquals(qc1, qc1);
        assertNotEquals(qc1, qc2);

        Question q3 = new Question("1","what plane?", "plane", "transverse", Arrays.asList("transverse", "longitudinal"), "/imageURL");
        QuestionCount qc3 = new QuestionCount(q3);
        assertEquals(qc1, qc3);
        qc3.increaseTimesAttempted();
        assertNotEquals(qc1, qc3);

    }
}

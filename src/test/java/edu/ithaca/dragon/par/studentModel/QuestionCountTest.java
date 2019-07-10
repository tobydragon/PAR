package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionCountTest {

    @Test
    public void timesSeenTest(){
        Question q1 = new Question("1","what plane?", "plane", "transverse", Arrays.asList("transverse", "longitudinal"), "/imageURL");
        QuestionCount qc1 = new QuestionCount(q1);

        assertEquals(0, qc1.getTimesSeen());
        qc1.increaseTimesSeen();
        assertEquals(1, qc1.getTimesSeen());

        qc1.increaseTimesSeen();
        qc1.increaseTimesSeen();
        qc1.increaseTimesSeen();
        qc1.increaseTimesSeen();
        assertEquals(5, qc1.getTimesSeen());
        assertEquals(5, qc1.getTimesSeen());
        qc1.increaseTimesSeen();
        assertEquals(6, qc1.getTimesSeen());
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
}

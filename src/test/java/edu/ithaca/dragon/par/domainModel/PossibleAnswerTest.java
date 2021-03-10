package edu.ithaca.dragon.par.domainModel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PossibleAnswerTest {

    @Test
    public void createPossibleAnswerTest(){
        PossibleAnswer pa = new PossibleAnswer("answerHere", "my feedback");
        PossibleAnswer pa2 = new PossibleAnswer("answerOnly");

        assertEquals("answerHere", pa.getAnswerText());
        assertEquals("my feedback", pa.getFeedbackText());

        assertEquals("answerOnly", pa2.getAnswerText());
        assertEquals(null, pa2.getFeedbackText());
    }

}

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

    @Test
    public void toStringTest(){
        PossibleAnswer pa = new PossibleAnswer("answerHere", "my feedback");
        PossibleAnswer pa2 = new PossibleAnswer("answerOnly");

        assertEquals("PossibleAnswer [answerText=answerHere, feedbackText=my feedback]", pa.toString());
        assertEquals("PossibleAnswer [answerText=answerOnly, feedbackText=null]", pa2.toString());
    }

    @Test
    public void equalsTest(){
        PossibleAnswer pa = new PossibleAnswer("answerHere", "my feedback");
        PossibleAnswer pa2 = new PossibleAnswer("answerHere");
        PossibleAnswer pa3 = new PossibleAnswer("answerHere", "my feedback");
        PossibleAnswer pa4 = new PossibleAnswer("answerHere", "feedback");
        PossibleAnswer pa5 = new PossibleAnswer("theanswer", "my feedback");

        assertTrue(pa.equals(pa3));
        assertFalse(pa.equals(pa2));
        assertFalse(pa.equals(pa4));
        assertFalse(pa.equals(pa5));
        assertFalse(pa3.equals(pa4));
    }

}

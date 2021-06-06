package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.student.json.QuestionHistoryTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class QuestionHistorySummaryTest {

    @Test
    public void createHistorySummaryTest(){
        fail();
        //QuestionHistorySummary questionHistorySummary = new QuestionHistorySummary(QuestionHistoryTest.makeExamples());
    }

    @Test
    public void buildQuestionIdsSeenTest(){
        List<String> questionIdsSeen = QuestionHistorySummary.buildQuestionIdsSeen(QuestionHistoryTest.makeExamples());
        assertEquals(5, questionIdsSeen.size());
        assertEquals("q1", questionIdsSeen.get(0));
        assertEquals("q2", questionIdsSeen.get(1));
        assertEquals("q3", questionIdsSeen.get(2));
        assertEquals("q4", questionIdsSeen.get(3));
        assertEquals("q5", questionIdsSeen.get(4));
    }

    @Test
    public void checkQuestionsAnsweredTest(){
        List<String> questionsAnswered = QuestionHistorySummary.checkQuestionsAnswered(QuestionHistoryTest.makeExamples());
        assertEquals(3, questionsAnswered.size());
        assertEquals("q1", questionsAnswered.get(0));
        assertEquals("q3", questionsAnswered.get(1));
        assertEquals("q5", questionsAnswered.get(2));
    }

}
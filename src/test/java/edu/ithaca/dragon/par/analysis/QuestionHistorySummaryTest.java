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
        assertEquals(3, questionIdsSeen.size());
        assertEquals("q1", questionIdsSeen.get(0));
        assertEquals("q2", questionIdsSeen.get(1));
        assertEquals("q3", questionIdsSeen.get(1));
    }

    @Test
    public void buildQuestionsCorrectFirstTime(){
        List<String> questionsCorrectFirstTime = QuestionHistorySummary.buildQuestionsCorrectFirstTime(QuestionHistoryTest.makeExamples());
        assertEquals(1, questionsCorrectFirstTime.size());
        assertEquals("q2", questionsCorrectFirstTime.get(0));
    }

}
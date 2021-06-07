package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
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
    public void checkQuestionsRespondedTest(){
        List<String> questionsResponded = QuestionHistorySummary.checkQuestionsResponded(QuestionHistoryTest.makeExamples());
        assertEquals(3, questionsResponded.size());
        assertEquals("q1", questionsResponded.get(0));
        assertEquals("q3", questionsResponded.get(1));
        assertEquals("q5", questionsResponded.get(2));
    }

    @Test
    public void findQuestionsCorrectTest(){
        DomainDatasourceJson data = new DomainDatasourceJson
        (
            id, 
            questionFilePath, 
            defaultQuestionReadOnlyFilePath, 
            jsonIoHelper
        );

        List<String> questionsCorrect = QuestionHistorySummary.findQuestionsCorrect(QuestionHistoryTest.makeExamples(), data);

        // Example Question 1: What color is the sky?
        assertEquals("blue", questionsCorrect.get(0));
        // Example Question 2: What is 1 + 1?
        assertEquals("2", questionsCorrect.get(1));
        // Example Question 3: What is your major?
        assertEquals("CS", questionsCorrect.get(2));
    }

}
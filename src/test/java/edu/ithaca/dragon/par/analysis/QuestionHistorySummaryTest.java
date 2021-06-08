package edu.ithaca.dragon.par.analysis;

import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceSimple;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.json.QuestionHistoryTest;
import edu.ithaca.dragon.util.JsonIoHelperSpring;
import edu.ithaca.dragon.util.JsonUtil;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
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
        List<String> questionsResponded = QuestionHistorySummary.checkQuestionsRespondedTo(QuestionHistoryTest.makeExamples());
        assertEquals(3, questionsResponded.size());
        assertEquals("q1", questionsResponded.get(0));
        assertEquals("q3", questionsResponded.get(1));
        assertEquals("q5", questionsResponded.get(2));
    }

    @Test
    public void findQuestionsCorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> questionsCorrect = QuestionHistorySummary.findQuestionsCorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(3, data.getAllQuestions().size());

        assertEquals(2, questionsCorrect.size());

        // Sample Question 1: What color is the sky?
        assertEquals("blue", questionsCorrect.get(0));

        // Sample Question 2: What is 1 + 1?
        assertEquals("2", questionsCorrect.get(1));
    }

}
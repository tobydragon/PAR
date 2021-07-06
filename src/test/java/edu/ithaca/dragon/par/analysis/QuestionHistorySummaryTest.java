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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

class QuestionHistorySummaryTest {

    @Test
    public void createQuestionHistorySummary() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
 
        QuestionHistorySummary poorPerformance = new QuestionHistorySummary(QuestionHistoryTest.poorStudent(), data);
        assertEquals(5, poorPerformance.getQuestionIdsSeen().size());
        assertEquals(2, poorPerformance.getQuestionIdsRespondedTo().size());
        assertEquals(0, poorPerformance.getQuestionIdsCorrectFirstTime().size());
        assertEquals(2, poorPerformance.getQuestionIdsIncorrect().size());
        assertEquals(3, poorPerformance.getQuestionIdsSeen().size() - poorPerformance.getQuestionIdsRespondedTo().size());
        assertEquals("majorQ", poorPerformance.getQuestionIdsIncorrect().get(0));
        assertEquals("yearQ", poorPerformance.getQuestionIdsIncorrect().get(1));

        QuestionHistorySummary strongPerformance = new QuestionHistorySummary(QuestionHistoryTest.strongStudent(), data);
        assertEquals(5, strongPerformance.getQuestionIdsSeen().size());
        assertEquals(5, strongPerformance.getQuestionIdsRespondedTo().size());
        assertEquals(5, strongPerformance.getQuestionIdsCorrectFirstTime().size());
        assertEquals("skyQ", strongPerformance.getQuestionIdsCorrectFirstTime().get(0));
        assertEquals("mathQ", strongPerformance.getQuestionIdsCorrectFirstTime().get(1));
        assertEquals("majorQ", strongPerformance.getQuestionIdsCorrectFirstTime().get(2));
        assertEquals("yearQ", strongPerformance.getQuestionIdsCorrectFirstTime().get(3));
        assertEquals("googleQ", strongPerformance.getQuestionIdsCorrectFirstTime().get(4));

        QuestionHistorySummary improvingPerformance = new QuestionHistorySummary(QuestionHistoryTest.improvingStudent(), data);
        assertEquals(5, improvingPerformance.getQuestionIdsSeen().size());
        assertEquals(4, improvingPerformance.getQuestionIdsRespondedTo().size());
        assertEquals(2, improvingPerformance.getQuestionIdsCorrectFirstTime().size());
        assertEquals("mathQ", improvingPerformance.getQuestionIdsCorrectFirstTime().get(0));
        assertEquals("majorQ", improvingPerformance.getQuestionIdsCorrectFirstTime().get(1));
        assertEquals(1, improvingPerformance.getQuestionIdsIncorrect().size());
        assertEquals("yearQ", improvingPerformance.getQuestionIdsIncorrect().get(0));
        assertEquals(1, improvingPerformance.getQuestionIdsCorrectAfterIncorrect().size());
        assertEquals("googleQ", improvingPerformance.getQuestionIdsCorrectAfterIncorrect().get(0));
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
        List<String> questionsRespondedTo = QuestionHistorySummary.checkQuestionIdsRespondedTo(QuestionHistoryTest.makeExamples());
        assertEquals(3, questionsRespondedTo.size());
        assertEquals("q1", questionsRespondedTo.get(0));
        assertEquals("q3", questionsRespondedTo.get(1));
        assertEquals("q5", questionsRespondedTo.get(2));
    }

    @Test
    public void findQuestionsCorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> questionsCorrect = QuestionHistorySummary.findQuestionIdsCorrectFirstTime(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(1, questionsCorrect.size());
        // Sample Question 1: What color is the sky?
        assertEquals("skyQ", questionsCorrect.get(0));
    }

    @Test
    public void findQuestionsIncorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> questionsIncorrect = QuestionHistorySummary.findQuestionIdsIncorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(1, questionsIncorrect.size());
        assertEquals("mathQ", questionsIncorrect.get(0));
    }

    @Test
    public void findQuestionsCorrectAfterIncorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> CorrectAfterIncorrect = QuestionHistorySummary.findQuestionIdsCorrectAfterIncorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(1, CorrectAfterIncorrect.size());

        // Sample Question 3: What is your major?
        assertEquals("majorQ", CorrectAfterIncorrect.get(0));
    }

}
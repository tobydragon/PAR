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
        assertEquals(2, poorPerformance.getQuestionsRespondedTo().size());
        assertEquals(0, poorPerformance.getQuestionsCorrectFirstTime().size());
        assertEquals(2, poorPerformance.getQuestionsIncorrect().size());
        assertEquals(3, poorPerformance.getQuestionIdsSeen().size() - poorPerformance.getQuestionsRespondedTo().size());
        assertEquals("majorQ", poorPerformance.getQuestionsIncorrect().get(0));
        assertEquals("yearQ", poorPerformance.getQuestionsIncorrect().get(1));

        QuestionHistorySummary strongPerformance = new QuestionHistorySummary(QuestionHistoryTest.strongStudent(), data);
        assertEquals(5, strongPerformance.getQuestionIdsSeen().size());
        assertEquals(5, strongPerformance.getQuestionsRespondedTo().size());
        assertEquals(5, strongPerformance.getQuestionsCorrectFirstTime().size());
        assertEquals("skyQ", strongPerformance.getQuestionsCorrectFirstTime().get(0));
        assertEquals("mathQ", strongPerformance.getQuestionsCorrectFirstTime().get(1));
        assertEquals("majorQ", strongPerformance.getQuestionsCorrectFirstTime().get(2));
        assertEquals("yearQ", strongPerformance.getQuestionsCorrectFirstTime().get(3));
        assertEquals("googleQ", strongPerformance.getQuestionsCorrectFirstTime().get(4));

        QuestionHistorySummary improvingPerformance = new QuestionHistorySummary(QuestionHistoryTest.improvingStudent(), data);
        assertEquals(5, improvingPerformance.getQuestionIdsSeen().size());
        assertEquals(4, improvingPerformance.getQuestionsRespondedTo().size());
        assertEquals(2, improvingPerformance.getQuestionsCorrectFirstTime().size());
        assertEquals("mathQ", improvingPerformance.getQuestionsCorrectFirstTime().get(0));
        assertEquals("majorQ", improvingPerformance.getQuestionsCorrectFirstTime().get(1));
        assertEquals(1, improvingPerformance.getQuestionsIncorrect().size());
        assertEquals("yearQ", improvingPerformance.getQuestionsIncorrect().get(0));
        assertEquals(1, improvingPerformance.getQuestionsCorrectAfterIncorrect().size());
        assertEquals("googleQ", improvingPerformance.getQuestionsCorrectAfterIncorrect().get(0));
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
        List<String> questionsRespondedTo = QuestionHistorySummary.checkQuestionsRespondedTo(QuestionHistoryTest.makeExamples());
        assertEquals(3, questionsRespondedTo.size());
        assertEquals("q1", questionsRespondedTo.get(0));
        assertEquals("q3", questionsRespondedTo.get(1));
        assertEquals("q5", questionsRespondedTo.get(2));
    }

    @Test
    public void findQuestionsCorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> questionsCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(1, questionsCorrect.size());
        // Sample Question 1: What color is the sky?
        assertEquals("skyQ", questionsCorrect.get(0));
    }

    @Test
    public void findQuestionsIncorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> questionsIncorrect = QuestionHistorySummary.findQuestionsIncorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(1, questionsIncorrect.size());
        assertEquals("mathQ", questionsIncorrect.get(0));
    }

    @Test
    public void findQuestionsCorrectAfterIncorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> CorrectAfterIncorrect = QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(1, CorrectAfterIncorrect.size());

        // Sample Question 3: What is your major?
        assertEquals("majorQ", CorrectAfterIncorrect.get(0));
    }

}
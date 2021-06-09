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
        assertEquals(5, poorPerformance.getQuestionsRespondedTo().size());
        assertEquals(1, poorPerformance.getQuestionsCorrect().size());
        assertEquals("blue", poorPerformance.getQuestionsCorrect().get(0));
        assertEquals(4, poorPerformance.getQuestionsIncorrect().size());
        assertNotEquals("2", poorPerformance.getQuestionsIncorrect().get(0));
        assertNotEquals("CS", poorPerformance.getQuestionsIncorrect().get(1));
        assertNotEquals("2020", poorPerformance.getQuestionsIncorrect().get(2));
        assertNotEquals("1998", poorPerformance.getQuestionsIncorrect().get(3));
 
        QuestionHistorySummary strongPerformance = new QuestionHistorySummary(QuestionHistoryTest.strongStudent(), data);
        assertEquals(5, strongPerformance.getQuestionIdsSeen().size());
        assertEquals(5, strongPerformance.getQuestionsRespondedTo().size());
        assertEquals(5, strongPerformance.getQuestionsCorrect().size());
        assertEquals("blue", strongPerformance.getQuestionsCorrect().get(0));
        assertEquals("2", strongPerformance.getQuestionsCorrect().get(1));
        assertEquals("CS", strongPerformance.getQuestionsCorrect().get(2));
        assertEquals("2020", strongPerformance.getQuestionsCorrect().get(3));
        assertEquals("1998", strongPerformance.getQuestionsCorrect().get(4));

        QuestionHistorySummary improvingPerformance = new QuestionHistorySummary(QuestionHistoryTest.improvingStudent(), data);
        assertEquals(5, improvingPerformance.getQuestionIdsSeen().size());
        assertEquals(5, improvingPerformance.getQuestionsRespondedTo().size());
        assertEquals(1, improvingPerformance.getQuestionsCorrect().size());
        assertEquals("2", improvingPerformance.getQuestionsCorrect().get(0));
        assertEquals(4, improvingPerformance.getQuestionsIncorrect().size());
        assertEquals(5, improvingPerformance.getQuestionsCorrectAfterIncorrect().size());
        assertEquals("blue", improvingPerformance.getQuestionsCorrectAfterIncorrect().get(0));
        assertEquals("2", improvingPerformance.getQuestionsCorrectAfterIncorrect().get(1));
        assertEquals("CS", improvingPerformance.getQuestionsCorrectAfterIncorrect().get(2));
        assertEquals("2020", improvingPerformance.getQuestionsCorrectAfterIncorrect().get(3));
        assertEquals("1998", improvingPerformance.getQuestionsCorrectAfterIncorrect().get(4));


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
        List<String> questionsCorrect = QuestionHistorySummary.findQuestionsCorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(2, questionsCorrect.size());
        // Sample Question 1: What color is the sky?
        assertEquals("blue", questionsCorrect.get(0));
        // Sample Question 2: What is 1 + 1?
        assertEquals("2", questionsCorrect.get(1));
    }

    @Test
    public void findQuestionsIncorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> questionsIncorrect = QuestionHistorySummary.findQuestionsIncorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(1, questionsIncorrect.size());

        // Sample Question 3: What is your major?
        assertNotEquals("CS", questionsIncorrect.get(0));
        assertEquals("ENG", questionsIncorrect.get(0));
    }

    @Test
    public void findQuestionsCorrectAfterIncorrectTest() throws IOException{
        DomainDatasourceSimple data = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        List<String> CorrectAfterIncorrect = QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(QuestionHistoryTest.SampleQuestionsEx(), data);

        assertEquals(5, data.getAllQuestions().size());
        assertEquals(3, CorrectAfterIncorrect.size());

        // Sample Question 3: What is your major?
        assertEquals("blue", CorrectAfterIncorrect.get(0));
        assertEquals("2", CorrectAfterIncorrect.get(1));
        assertEquals("CS", CorrectAfterIncorrect.get(2));
    }

}
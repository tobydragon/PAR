package edu.ithaca.dragon.par.domain;

import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DomainDatasourceTest {

    @Test
    void gettersTest() throws IOException {
        DomainDatasource domainDatasource = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        assertEquals(5, domainDatasource.getAllQuestions().size());

        assertEquals("blue", domainDatasource.getQuestion("generic0").getCorrectAnswer());
        assertEquals("What is the color of the sky?", domainDatasource.getQuestion("generic0").getQuestionText());

        assertEquals("2", domainDatasource.getQuestion("generic1").getCorrectAnswer());
        assertEquals("What is 1 + 1?", domainDatasource.getQuestion("generic1").getQuestionText());

        assertEquals("CS", domainDatasource.getQuestion("generic2").getCorrectAnswer());
        assertEquals("What is your major?", domainDatasource.getQuestion("generic2").getQuestionText());

        assertThrows(IllegalArgumentException.class, ()-> domainDatasource.getQuestion("invalidId"));
    }

}
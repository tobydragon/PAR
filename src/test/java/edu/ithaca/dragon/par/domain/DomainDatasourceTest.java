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

        assertEquals("blue", domainDatasource.getQuestion("skyQ").getCorrectAnswer());
        assertEquals("What color is the sky?", domainDatasource.getQuestion("skyQ").getQuestionText());

        assertEquals("2", domainDatasource.getQuestion("mathQ").getCorrectAnswer());
        assertEquals("What is 1 + 1?", domainDatasource.getQuestion("mathQ").getQuestionText());

        assertEquals("CS", domainDatasource.getQuestion("majorQ").getCorrectAnswer());
        assertEquals("What is your major?", domainDatasource.getQuestion("majorQ").getQuestionText());

        assertThrows(IllegalArgumentException.class, ()-> domainDatasource.getQuestion("invalidId"));
    }

}
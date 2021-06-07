package edu.ithaca.dragon.par.domain;

import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DomainDatasourceTest {

    @Test
    void gettersTest() throws IOException {
        DomainDatasource domainDatasource = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        assertEquals(3, domainDatasource.getAllQuestions().size());
        assertEquals("2", domainDatasource.getQuestion("generic1").getCorrectAnswer());

        assertThrows(IllegalArgumentException.class, ()-> domainDatasource.getQuestion("invalidId"));
    }
}
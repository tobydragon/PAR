package edu.ithaca.dragon.par.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.ithaca.dragon.util.JsonIoHelperDefault;

public class QuestionTest {
    @Test
    public void getQuestionRecursivetest() throws IOException{
        DomainDatasource domain = new DomainDatasourceJson(
            "SampleFollowUpQuestions",
            "src/test/resources/rewrite/FollowUpQuestionsSample.json",
            null,
            new JsonIoHelperDefault());

        assertEquals("mathQ1", DomainDatasourceJson.getQuestionRecursive("mathQ1", domain.getQuestion("mathQ1")).getQuestionText());
        assertEquals("mathFQ1", DomainDatasourceJson.getQuestionRecursive("mathFQ1", domain.getQuestion("mathQ1")).getQuestionText());
        assertEquals("mathFFQ1", DomainDatasourceJson.getQuestionRecursive("mathFFQ1", domain.getQuestion("mathQ1")).getQuestionText());
        assertEquals("mathFQ2", DomainDatasourceJson.getQuestionRecursive("mathFQ2", domain.getQuestion("mathQ1")).getQuestionText());

        assertNull(DomainDatasourceJson.getQuestionRecursive("mathQ8", domain.getQuestion("mathQ1")));

    }
}

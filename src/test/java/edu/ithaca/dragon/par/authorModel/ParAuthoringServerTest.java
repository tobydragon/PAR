package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class ParAuthoringServerTest {

    @BeforeEach
    public void constructorTest() throws IOException {
        Datastore datastore = new JsonDatastore("src/test/resources/author/DemoQuestionPool.json");
        Datastore templateDatastore = new JsonDatastore("src/test/resources/author/DemoQuestionPoolTemplate.json");
        ParAuthoringServer pas = new ParAuthoringServer(datastore, templateDatastore);
    }

    @Test
    public void convertQuestionTemplateToQuestionTest(){
        fail("Make the function!");
    }
}

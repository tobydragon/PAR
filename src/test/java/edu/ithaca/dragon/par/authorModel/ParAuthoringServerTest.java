package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.io.Datastore;
import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class ParAuthoringServerTest {

    private Datastore datastore;
    private Datastore templateDatastore;
    private ParAuthoringServer pas;


    @BeforeEach
    public void setUp() throws IOException {
        Datastore datastore = new JsonDatastore("src/test/resources/author/SampleQuestionsEmpty.json");
        Datastore templateDatastore = new JsonDatastore("src/test/resources/author/DemoQuestionPoolTemplate.json");
        ParAuthoringServer pas = new ParAuthoringServer(datastore, templateDatastore);
    }

    @Test
    public void convertQuestionTemplateToQuestionTest(){

    }

    @Test
    public void getOrCreateAuthorModelTest() throws IOException{
        AuthorModel authorModel1 = ParAuthoringServer.getOrCreateAuthorModel();

    }
}

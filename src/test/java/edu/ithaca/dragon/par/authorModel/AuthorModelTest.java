package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthorModelTest {

    private AuthorModel authorModel;
    private QuestionPool questionPool;

    @BeforeEach
    public void setUp() throws IOException {
        //TODO: the questionPool should load in questionTemplates
        questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        authorModel = new AuthorModel("TestAuthor1", questionPool.getAllQuestions());
    }

    @Test
    public void constructorTest() throws IOException {
        assertEquals("TestAuthor1", authorModel.getAuthorId());
        assertEquals(15, authorModel.getQuestionCountList().size());
    }

    @Test
    public void removeQuestionTest(){
        assertEquals(15, authorModel.getQuestionCountList().size());
        authorModel.removeQuestion("PlaneQ1");
        assertEquals(14, authorModel.getQuestionCountList().size());
        authorModel.removeQuestion("ZoneQ3");
        assertEquals(13, authorModel.getQuestionCountList().size());

        assertThrows(InvalidParameterException.class, ()-> { authorModel.removeQuestion("ZoneQ3"); });
        assertThrows(InvalidParameterException.class, ()-> { authorModel.removeQuestion("NotAValidQuestionId"); });
    }

}

package edu.ithaca.dragon.par.domain;

import edu.ithaca.dragon.par.io.JsonDatastore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class JsonDatastoreTest {
    @Test
    public void createQuestionPoolTest() throws IOException{
        //create QuestionPool with JsonDatastore
        QuestionPool myQP = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions.json"));
        //get all questions and check them
        List<Question> myQPList = myQP.getAllQuestions();
        assertTrue(myQPList.size() == 3);
    }
}

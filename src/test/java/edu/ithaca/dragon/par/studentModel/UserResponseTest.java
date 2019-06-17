package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserResponseTest {

    @Test
    public void checkResponseTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserResponse response=new UserResponse("user1",questionsFromFile.get(0),questionsFromFile.get(0).getCorrectAnswer());
        assertEquals(response.checkResponse(),true);

    }


}

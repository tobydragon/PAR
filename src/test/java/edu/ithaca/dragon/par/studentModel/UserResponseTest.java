package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserResponseTest {

    @Test
    public void checkResponseTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponse response = new UserResponse("user1", questionsFromFile.get(0), responsesFromFile.get(0).getResponseTexts().get(0));
        assertEquals(response.checkResponse(), true);
    }

}

package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResponsesPerQuestionTest {

    @Test
    public void knowledgeCalcTest() throws IOException {

    }

    @Test
    public void allResponseTextSizeTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        ResponsesPerQuestion responsesPerQuestion=new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(),questionsFromFile.get(0),responsesFromFile.get(0).getResponseTexts().get(0));
        assertEquals(1,responsesPerQuestion.allResponseTextSize());
        responsesPerQuestion.addNewResponse("answer2");
        assertEquals(2,responsesPerQuestion.allResponseTextSize());
    }

}

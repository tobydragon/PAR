package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResponsesPerQuestionTest {

    @Test
    public void knowledgeCalcTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);

        ResponsesPerQuestion responsesPerQuestion=new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(),questionsFromFile.get(0),responsesFromFile.get(0).getResponseTexts().get(0));
        assertEquals(100.0,responsesPerQuestion.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);
        responsesPerQuestion.addNewResponse("Lateral");
        assertEquals(100.0,responsesPerQuestion.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);
        responsesPerQuestion.addNewResponse("hello");
        assertEquals(50.0,responsesPerQuestion.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);

        ResponsesPerQuestion responsesPerQuestion2=new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(),questionsFromFile.get(0),"hello");
        assertEquals(0.0,responsesPerQuestion2.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);
        responsesPerQuestion2.addNewResponse("goodbye");
        assertEquals(0.0,responsesPerQuestion2.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);
        responsesPerQuestion2.addNewResponse("Lateral");
        assertEquals(50.0,responsesPerQuestion2.knowledgeCalc(), DataUtil.OK_DOUBLE_MARGIN);







    }

    @Test
    public void allResponseTextSizeTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        ResponsesPerQuestion responsesPerQuestion=new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(),questionsFromFile.get(0),responsesFromFile.get(0).getResponseTexts().get(0));
        assertEquals(1,responsesPerQuestion.allResponseTextSize());
        responsesPerQuestion.addNewResponse("answer2");
        assertEquals(2,responsesPerQuestion.allResponseTextSize());
    }

}

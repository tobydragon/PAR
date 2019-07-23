package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.JsonStudentModelDatastore;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class UserResponseSetTest {

    @Test
    public void getResponseSizeTest()throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        List<ResponsesPerQuestion> userResponse =new ArrayList<>();
        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userResponse.add(response);
        }
        respSet.addAllResponses(userResponse);
        assertEquals(respSet.getUserResponsesSize(),15);

    }

    @Test
    public void addToResponsesPerQuestionTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());

        ResponsesPerQuestion response1 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0),responsesFromFile.get(0).getResponseTexts().get(0));
        ResponsesPerQuestion response2 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(1),responsesFromFile.get(0).getResponseTexts().get(1));
        respSet.addResponse(response1);respSet.addResponse(response1);respSet.addResponse(response2);

        assertEquals(2,response1.allResponsesSize());
        assertEquals(1,response2.allResponsesSize());
    }

    @Test
    public void addAllResponseTest()throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        List<ResponsesPerQuestion> responsesPerQuestions=StudentModel.createUserResponseObj(responsesFromFile.get(0),questionPool,"s1");
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        respSet.addAllResponses(responsesPerQuestions.subList(0,10));
        assertEquals(10,respSet.getUserResponsesSize());
        assertEquals(10,respSet.countTotalResponses());
        respSet.addAllResponses(responsesPerQuestions.subList(0,10));
        assertEquals(10,respSet.getUserResponsesSize());
        assertEquals(20,respSet.countTotalResponses());
       // responsesPerQuestions=StudentModel.createUserResponseObj(responsesFromFile.get(2),questionPool,"s1");
        respSet.addAllResponses(responsesPerQuestions.subList(5,15));
        assertEquals(15,respSet.getUserResponsesSize());
        assertEquals(30,respSet.countTotalResponses());


    }


    @Test
    public void knowledgeCalcTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);

        //For empty list returns -1
        UserResponseSet responseSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        assertEquals(-1.000,responseSet.knowledgeCalc(4), DataUtil.OK_DOUBLE_MARGIN);

        //One correct answer, no wrong answers, but still a low score
        ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(0), "Lateral");
        responseSet.addResponse(response);
        assertEquals(25.00, responseSet.knowledgeCalc(4), DataUtil.OK_DOUBLE_MARGIN);

        //Two correct answers, no wrong answers
        ResponsesPerQuestion response2 = new ResponsesPerQuestion(responsesFromFile.get(1).getUserId(), questionsFromFile.get(1), "Transverse");
        responseSet.addResponse(response2);
        assertEquals(50.00, responseSet.knowledgeCalc(4), DataUtil.OK_DOUBLE_MARGIN);

        //The score is increased by lowering the responsesToConsider parameter
        assertEquals(100.00, responseSet.knowledgeCalc(2), DataUtil.OK_DOUBLE_MARGIN);

        //The score is decreased by raising the responsesToConsider parameter
        assertEquals(1.00, responseSet.knowledgeCalc(200), DataUtil.OK_DOUBLE_MARGIN);

        //A new wrong answer lowers the score
        ResponsesPerQuestion response3 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(3), "Wrong Answer");
        responseSet.addResponse(response3);
        assertEquals(66.666666, responseSet.knowledgeCalc(3), DataUtil.OK_DOUBLE_MARGIN);

        //A series of correct answers forgives the wrong answers, leading to a perfect score
        ResponsesPerQuestion response4 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(2), "Lateral");
        responseSet.addResponse(response4);
        assertEquals(66.666666, responseSet.knowledgeCalc(3), DataUtil.OK_DOUBLE_MARGIN);

        ResponsesPerQuestion response5 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(4), "Transverse");
        responseSet.addResponse(response5);
        assertEquals(66.666666, responseSet.knowledgeCalc(3), DataUtil.OK_DOUBLE_MARGIN);

        ResponsesPerQuestion response6 = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(5), "bone");
        responseSet.addResponse(response6);
        assertEquals(100, responseSet.knowledgeCalc(3), DataUtil.OK_DOUBLE_MARGIN);

    }

    @Test
    public void generateKnowledgeBaseMapTest()throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        UserResponseSet userResponseSet = new UserResponseSet("TestUser1");

        Map<EquineQuestionTypes, String> m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("____", m1.get(EquineQuestionTypes.plane));
        assertEquals("____", m1.get(EquineQuestionTypes.structure));
        assertEquals("____", m1.get(EquineQuestionTypes.attachment));
        assertEquals("____", m1.get(EquineQuestionTypes.zone));

        ResponsesPerQuestion response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(0),questionsFromFile.get(0).getCorrectAnswer());
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("____", m1.get(EquineQuestionTypes.structure));
        assertEquals("____", m1.get(EquineQuestionTypes.attachment));
        assertEquals("____", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1",questionsFromFile.get(1),questionsFromFile.get(1).getCorrectAnswer());
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("___O", m1.get(EquineQuestionTypes.structure));
        assertEquals("____", m1.get(EquineQuestionTypes.attachment));
        assertEquals("____", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(2),"wrong");
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("__OX", m1.get(EquineQuestionTypes.structure));
        assertEquals("____", m1.get(EquineQuestionTypes.attachment));
        assertEquals("____", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(1).getFollowupQuestions().get(0),"wrong");
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("__OX", m1.get(EquineQuestionTypes.structure));
        assertEquals("___X", m1.get(EquineQuestionTypes.attachment));
        assertEquals("____", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(1).getFollowupQuestions().get(1),questionsFromFile.get(1).getFollowupQuestions().get(1).getCorrectAnswer());
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("__OX", m1.get(EquineQuestionTypes.structure));
        assertEquals("__XO", m1.get(EquineQuestionTypes.attachment));
        assertEquals("____", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(4),questionsFromFile.get(4).getCorrectAnswer());
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("__OX", m1.get(EquineQuestionTypes.structure));
        assertEquals("__XO", m1.get(EquineQuestionTypes.attachment));
        assertEquals("___O", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(3),questionsFromFile.get(3).getCorrectAnswer());
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("_OXO", m1.get(EquineQuestionTypes.structure));
        assertEquals("__XO", m1.get(EquineQuestionTypes.attachment));
        assertEquals("___O", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(6),questionsFromFile.get(6).getCorrectAnswer());
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("OXOO", m1.get(EquineQuestionTypes.structure));
        assertEquals("__XO", m1.get(EquineQuestionTypes.attachment));
        assertEquals("___O", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(7),questionsFromFile.get(7).getCorrectAnswer());
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("XOOO", m1.get(EquineQuestionTypes.structure));
        assertEquals("__XO", m1.get(EquineQuestionTypes.attachment));
        assertEquals("___O", m1.get(EquineQuestionTypes.zone));

        response = new ResponsesPerQuestion("TestUser1", questionsFromFile.get(8),"wrong");
        userResponseSet.addResponse(response);
        m1 = userResponseSet.generateKnowledgeBaseMap();
        assertEquals("___O", m1.get(EquineQuestionTypes.plane));
        assertEquals("OOOX", m1.get(EquineQuestionTypes.structure));
        assertEquals("__XO", m1.get(EquineQuestionTypes.attachment));
        assertEquals("___O", m1.get(EquineQuestionTypes.zone));

    }
    /*
    @Test
    public void splitResponsesByTypeTest()throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        List<ResponsesPerQuestion> userResponse =new ArrayList<>();
        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userResponse.add(response);
        }
        respSet.addAllResponses(userResponse);
        //UserResponseSet userResponseSet=new UserResponseSet("TestUser1");
        Map<String, List<ResponsesPerQuestion>> responseByType = respSet.splitResponsesByType(respSet.getUserResponses());
        //assertEquals(Arrays.asList("plane", "structure", "attachment", "zone"),responseByType.keySet());
        for (EquineQuestionTypes currType: EquineQuestionTypes.values()) {
            System.out.println(responseByType.get(currType.toString()).size());
        }
        System.out.println(responsesFromFile.get(0).getResponseTexts().size());

    }

 */
    /*
    @Test
    public void knowledgeBaseCalcTest()throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<ImageTaskResponse> responsesFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleResponses.json", ImageTaskResponse.class);
        UserResponseSet respSet=new UserResponseSet(responsesFromFile.get(0).getUserId());
        List<ResponsesPerQuestion> userResponse =new ArrayList<>();
        for(int i=0;i<questionsFromFile.size();i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userResponse.add(response);
        }
        respSet.addAllResponses(userResponse);

        String a=respSet.knowledgeBaseCalc(respSet.getUserResponses(),4);
        System.out.println(a);

        userResponse=new ArrayList<>();
        respSet=new UserResponseSet("TestUser");
        for(int i=0;i<3;i++){
            ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(i),responsesFromFile.get(0).getResponseTexts().get(i));
            userResponse.add(response);
        }
        ResponsesPerQuestion response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(3),"Wrong");
        userResponse.add(response);
        response = new ResponsesPerQuestion(responsesFromFile.get(0).getUserId(), questionsFromFile.get(4),"Wrong");
        userResponse.add(response);

        respSet.addAllResponses(userResponse);

        String a2=respSet.knowledgeBaseCalc(respSet.getUserResponses(),4);
        System.out.println(a2);
    }

 */

}

package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class AuthorTaskGeneratorTest {

    @Test
    public void nextImageTaskTemplateTest() throws IOException{
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);

    }

    @Test
    void getInitialQuestionTest() throws IOException {
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        List<QuestionCount> questionCountList = QuestionCount.questionToQuestionCount(questions);
        QuestionCount firstQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("plane./images/demoEquine14.jpg", firstQuestion.getQuestion().getImageUrl());
        for (int i = 0; i < 5; i++) {
            questionCountList.get(i).increaseTimesSeen();
        }
        QuestionCount secondQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("plane./images/demoEquine02.jpg", secondQuestion.getQuestion().getImageUrl());
        for (int i = 6; i < 11; i++) {
            questionCountList.get(i).increaseTimesSeen();
        }
        QuestionCount thirdQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("plane./images/demoEquine13.jpg", thirdQuestion.getQuestion().getImageUrl());

        for (int i = 0; i < questionCountList.size(); i++){
            questionCountList.get(i).setTimesSeen(1);
        }
        QuestionCount fourthQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("plane./images/demoEquine14.jpg", fourthQuestion.getQuestion().getImageUrl());

    }

    @Test
    void getAllQuestionsWithSameUrlTest() throws IOException{
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        List<QuestionCount> questionCountList = QuestionCount.questionToQuestionCount(questions);
        QuestionCount question = questionCountList.get(0);
        List<QuestionCount> sameUrl = AuthorTaskGenerator.getAllQuestionsWithSameUrl(question);
        assertEquals(5, sameUrl.size());

        question = questionCountList.get(7);
        sameUrl = AuthorTaskGenerator.getAllQuestionsWithSameUrl(question);
        assertEquals(6, sameUrl.size());

        question = questionCountList.get(12);
        sameUrl = AuthorTaskGenerator.getAllQuestionsWithSameUrl(question);
        assertEquals(4, sameUrl.size());
    }
}

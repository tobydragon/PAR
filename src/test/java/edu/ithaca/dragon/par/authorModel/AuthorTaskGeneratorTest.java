package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonAuthorDatastore;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperSpring;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class AuthorTaskGeneratorTest {

    @Test
    public void makeImageTaskTemplateTest() throws IOException{
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolTemplate.json", Question.class);
        AuthorModel authorModel = new AuthorModel("user1", QuestionCount.questionToQuestionCount(questions));
        ImageTask im = AuthorTaskGenerator.makeTaskTemplate(authorModel);
        ImageTask intendedFirstTask = JsonUtil.fromJsonFile("src/test/resources/author/imageTasks/nextImageTaskTemplateTest1.json", ImageTask.class);
        assertEquals(intendedFirstTask, im);

        for (int i = 0; i < intendedFirstTask.getTaskQuestions().size(); i++){
            authorModel.increaseTimesAttempted(intendedFirstTask.getTaskQuestions().get(i).getId());
        }

        ImageTask im2 = AuthorTaskGenerator.makeTaskTemplate(authorModel);
        ImageTask intendedSecondTask = JsonUtil.fromJsonFile("src/test/resources/author/imageTasks/nextImageTaskTemplateTest2.json", ImageTask.class);
        assertEquals(intendedSecondTask, im2);

    }

    @Test
    void getInitialQuestionTest() throws IOException {
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        List<QuestionCount> questionCountList = QuestionCount.questionToQuestionCount(questions);
        QuestionCount firstQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("./images/demoEquine14.jpg", firstQuestion.getQuestion().getImageUrl());
        for (int i = 0; i < 5; i++) {
            questionCountList.get(i).setTimesAttempted(1);
        }
        QuestionCount secondQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("./images/demoEquine02.jpg", secondQuestion.getQuestion().getImageUrl());
        for (int i = 5; i < 11; i++) {
            questionCountList.get(i).setTimesAttempted(1);
        }
        QuestionCount thirdQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("./images/demoEquine13.jpg", thirdQuestion.getQuestion().getImageUrl());

        for (int i = 0; i < questionCountList.size(); i++){
            questionCountList.get(i).setTimesAttempted(1);
        }
        QuestionCount fourthQuestion = AuthorTaskGenerator.getInitialQuestion(questionCountList);
        assertEquals("./images/demoEquine14.jpg", fourthQuestion.getQuestion().getImageUrl());

    }

    @Test
    void getAllQuestionsWithSameUrlTest() throws IOException{
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        AuthorModel authorModel = new AuthorModel("user1", QuestionCount.questionToQuestionCount(questions));
        List<QuestionCount> questionCountList = QuestionCount.questionToQuestionCount(questions);
        QuestionCount question = questionCountList.get(0);
        List<QuestionCount> sameUrl = AuthorTaskGenerator.getAllQuestionsWithSameUrl(question, authorModel);
        assertEquals(5, sameUrl.size());

        question = questionCountList.get(7);
        sameUrl = AuthorTaskGenerator.getAllQuestionsWithSameUrl(question, authorModel);
        assertEquals(6, sameUrl.size());

        question = questionCountList.get(12);
        sameUrl = AuthorTaskGenerator.getAllQuestionsWithSameUrl(question, authorModel);
        assertEquals(4, sameUrl.size());
    }

    @Test
    public void urlsTest()throws IOException {
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        Set<String> urls = AuthorTaskGenerator.urls(questions);
        assertEquals(10, urls.size());

    }

    @Test
    public void authoredQuestionsTest()throws IOException{
        List<Question> questions = JsonUtil.listFromJsonFile("src/test/resources/author/questionPools/DemoQuestionPoolFollowup.json", Question.class);
        List<ImageTask> authoredImageTasks=AuthorTaskGenerator.authoredQuestions(questions);

        assertEquals(10,authoredImageTasks.size());
        assertEquals(5,authoredImageTasks.get(0).getTaskQuestions().size());//should be 5 questions with this url
        assertEquals(6,authoredImageTasks.get(1).getTaskQuestions().size());//should be 6 questions with this url
        assertEquals(6,authoredImageTasks.get(9).getTaskQuestions().size());//should be 6 questions with this url

    }
}

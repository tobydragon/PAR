package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TaskGeneratorTest {

    @Test
    public void makeTaskWithSingleQuestionTest() throws IOException {
        //set up questionPool and studentModel, create an imageTask with the studentModel
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //no questions have been seen
        assertEquals(15, studentModel.getUnseenQuestionCount());

        //make an imageTask and check aspects of it
        Question task1Question = TaskGenerator.getInitialQuestionForTask(studentModel, 1);
        ImageTask task1 = new ImageTask(task1Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine04.jpg", task1.getImageUrl());
        assertEquals(1, task1.getTaskQuestions().size());

        //make a new imageTask and check aspects of it
        Question task2Question = TaskGenerator.getInitialQuestionForTask(studentModel, 1);
        ImageTask task2 = new ImageTask(task2Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine04.jpg", task1.getImageUrl());
        assertEquals(1, task2.getTaskQuestions().size());

    }

    @Test
    public void makeTaskTest() throws IOException{
        //set up questionPool and studentModel, create an imageTask with the studentModel
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/DemoQuestionPool.json"));
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //make an imageTask and check aspects of it
        ImageTask task1 = TaskGenerator.makeTask(studentModel);

        assertEquals("./images/demoEquine14.jpg", task1.getImageUrl());

        assertEquals(1, task1.getTaskQuestions().size());

        //make a new imageTask and check aspects of it
        ImageTask task2 = TaskGenerator.makeTask(studentModel);
        assertEquals("./images/demoEquine02.jpg", task2.getImageUrl());
        assertEquals(1, task2.getTaskQuestions().size());
    }

    @Test
    public void studentModelWithNoQuestionsTest() throws IOException{
        QuestionPool emptyQP = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionsEmpty.json"));
        StudentModel studentModel = new StudentModel("TestUser1", emptyQP.getAllQuestions());

        //try to make a single Question
        try{
            Question newQ = TaskGenerator.getInitialQuestionForTask(studentModel, 1);
            fail();
        }catch(Exception ee){

        }

        //try to make a task
        try{
            ImageTask imageTask = TaskGenerator.makeTask(studentModel);
            fail();
        }catch(Exception ee){

        }
    }

    @Test
    public void setTypeTest(){
        //this test will be written once StudentModel has individual scores for each question and TaskGenerator
        //can use those to determine level
        assertEquals(4,4);
    }

    @Test
    public void removeTypeTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        questions = TaskGenerator.removeType(questions, "plane");
        assertEquals(10, questions.size());
        questions = TaskGenerator.removeType(questions, "structure");
        assertEquals(5, questions.size());
        questions = TaskGenerator.removeType(questions, "zone");
        assertEquals(0, questions.size());

        questions = TaskGenerator.removeType(questions, "plane");
        assertEquals(0, questions.size());
        questions = TaskGenerator.removeType(questions, "structure");
        assertEquals(0, questions.size());
        questions = TaskGenerator.removeType(questions, "zone");
        assertEquals(0, questions.size());


    }

    @Test
    public void addAllQuestionsTest()throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/DemoQuestionPool.json"));
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //first url
        Question q1 = studentModel.getUserQuestionSet().getUnseenQuestions().get(0);
        List<Question> qs =TaskGenerator.addAllQuestions(studentModel, q1);
        assertEquals(5, qs.size());

        //second url
        Question q2 = studentModel.getUserQuestionSet().getUnseenQuestions().get(5);
        qs =TaskGenerator.addAllQuestions(studentModel, q2);
        assertEquals(6, qs.size());

        //third url
        Question q3 = studentModel.getUserQuestionSet().getUnseenQuestions().get(11);
        qs =TaskGenerator.addAllQuestions(studentModel, q3);
        assertEquals(4, qs.size());

        //fourth url
        Question q4 = studentModel.getUserQuestionSet().getUnseenQuestions().get(15);
        qs =TaskGenerator.addAllQuestions(studentModel, q4);
        assertEquals(3, qs.size());
        //repeat
        qs =TaskGenerator.addAllQuestions(studentModel, q4);
        assertEquals(3, qs.size());
        qs =TaskGenerator.addAllQuestions(studentModel, q4);
        assertEquals(3, qs.size());

        //last question
        Question q5 = studentModel.getUserQuestionSet().getUnseenQuestions().get(questionPool.getAllQuestions().size()-1);
        qs =TaskGenerator.addAllQuestions(studentModel, q5);
        assertEquals(6, qs.size());
    }

    @Test
    public void filterQuestionsTest()throws IOException{
       //plane only
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/DemoQuestionPool.json"));
        List<Question> questionList = questionPool.getAllQuestions();
        questionList = TaskGenerator.filterQuestions(1, questionList);
        assertEquals(10, questionList.size());

        //plane and structure only
        questionList = questionPool.getAllQuestions();
        questionList = TaskGenerator.filterQuestions(2, questionList);
        assertEquals(37, questionList.size());

        //structure only
        questionList = questionPool.getAllQuestions();
        questionList = TaskGenerator.filterQuestions(3, questionList);
        assertEquals(27, questionList.size());

        //structure and attachment only
        questionList = questionPool.getAllQuestions();
        questionList = TaskGenerator.filterQuestions(4, questionList);
        assertEquals(27, questionList.size());

        //structure, attachment, and zone only
        questionList = questionPool.getAllQuestions();
        questionList = TaskGenerator.filterQuestions(5, questionList);
        assertEquals(37, questionList.size());

        //zone only
        questionList = questionPool.getAllQuestions();
        questionList = TaskGenerator.filterQuestions(6, questionList);
        assertEquals(10, questionList.size());
    }

    @Test
    public void imageTaskWithFollowupQuestionsTest(@TempDir Path tempDir) throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json"));
        StudentModel studentModel = new StudentModel("TestUser111", questionPool.getAllQuestions());

        ImageTask imageTask = TaskGenerator.makeTask(studentModel, 4);
        JsonUtil.toJsonFile("src/test/resources/autoGenerated/imageTaskWithFollowup.json", imageTask);
    }
}

package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

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
        Question task1Question = TaskGenerator.getInitialQuestionForTask(studentModel);
        ImageTask task1 = new ImageTask(task1Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine02.jpg", task1.getImageUrl());
        assertEquals(1, task1.getTaskQuestions().size());

        //make a new imageTask and check aspects of it
        Question task2Question = TaskGenerator.getInitialQuestionForTask(studentModel);
        ImageTask task2 = new ImageTask(task2Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine02.jpg", task1.getImageUrl());
        assertEquals(1, task2.getTaskQuestions().size());
        System.out.println(task2.getImageUrl());
    }

    @Test
    public void makeTaskTest() throws IOException{
        //set up questionPool and studentModel, create an imageTask with the studentModel
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //make an imageTask and check aspects of it
        ImageTask task1 = TaskGenerator.makeTask(studentModel);
        assertEquals("./images/demoEquine02.jpg", task1.getImageUrl());
        assertEquals(4, task1.getTaskQuestions().size());

        //make a new imageTask and check aspects of it
        ImageTask task2 = TaskGenerator.makeTask(studentModel);
        assertEquals("./images/demoEquine04.jpg", task2.getImageUrl());
        assertEquals(5, task2.getTaskQuestions().size());
    }

    @Test
    public void studentModelWithNoQuestionsTest() throws IOException{
        QuestionPool emptyQP = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionsEmpty.json"));
        StudentModel studentModel = new StudentModel("TestUser1", emptyQP.getAllQuestions());

        //try to make a single Question
        try{
            Question newQ = TaskGenerator.getInitialQuestionForTask(studentModel);
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
}

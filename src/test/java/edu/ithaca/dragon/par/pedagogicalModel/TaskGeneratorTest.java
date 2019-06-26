package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskGeneratorTest {

    @Test
    public void makeTaskWithSingleQuestionTest() throws IOException {
        //set up questionPool and studentModel, create an imageTask with the studentModel
        QuestionPool questionPool = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //no questions have been seen
        assertEquals(15, studentModel.getUnseenQuestionCount());

        //make an imageTask and check aspects of it
        ImageTask task1 = TaskGenerator.makeTaskWithSingleQuestion(studentModel);
        assertEquals("../static/images/demoEquine02.jpg", task1.getImageUrl());
        assertEquals(1, task1.getTaskQuestions().size());

        //one question has been seen
        assertEquals(14, studentModel.getUnseenQuestionCount());

        //make a new imageTask and check aspects of it
        ImageTask task2 = TaskGenerator.makeTaskWithSingleQuestion(studentModel);
        assertEquals("../static/images/demoEquine02.jpg", task1.getImageUrl());
        assertEquals(1, task1.getTaskQuestions().size());
        System.out.println(task2.getImageUrl());
    }
}

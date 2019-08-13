package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskGeneratorTest {

    //TODO: tests should be more thorough
    public void makeTaskTest(TaskGenerator taskGenerator) throws IOException {
        //set up questionPool and studentModel, create an imageTask with the studentModel
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //make an imageTask and check aspects of it
        ImageTask task1 = taskGenerator.makeTask(studentModel, 4);
        assertEquals("./images/demoEquine14.jpg", task1.getImageUrl());
        assertEquals(1, task1.getTaskQuestions().size());

        //make a new imageTask and check aspects of it
        ImageTask task2 = taskGenerator.makeTask(studentModel, 4);
        assertEquals("./images/demoEquine02.jpg", task2.getImageUrl());
        assertEquals(1, task2.getTaskQuestions().size());
    }

    @Test
    public void levelTaskGeneratorTests() throws IOException{
        makeTaskTest(new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()));
    }

}

package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class OrderedTaskGeneratorTest {

    @Test
    public void loopedQuestionSetTest() throws IOException {
        OrderedTaskGenerator randomTaskGenerator = new OrderedTaskGenerator();
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        
        String firstQuestionType = null;
        String lastQuestionType;
        for (int i = 0; i < 15; i++){
            ImageTask taskToCheck = randomTaskGenerator.makeTask(studentModel, 4);
            List<Question> questionsList = taskToCheck.getTaskQuestions();
            if (i == 0) {
                firstQuestionType = questionsList.get(0).getType();
            }
        }

        //ask for one more task to check that it loops
        ImageTask taskToCheck = randomTaskGenerator.makeTask(studentModel, 4);
        List<Question> questionsList = taskToCheck.getTaskQuestions();
        lastQuestionType = questionsList.get(0).getType();

        assertEquals(firstQuestionType, lastQuestionType);
    }

    @Test
    public void jsonLoadedTest() throws IOException {
        //load two students
        QuestionPool questionPool1 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/DemoQuestionPoolFewFollowups.json").getAllQuestions());
        StudentModel studentModel1 = new StudentModel("TestUser1", questionPool1.getAllQuestions());

        QuestionPool questionPool2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/DemoQuestionPool.json").getAllQuestions());
        StudentModel studentModel2 = new StudentModel("TestUser2", questionPool2.getAllQuestions());

        // load two OrderedTaskGenerators with different QuestionPools
        OrderedTaskGenerator otg1 = new OrderedTaskGenerator(questionPool1, true);
        OrderedTaskGenerator otg2 = new OrderedTaskGenerator(questionPool2, false);

        //ask for task with followup questions; verify length is greater than 1
        ImageTask testFollowup = otg1.makeTask(studentModel1, 4);
        assertNotEquals(testFollowup.getTaskQuestions().size(), 1);
        assertTrue(testFollowup.getTaskQuestions().size() > 0);

        //ask for task with no followup; verify length is exactly 1
        ImageTask testSingle = otg2.makeTask(studentModel2, 4);
        assertEquals(testSingle.getTaskQuestions().size(), 1);

        //verify that two asked questions are different
        assertFalse(testFollowup.equals(testSingle));
    }
}

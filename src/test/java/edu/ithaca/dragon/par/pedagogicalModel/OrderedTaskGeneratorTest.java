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
}

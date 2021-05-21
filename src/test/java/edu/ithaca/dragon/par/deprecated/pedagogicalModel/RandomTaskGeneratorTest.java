package edu.ithaca.dragon.par.deprecated.pedagogicalModel;

import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.io.ImageTask;
import edu.ithaca.dragon.par.deprecated.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.deprecated.studentModel.StudentModel;
import edu.ithaca.dragon.par.domain.Question;

import java.io.IOException;
import java.util.List;

public class RandomTaskGeneratorTest {

    /**
     * asks for a question 50 times, makeTask should produce a variety of types of questions
     */
    public static void checkUniquenessTest() throws IOException {
        RandomTaskGenerator randomTaskGenerator = new RandomTaskGenerator();
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        for (int i = 0; i < 50; i++){
            ImageTask taskToCheck = randomTaskGenerator.makeTask(studentModel, 4);
            List<Question> questionsList = taskToCheck.getTaskQuestions();
            for (int j = 0; j < questionsList.size(); j++){
                System.out.println(questionsList.get(j).getType());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        checkUniquenessTest();
    }

}

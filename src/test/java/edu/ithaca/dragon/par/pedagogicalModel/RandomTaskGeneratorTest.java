package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.JsonStudentModelDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class RandomTaskGeneratorTest {

    /**
     * asks for a question 50 times, makeTask should produce a variety of types of questions
     */
    public static void checkUniquenessTest() throws IOException {
        RandomTaskGenerator randomTaskGenerator = new RandomTaskGenerator();
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        for (int i = 0; i < 50; i++){
            ImageTask taskToCheck = randomTaskGenerator.makeTask(studentModel, 1);
            List<Question> questionsList = taskToCheck.getTaskQuestions();
            for (int j = 0; j < questionsList.size(); i++){
                System.out.println(questionsList.get(i).getType());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        checkUniquenessTest();
    }

}

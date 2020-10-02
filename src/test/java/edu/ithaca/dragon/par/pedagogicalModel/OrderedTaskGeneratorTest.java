package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class OrderedTaskGeneratorTest {

    //main:
    //take in file name to Question Pool, then use static method to create List<orderedQuestionInfo> (creates default)
        //eventually if hasFollowUpQuestions should not be necessary; (needs default value)
    //write list to JSON (inside of test/resources/author/orderedQuestionInfo)
    //tests: load changed orderedQuestionInfo List files; verify that they are different from a default, UNCHANGED, order
    public static void main(String[] args) throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> orderedInfoList = OrderedTaskGenerator.createOrderedQuestionInfoListFromQuestionPool(questionPool, true);
        JsonIoUtil writer = new JsonIoUtil(new JsonIoHelperDefault());
        writer.toFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", orderedInfoList);
    }

}

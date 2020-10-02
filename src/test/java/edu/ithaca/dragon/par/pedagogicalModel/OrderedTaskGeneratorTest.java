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
    @Test
    public void makeTaskTest() throws IOException {
        JsonIoUtil reader = new JsonIoUtil(new JsonIoHelperDefault());

        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> defaultQuestionOrderedInfoList = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", QuestionOrderedInfo.class);
        List<QuestionOrderedInfo> QuestionOrderedInfoListTest1 = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoListTest1.json", QuestionOrderedInfo.class);
        List<QuestionOrderedInfo> QuestionOrderedInfoListTest2 = reader.listFromFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoListTest2.json", QuestionOrderedInfo.class);

        OrderedTaskGenerator defaultOrderedTaskGenerator = new OrderedTaskGenerator(questionPool, defaultQuestionOrderedInfoList);
        OrderedTaskGenerator test1OrderedTaskGenerator = new OrderedTaskGenerator(questionPool, QuestionOrderedInfoListTest1);
        OrderedTaskGenerator test2OrderedTaskGenerator = new OrderedTaskGenerator(questionPool, QuestionOrderedInfoListTest2);

        StudentModel dummy = new StudentModel("NotUsed", questionPool.getAllQuestions());

        //check default != Test1
        //TODO we are potentially asking the same questions twice (as an attached followup OR by itself...)
        //TODO testing the entire set of questions with automation?

        ImageTask defaultTask = defaultOrderedTaskGenerator.makeTask(dummy, 4);
        ImageTask test1Task = test1OrderedTaskGenerator.makeTask(dummy, 4);

        //check default != Test2
        //check Test1 != Test2
    }

    //main:
    //take in file name to Question Pool, then use static method to create List<QuestionOrderedInfo> (creates default)
        //eventually if hasFollowUpQuestions should not be necessary; (needs default value)
    //write list to JSON (inside of test/resources/author/QuestionOrderedInfo)
    //tests: load changed QuestionOrderedInfo List files; verify that they are different from a default, UNCHANGED, order
    public static void main(String[] args) throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> orderedInfoList = OrderedTaskGenerator.createOrderedQuestionInfoListFromQuestionPool(questionPool, true);
        JsonIoUtil writer = new JsonIoUtil(new JsonIoHelperDefault());
        writer.toFile("src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json", orderedInfoList);
    }

}

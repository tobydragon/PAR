package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
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
        List<ImageTask> defaultList = new ArrayList<>();
        List<ImageTask> test1List = new ArrayList<>();
        for (int i = 0; i < defaultQuestionOrderedInfoList.size(); i++){
            defaultList.add(defaultOrderedTaskGenerator.makeTask(dummy,4));
            test1List.add(test1OrderedTaskGenerator.makeTask(dummy,4));
        }
        assertEquals("plane", defaultList.get(0).getTaskQuestions().get(0).getType());
        assertEquals("structure", test1List.get(0).getTaskQuestions().get(0).getType());
        assertNotEquals(test1List, defaultList);

        //check default != Test2
        List<ImageTask> test2List = new ArrayList<>();
        for (int i = 0; i < defaultQuestionOrderedInfoList.size(); i++){
            test2List.add(test2OrderedTaskGenerator.makeTask(dummy,4));
        }

        assertEquals("plane", test2List.get(0).getTaskQuestions().get(0).getType());
        assertNotEquals(test2List, defaultList);

        //check Test1 != Test2
        assertNotEquals(test1List, test2List);
    }

    @Test
    public void createQuestionOrderedInfoListTest() throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPool.json").getAllQuestions());

        List<QuestionOrderedInfo> toTest = OrderedTaskGenerator.createDefaultQuestionOrderedInfoList(questionPool, true);
        assertEquals(questionPool.getAllQuestions().size(), toTest.size());
        for (int i = 0; i < toTest.size(); i++){
            assertEquals(questionPool.getAllQuestions().get(i).getId(), toTest.get(i).getQuestionID());
            assertTrue(toTest.get(i).isIncludesFollowup());
        }

        //default with new method
        OrderedTaskGenerator testOTG1 = new OrderedTaskGenerator(questionPool, "src/test/resources/author/orderedQuestionInfo/OrderedQuestionInfoList.json");
        assertEquals(questionPool.getAllQuestions().size(), testOTG1.getQuestionOrderedInfoList().size());
        for (int i = 0; i < toTest.size(); i++){
            assertEquals(questionPool.getAllQuestions().get(i).getId(), toTest.get(i).getQuestionID());
            assertTrue(toTest.get(i).isIncludesFollowup());
        }

        //TODO test case for custom order
    }


    //main:
    //take in file name to Question Pool, then use static method to create List<QuestionOrderedInfo> (creates default)
        //eventually if hasFollowUpQuestions should not be necessary; (needs default value)
    //write list to JSON (inside of test/resources/author/QuestionOrderedInfo)
    //tests: load changed QuestionOrderedInfo List files; verify that they are different from a default, UNCHANGED, order
    public static void main(String[] args) throws IOException {
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("localData/currentQuestionPool.json").getAllQuestions());
//        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        List<QuestionOrderedInfo> orderedInfoList = OrderedTaskGenerator.createDefaultQuestionOrderedInfoList(questionPool, true);
        JsonIoUtil writer = new JsonIoUtil(new JsonIoHelperDefault());
        writer.toFile("src/main/resources/author/orderedQuestionInfo/currentOrderedQuestionInfoList.json", orderedInfoList);
    }

}

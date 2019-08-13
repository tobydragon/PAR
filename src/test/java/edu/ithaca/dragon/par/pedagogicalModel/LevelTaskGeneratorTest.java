package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.JsonStudentModelDatastore;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LevelTaskGeneratorTest {

    @Test
    public void makeTaskWithSingleQuestionTestFix() throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(studentModel.getUserQuestionSet().getTopLevelUnseenQuestions());

        Question task1Question = LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel);
        ImageTask task1 = new ImageTask(task1Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine04.jpg", task1.getImageUrl());

    }

    @Test
    public void emptyQuestionSetTest()throws IOException {
        JsonStudentModelDatastore datastore = new JsonStudentModelDatastore("src/test/resources/author/simpleTestSet/currentQuestionPool.json", "src/test/resources/author/simpleTestSet/students");
        StudentModel testUser2 = datastore.getOrCreateStudentModel("testUser2");

        ImageTask imageTask = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()).makeTask(testUser2,4);
        assertEquals(1,imageTask.getTaskQuestions().size());

    }

    @Test
    public void checkIfAllTypesInQuesListTest()throws IOException{
        JsonStudentModelDatastore datastore = new JsonStudentModelDatastore("src/test/resources/author/simpleTestSet/currentQuestionPool.json", "src/test/resources/author/simpleTestSet/students");
        StudentModel testUser2 = datastore.getOrCreateStudentModel("testUser2");

        assertEquals(true,LevelTaskGenerator.checkForAllNeededTypesOfQuestions(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.plane.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(3).getQuestion()));
        assertEquals(false,LevelTaskGenerator.checkForAllNeededTypesOfQuestions(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.structure.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(3).getQuestion()));
        assertEquals(true,LevelTaskGenerator.checkForAllNeededTypesOfQuestions(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.structure.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(0).getQuestion()));
        assertEquals(true,LevelTaskGenerator.checkForAllNeededTypesOfQuestions(Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(1).getQuestion()));
        //TODO: test with followup questions
    }

    @Test
    public void leastSeenQuestionTest()throws IOException{
        JsonStudentModelDatastore datastore = new JsonStudentModelDatastore("src/test/resources/author/simpleTestSet/currentQuestionPool.json", "src/test/resources/author/simpleTestSet/students");
        StudentModel testUser2 = datastore.getOrCreateStudentModel("testUser2");

        assertEquals("plane./images/demoEquine14.jpg",LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.structure.toString()),testUser2).getId());
        assertEquals("plane./images/demoEquine13.jpg",LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.plane.toString()),testUser2).getId());
        assertEquals( "structure0./images/demoEquine02.jpg",LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.structure.toString()),testUser2).getId());
        assertEquals( "structure0./images/demoEquine02.jpg",LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()),testUser2).getId());
        assertEquals( "AttachQ5",LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.attachment.toString()),testUser2).getId());
        //TODO: need to test when the least seen question does not have the proper types

        try{
            LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.attachment.toString(),EquineQuestionTypes.zone.toString()),testUser2).getId();
            fail();
        }catch(Exception ee){

        }
        assertEquals( "zone./images/demoEquine05.jpg",LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.zone.toString()),testUser2).getId());

    }

    @Test
    public void makeTaskImp2Test() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());
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
    public void makeTaskWithSingleQuestionImp2Test() throws IOException {
        //set up questionPool and studentModel, create an imageTask with the studentModel
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //no questions have been seen
        assertEquals(15, studentModel.getUnseenQuestionCount());

        //make an imageTask and check aspects of it
        Question task1Question = LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel);

        ImageTask task1 = new ImageTask(task1Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine04.jpg", task1.getImageUrl());
        assertEquals(1, task1.getTaskQuestions().size());

        //make a new imageTask and check aspects of it
        Question task2Question = LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel);
        ImageTask task2 = new ImageTask(task2Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine04.jpg", task1.getImageUrl());
        assertEquals(1, task2.getTaskQuestions().size());

    }

    @Test
    public void studentModelWithNoQuestionsImp2Test() throws IOException{
        QuestionPool emptyQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionsEmpty.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", emptyQP.getAllQuestions());

        //try to make a single Question
        try{
            Question newQ = LevelTaskGenerator.leastSeenQuestionWithTypesNeeded(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel);
            fail();
        }catch(Exception ee){

        }

        //try to make a task
        try{
            ImageTask imageTask = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()).makeTask(studentModel, 4);
            fail();
        }catch(Exception ee){

        }
    }

    @Test
    public void calcLevelTest() {
        //throws exception when the types are invalid
        try{
            Map<String, Double> m1 = new HashMap<>();
            // m1.put(EquineQuestionTypes.plane.toString(), 1.1);
            m1.put("NotValidKey", -1.0);
        }
        catch(RuntimeException ee){
        }

        Map<String, Double> m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 0.0);
        m2.put(EquineQuestionTypes.structure.toString(), 0.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 90.0);
        m2.put(EquineQuestionTypes.zone.toString(), 100.0);
        assertEquals(1, LevelTaskGenerator.calcLevel(m2));

        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), -1.0);
        m2.put(EquineQuestionTypes.structure.toString(), -1.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(1, LevelTaskGenerator.calcLevel(m2));
        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 75.0);
        m2.put(EquineQuestionTypes.structure.toString(), 20.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(2, LevelTaskGenerator.calcLevel(m2));


        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 75.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 30.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(2, LevelTaskGenerator.calcLevel(m2));


        m2 = new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 75.0);
        m2.put(EquineQuestionTypes.structure.toString(), 75.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(2, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 59.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(3, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 75.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(4, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 75.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 75.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(4, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), -1.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(5, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 34.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(5, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 75.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(6, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 75.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(6, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 99.0);
        m2.put(EquineQuestionTypes.zone.toString(), 23.0);
        assertEquals(6, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 100.0);
        m2.put(EquineQuestionTypes.zone.toString(), -1.0);
        assertEquals(7, LevelTaskGenerator.calcLevel(m2));

        m2=new HashMap<>();
        m2.put(EquineQuestionTypes.plane.toString(), 100.0);
        m2.put(EquineQuestionTypes.structure.toString(), 100.0);
        m2.put(EquineQuestionTypes.attachment.toString(), 100.0);
        m2.put(EquineQuestionTypes.zone.toString(), 75.0);
        assertEquals(7, LevelTaskGenerator.calcLevel(m2));

    }

    @Test
    public void removeTypeFromQuestionTest() throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFollowup.json").getAllQuestions());
        Question noFollowups = questionPool.getQuestionFromId("plane./images/demoEquine14.jpg");
        Question twoFollowups = questionPool.getQuestionFromId("structure3./images/demoEquine10.jpg");
        Question recFollowups = questionPool.getQuestionFromId("structure0./images/demoEquine14.jpg");

        //Trying to remove nonexistant followup questions should have no effect on the Question
        Question noFollowsAfter = LevelTaskGenerator.removeTypeFromQuestion(noFollowups, EquineQuestionTypes.attachment.toString());
        assertEquals(noFollowups, noFollowsAfter);

        //The method should not remove the base question
        assertThrows(RuntimeException.class, () -> {Question noFollowsAfterPlane = LevelTaskGenerator.removeTypeFromQuestion(noFollowups, EquineQuestionTypes.plane.toString());});

        //Removing attachment followups should create a question with no followups
        Question twoFollowupsAfter = LevelTaskGenerator.removeTypeFromQuestion(twoFollowups, EquineQuestionTypes.attachment.toString());
        assertEquals(0, twoFollowupsAfter.getFollowupQuestions().size());
        assertFalse(twoFollowups == twoFollowupsAfter);

        //Removing a followup to a followup
        Question recFollowupsAfter = LevelTaskGenerator.removeTypeFromQuestion(recFollowups, EquineQuestionTypes.plane.toString());
        assertFalse(recFollowupsAfter == recFollowups);
        assertEquals("plane", recFollowups.getFollowupQuestions().get(2).getFollowupQuestions().get(0).getType());
        assertEquals(0, recFollowupsAfter.getFollowupQuestions().get(2).getFollowupQuestions().size());
    }

    @Test
    public void removeTypeTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        questions = LevelTaskGenerator.removeTypeFromQuestionList(questions, EquineQuestionTypes.plane.toString());
        assertEquals(10, questions.size());
        questions = LevelTaskGenerator.removeTypeFromQuestionList(questions, EquineQuestionTypes.structure.toString());
        assertEquals(5, questions.size());
        questions = LevelTaskGenerator.removeTypeFromQuestionList(questions, EquineQuestionTypes.zone.toString());
        assertEquals(0, questions.size());

        questions = LevelTaskGenerator.removeTypeFromQuestionList(questions, EquineQuestionTypes.plane.toString());
        assertEquals(0, questions.size());
        questions = LevelTaskGenerator.removeTypeFromQuestionList(questions, EquineQuestionTypes.structure.toString());
        assertEquals(0, questions.size());
        questions = LevelTaskGenerator.removeTypeFromQuestionList(questions, EquineQuestionTypes.zone.toString());
        assertEquals(0, questions.size());


    }

}

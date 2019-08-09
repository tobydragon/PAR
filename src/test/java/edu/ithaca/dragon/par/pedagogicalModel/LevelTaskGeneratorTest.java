package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.JsonStudentModelDatastore;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LevelTaskGeneratorTest {

    @Test
    public void questionTypesMapTest() throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPoolFewFollowups.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        Map<String,List<QuestionCount>> questionByTypesMap=new LinkedHashMap<>();
        StudentModel.questionByTypeMap(studentModel.getUserQuestionSet().getQuestionCounts(),questionByTypesMap);

        assertEquals(4,questionByTypesMap.size());
        //do bonus and attachment not being recorded
        assertEquals(13,questionByTypesMap.get(EquineQuestionTypes.plane.toString()).size());
        assertEquals(27,questionByTypesMap.get(EquineQuestionTypes.structure.toString()).size());
        assertEquals(7,questionByTypesMap.get(EquineQuestionTypes.attachment.toString()).size());
        assertEquals(10,questionByTypesMap.get(EquineQuestionTypes.zone.toString()).size());
    }

    @Test
    public void makeTaskWithSingleQuestionTestFix() throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/SampleQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());
        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(studentModel.getUserQuestionSet().getTopLevelUnseenQuestions());
        Map<String,List<QuestionCount>> questionByTypesMap=new LinkedHashMap<>();
        StudentModel.questionByTypeMap(studentModel.getUserQuestionSet().getQuestionCounts(),questionByTypesMap);

        Question task1Question = LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel, questionByTypesMap);
        ImageTask task1 = new ImageTask(task1Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine04.jpg", task1.getImageUrl());

    }

    @Test
    public void emptyQuestionSetTest()throws IOException {
        JsonStudentModelDatastore datastore = new JsonStudentModelDatastore("src/test/resources/author/simpleTestSet/currentQuestionPool.json", "src/test/resources/author/simpleTestSet/students");
        StudentModel testUser2 = datastore.getOrCreateStudentModel("testUser2");

        ImageTask imageTask = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()).makeTask(testUser2,4);
        ImageTask imageTask2 = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap()).makeTask(testUser2,4);

        //System.out.println(imageTask2.getTaskQuestions());
        //System.out.println(imageTask.getTaskQuestions());

        assertEquals(1,imageTask.getTaskQuestions().size());

    }

    @Test
    public void checkIfAllTypesInQuesListTest()throws IOException{
        JsonStudentModelDatastore datastore = new JsonStudentModelDatastore("src/test/resources/author/simpleTestSet/currentQuestionPool.json", "src/test/resources/author/simpleTestSet/students");
        StudentModel testUser2 = datastore.getOrCreateStudentModel("testUser2");

        assertEquals(true,LevelTaskGenerator.checkIfAllTypesInQuesList(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.plane.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(3).getQuestion()));
        assertEquals(false,LevelTaskGenerator.checkIfAllTypesInQuesList(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.structure.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(3).getQuestion()));
        assertEquals(true,LevelTaskGenerator.checkIfAllTypesInQuesList(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.structure.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(0).getQuestion()));
        assertEquals(true,LevelTaskGenerator.checkIfAllTypesInQuesList(Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()),testUser2,testUser2.getUserQuestionSet().getQuestionCounts().get(1).getQuestion()));

    }

    @Test
    public void leastSeenQuestionTest()throws IOException{
        JsonStudentModelDatastore datastore = new JsonStudentModelDatastore("src/test/resources/author/simpleTestSet/currentQuestionPool.json", "src/test/resources/author/simpleTestSet/students");
        StudentModel testUser2 = datastore.getOrCreateStudentModel("testUser2");

        Map<String,List<QuestionCount>> questionTypesListMap=new LinkedHashMap<>();
        StudentModel.questionByTypeMap(testUser2.getUserQuestionSet().getQuestionCounts(),questionTypesListMap);

        assertEquals("plane./images/demoEquine14.jpg",LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.plane.toString(),EquineQuestionTypes.structure.toString()),testUser2,questionTypesListMap).getId());
        assertEquals("plane./images/demoEquine13.jpg",LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.plane.toString()),testUser2,questionTypesListMap).getId());
        assertEquals( "structure0./images/demoEquine02.jpg",LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.structure.toString()),testUser2,questionTypesListMap).getId());
        assertEquals( "structure0./images/demoEquine02.jpg",LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()),testUser2,questionTypesListMap).getId());
        assertEquals( "AttachQ5",LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.attachment.toString()),testUser2,questionTypesListMap).getId());
        try{
            LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.attachment.toString(),EquineQuestionTypes.zone.toString()),testUser2,questionTypesListMap).getId();
            fail();
        }catch(Exception ee){

        }
        assertEquals( "zone./images/demoEquine05.jpg",LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.zone.toString()),testUser2,questionTypesListMap).getId());

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
        Map<String,List<QuestionCount>> questionTypesListMap=new LinkedHashMap<>();
        StudentModel.questionByTypeMap(studentModel.getUserQuestionSet().getQuestionCounts(),questionTypesListMap);
        Question task1Question = LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel, questionTypesListMap);

        ImageTask task1 = new ImageTask(task1Question.getImageUrl(), Arrays.asList(task1Question));
        assertEquals("./images/demoEquine04.jpg", task1.getImageUrl());
        assertEquals(1, task1.getTaskQuestions().size());

        //make a new imageTask and check aspects of it
        Question task2Question = LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel, questionTypesListMap);
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
            Map<String,List<QuestionCount>> questionTypesListMap=new LinkedHashMap<>();
            StudentModel.questionByTypeMap(studentModel.getUserQuestionSet().getQuestionCounts(),questionTypesListMap);
            Question newQ = LevelTaskGenerator.leastSeenQuestion(Arrays.asList(EquineQuestionTypes.plane.toString()),studentModel, questionTypesListMap);
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
    public void buildQuestionListWithSameUrlImp2Test()throws IOException{
        QuestionPool questionPool = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/DemoQuestionPool.json").getAllQuestions());
        StudentModel studentModel = new StudentModel("TestUser1", questionPool.getAllQuestions());

        //first url
        Question q1 = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().get(0);
        List<Question> qs = LevelTaskGenerator.buildQuestionListWithSameUrl2(studentModel, q1);
        assertEquals(5, qs.size());

        //second url
        Question q2 = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().get(5);
        qs = LevelTaskGenerator.buildQuestionListWithSameUrl2(studentModel, q2);
        assertEquals(6, qs.size());

        //third url
        Question q3 = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().get(11);
        qs = LevelTaskGenerator.buildQuestionListWithSameUrl2(studentModel, q3);
        assertEquals(4, qs.size());

        //fourth url
        Question q4 = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().get(15);
        qs = LevelTaskGenerator.buildQuestionListWithSameUrl2(studentModel, q4);
        assertEquals(3, qs.size());
        //repeat
        qs = LevelTaskGenerator.buildQuestionListWithSameUrl2(studentModel, q4);
        assertEquals(3, qs.size());
        qs = LevelTaskGenerator.buildQuestionListWithSameUrl2(studentModel, q4);
        assertEquals(3, qs.size());

        //last question
        Question q5 = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions().get(questionPool.getAllQuestions().size()-1);
        qs = LevelTaskGenerator.buildQuestionListWithSameUrl2(studentModel, q5);
        assertEquals(6, qs.size());
    }

}

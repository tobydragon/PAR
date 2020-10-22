package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.*;
import edu.ithaca.dragon.par.studentModel.QuestionResponse;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.config.Task;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageGeneratorTest {
    @Test
    public void generateMessageTest() throws IOException { //TODO: will start failing once calcLevelFix stuff is in here
        //task generator

        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setPreviousLevel(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //mastered
        masteredStudentModel.setCurrentLevel(8);
        masteredStudentModel.setPreviousLevel(7);
        String message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("You have mastered the material, feel free to keep practicing", message);

        //not level 7, no message to display
        masteredStudentModel.setPreviousLevel(7);
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setPreviousLevel(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        level2Student.setCurrentLevel(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        message = MessageGenerator.generateMessage(level2Student, it2);
        assertNull(message);


        //goes down level, structure
        level2Student.setCurrentLevel(2);
        level2Student.setPreviousLevel(3);
        message = MessageGenerator.generateMessage(level2Student, it2);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", message);

        //goes up level
        level2Student.setPreviousLevel(1);
        message = MessageGenerator.generateMessage(level2Student, it2);
        assertEquals("You're doing great!", message);


        //7 to 6
        masteredStudentModel.setCurrentLevel(6);
        masteredStudentModel.setPreviousLevel(7);
        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with zone questions, go look at resources and come back if you need to", message);

        //stay on level 7, repeated question
        masteredStudentModel.setPreviousLevel(8);
        masteredStudentModel.setCurrentLevel(8);

        Date date = new Date();
        for (ResponsesPerQuestion response:masteredStudentModel.getUserResponseSet().getResponsesPerQuestionList()){
            List<QuestionResponse> r = response.getAllResponses();
            QuestionResponse last = r.get(response.getAllResponses().size()-1);
            last.setMillSeconds(date.getTime()-1799500);
            response.setAllResponses(r);
        }
        for (Question question : it.getTaskQuestions()){
            if (masteredStudentModel.getUserQuestionSet().getTimesSeen(question.getId())==0){
                masteredStudentModel.increaseTimesSeen(question.getId());
            }
        }


        Question q = masteredStudentModel.getUserQuestionSet().getAllQuestions().get(0);
        List<Question> questionList = new ArrayList<>();
        questionList.add(q);
        it.setTaskQuestions(questionList);

        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("You've mastered the material and started repeating questions", message);

        //repeated
        //String userIdIn, Question questionIn, String responseIn
        List<QuestionResponseOOP> resp= new ArrayList<>();
        resp.add(new QuestionResponseOOP("477-zone-./images/metacarpal19.jpg", "In which zone is the ultrasound taken?", "2a"));

        ImageTaskResponseOOP itr = new ImageTaskResponseOOP();
        itr.setUserId(masteredStudentModel.getUserId());
        itr.setQuestionResponses(resp);
        masteredStudentModel.imageTaskResponseSubmitted(itr, myQP, 4);

        masteredStudentModel.setCurrentLevel(1);
        masteredStudentModel.setPreviousLevel(1);
        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("You've seen this question recently, you might be stuck on plane questions.", message);


    }

    @Test
    public void increaseLevelTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setPreviousLevel(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        level2Student.setCurrentLevel(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        String message = MessageGenerator.generateMessage(level2Student, it2);
        assertNull(message);


        //goes up level
        level2Student.setCurrentLevel(2);
        level2Student.setPreviousLevel(1);
        message = MessageGenerator.increaseLevelMessage(level2Student);
        assertEquals("You're doing great!", message);

        //stays the same, no message
        level2Student.setPreviousLevel(2);
        message = MessageGenerator.increaseLevelMessage(level2Student);
        assertNull(message);

        //goes down level, no message
        level2Student.setPreviousLevel(2);
        level2Student.setCurrentLevel(1);
        message = MessageGenerator.increaseLevelMessage(level2Student);
        assertNull(message);

        //goes up level
        level2Student.setPreviousLevel(2);
        level2Student.setCurrentLevel(3);
        message = MessageGenerator.increaseLevelMessage(level2Student);
        assertEquals("You're doing great!", message);
    }

    @Test
    public void level8MessageTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setPreviousLevel(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //mastered
        masteredStudentModel.setCurrentLevel(8);
        String message = MessageGenerator.level8Message(masteredStudentModel, it);
        assertEquals("You have mastered the material, feel free to keep practicing", message);

        //not level 8, no message to display
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        level2Student.setCurrentLevel(2);
        level2Student.setPreviousLevel(1);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setPreviousLevel(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        message = MessageGenerator.level8Message(level2Student, it2);
        assertNull(message);

        //stay on level 8, repeated question
        masteredStudentModel.setPreviousLevel(8);
        masteredStudentModel.setCurrentLevel(8);

        Date date = new Date();
        for (ResponsesPerQuestion response:masteredStudentModel.getUserResponseSet().getResponsesPerQuestionList()){
            List<QuestionResponse> r = response.getAllResponses();
            QuestionResponse last = r.get(response.getAllResponses().size()-1);
            last.setMillSeconds(date.getTime()-1799500);
            response.setAllResponses(r);
        }
        for (Question question : it.getTaskQuestions()){
            if (masteredStudentModel.getUserQuestionSet().getTimesSeen(question.getId())==0){
                masteredStudentModel.increaseTimesSeen(question.getId());
            }
        }


        Question q = masteredStudentModel.getUserQuestionSet().getAllQuestions().get(0);
        List<Question> questionList = new ArrayList<>();
        questionList.add(q);
        it.setTaskQuestions(questionList);

        message = MessageGenerator.level8Message(masteredStudentModel, it);
        assertEquals("You've mastered the material and started repeating questions", message);

        //down level, no message
        masteredStudentModel.setPreviousLevel(7);
        masteredStudentModel.setCurrentLevel(6);
        message = MessageGenerator.level8Message(masteredStudentModel, it);
        assertNull(message);

        //up level, no message
        masteredStudentModel.setPreviousLevel(5);
        masteredStudentModel.setCurrentLevel(6);
        message = MessageGenerator.level8Message(masteredStudentModel, it);
        assertNull(message);
    }

    @Test
    public void repeatLevelMessageTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());

        //repeated within 30 min, message
        StudentModelRecord  smr30 = JsonUtil.fromJsonFile("src/test/resources/author/students/RepeatAfter30MinTest.json", StudentModelRecord.class);
        StudentModel student30 = smr30.buildStudentModel(myQP);

        Date date = new Date();
        for (ResponsesPerQuestion response:student30.getUserResponseSet().getResponsesPerQuestionList()){
            List<QuestionResponse> r = response.getAllResponses();
            QuestionResponse last = r.get(response.getAllResponses().size()-1);
            last.setMillSeconds(date.getTime()-1799500);
            response.setAllResponses(r);
        }

        ImageTask it30 = taskGenerator.makeTask(student30, 4);
        student30.setPreviousLevel(LevelTaskGenerator.calcLevel(student30.calcKnowledgeEstimateByType(4)));
        student30.setCurrentLevel(LevelTaskGenerator.calcLevel(student30.calcKnowledgeEstimateByType(4)));
        String message = MessageGenerator.repeatLevelMessage(student30, it30);
        assertEquals("You've seen this question recently, you might be stuck on plane/structure questions.", message);

        //repeated within 29 min, message
        StudentModelRecord  smr29 = JsonUtil.fromJsonFile("src/test/resources/author/students/RepeatAfter30MinTest.json", StudentModelRecord.class);
        StudentModel student29 = smr29.buildStudentModel(myQP);

        for (ResponsesPerQuestion response:student29.getUserResponseSet().getResponsesPerQuestionList()){
            List<QuestionResponse> r = response.getAllResponses();
            QuestionResponse last = r.get(response.getAllResponses().size()-1);
            last.setMillSeconds(date.getTime()-1740000);
            response.setAllResponses(r);
        }
        ImageTask it29 = taskGenerator.makeTask(student29, 4);
        student29.setPreviousLevel(LevelTaskGenerator.calcLevel(student29.calcKnowledgeEstimateByType(4)));
        student29.setCurrentLevel(LevelTaskGenerator.calcLevel(student30.calcKnowledgeEstimateByType(4)));
        message = MessageGenerator.repeatLevelMessage(student29, it29);
        assertEquals("You've seen this question recently, you might be stuck on plane/structure questions.", message);

        //repeated within 31 min, no message
        StudentModelRecord  smr31 = JsonUtil.fromJsonFile("src/test/resources/author/students/RepeatAfter30MinTest.json", StudentModelRecord.class);
        StudentModel student31 = smr31.buildStudentModel(myQP);

        for (ResponsesPerQuestion response:student31.getUserResponseSet().getResponsesPerQuestionList()){
            List<QuestionResponse> r = response.getAllResponses();
            QuestionResponse last = r.get(response.getAllResponses().size()-1);
            last.setMillSeconds(date.getTime()-1860000);
            response.setAllResponses(r);
        }

        ImageTask it31 = taskGenerator.makeTask(student31, 4);
        student31.setPreviousLevel(LevelTaskGenerator.calcLevel(student31.calcKnowledgeEstimateByType(4)));
        message = MessageGenerator.repeatLevelMessage(student31, it31);
        assertNull(message);

        //not repeated, no message
        StudentModel student = smr30.buildStudentModel(myQP);
        ImageTask it = taskGenerator.makeTask(student, 4);
        student.setPreviousLevel(LevelTaskGenerator.calcLevel(student.calcKnowledgeEstimateByType(4)));
        message = MessageGenerator.repeatLevelMessage(student, it);
        assertNull(message);

    }


    @Test
    public void decreaseLevelTest() throws IOException, InterruptedException{

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/currentQP-10-5-2020.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);


        //submit correct zone
        List<QuestionResponseOOP> resp= new ArrayList<>();
        resp.add(new QuestionResponseOOP("477-zone-./images/metacarpal19.jpg", "In which zone is the ultrasound taken?", "2a"));
        ImageTaskResponseOOP itr = new ImageTaskResponseOOP();
        itr.setUserId(masteredStudentModel.getUserId());
        itr.setQuestionResponses(resp);
        masteredStudentModel.imageTaskResponseSubmitted(itr, myQP, 4);

        assertEquals(6, LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));

        //submit incorrect zones
        resp= new ArrayList<>();
        resp.add(new QuestionResponseOOP("491-zone-./images/metacarpal37.jpg", "In which zone is the ultrasound taken?", "1"));
        resp.add(new QuestionResponseOOP("463-zone-./images/metacarpal25.jpg", "In which zone is the ultrasound taken?", "1"));
        resp.add(new QuestionResponseOOP("379-zone-./images/metacarpal41.jpg", "In which zone is the ultrasound taken?", "1"));
        resp.add(new QuestionResponseOOP("351-zone-./images/metacarpal42.jpg", "In which zone is the ultrasound taken?", "1"));

        itr = new ImageTaskResponseOOP();
        itr.setUserId(masteredStudentModel.getUserId());
        itr.setQuestionResponses(resp);
        masteredStudentModel.imageTaskResponseSubmitted(itr, myQP, 4);

        masteredStudentModel.setCurrentLevel(6);
        masteredStudentModel.setPreviousLevel(7);

        assertEquals("Looks like you're having trouble with zone questions, go look at resources and come back if you need to", MessageGenerator.decreaseLevelMessage(masteredStudentModel));



        //submit incorrect attachments
        Thread.sleep(1001);
        resp= new ArrayList<>();
        resp.add(new QuestionResponseOOP("367-attachment0-structure0-./images/metacarpal41.jpg", "What is this structure’s proximal attachment?", "1"));
        resp.add(new QuestionResponseOOP("373-attachment0-structure2-./images/metacarpal41.jpg", "What is this structure’s proximal attachment?", "1"));
        resp.add(new QuestionResponseOOP("376-attachment0-structure3-./images/metacarpal41.jpg", "What is this structure’s proximal attachment?", "1"));

        itr = new ImageTaskResponseOOP();
        itr.setUserId(masteredStudentModel.getUserId());
        itr.setQuestionResponses(resp);
        masteredStudentModel.imageTaskResponseSubmitted(itr, myQP, 4);

        masteredStudentModel.setCurrentLevel(5);
        masteredStudentModel.setPreviousLevel(6);

        assertEquals("Looks like you're having trouble with attachment questions, go look at resources and come back if you need to", MessageGenerator.decreaseLevelMessage(masteredStudentModel));


        //submit incorrect structure
        Thread.sleep(1001);
        resp= new ArrayList<>();
        resp.add(new QuestionResponseOOP("378-structure3-./images/metacarpal41.jpg", "What structure is 1.5 cm deep?", "1"));
        resp.add(new QuestionResponseOOP("341-structure0-./images/metacarpal42.jpg", "What structure is 1.5 cm deep?", "1"));

        itr = new ImageTaskResponseOOP();
        itr.setUserId(masteredStudentModel.getUserId());
        itr.setQuestionResponses(resp);
        masteredStudentModel.imageTaskResponseSubmitted(itr, myQP, 4);

        masteredStudentModel.setCurrentLevel(3);
        masteredStudentModel.setPreviousLevel(5);

        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", MessageGenerator.decreaseLevelMessage(masteredStudentModel));



        //submit incorrect structure
        Thread.sleep(1001);
        resp.add(new QuestionResponseOOP("490-structure3-./images/metacarpal37.jpg", "What structure is 1.5 cm deep?", "1"));

        itr = new ImageTaskResponseOOP();
        itr.setUserId(masteredStudentModel.getUserId());
        itr.setQuestionResponses(resp);
        masteredStudentModel.imageTaskResponseSubmitted(itr, myQP, 4);

        masteredStudentModel.setCurrentLevel(2);
        masteredStudentModel.setPreviousLevel(3);

        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", MessageGenerator.decreaseLevelMessage(masteredStudentModel));


        //submit incorrect structure and plane
        Thread.sleep(1001);
        resp= new ArrayList<>();
        resp.add(new QuestionResponseOOP("456-structure1-./images/metacarpal25.jpg", "What structure is 1.5 cm deep?", "1"));
        resp.add(new QuestionResponseOOP("467-structure0-./images/metacarpal19.jpg", "What structure is in the far field?", "1"));
        resp.add(new QuestionResponseOOP("324-plane-./images/metacarpal56.jpg", "On which plane is the ultrasound taken?", "1"));
        resp.add(new QuestionResponseOOP("338-plane-./images/metacarpal42.jpg", "On which plane is the ultrasound taken?", "1"));
        resp.add(new QuestionResponseOOP("366-plane-./images/metacarpal41.jpg", "On which plane is the ultrasound taken?", "1"));


        itr = new ImageTaskResponseOOP();
        itr.setUserId(masteredStudentModel.getUserId());
        itr.setQuestionResponses(resp);
        masteredStudentModel.imageTaskResponseSubmitted(itr, myQP, 4);

        masteredStudentModel.setCurrentLevel(1);
        masteredStudentModel.setPreviousLevel(2);

        assertEquals("Looks like you're having trouble with plane/structure questions, go look at resources and come back if you need to", MessageGenerator.decreaseLevelMessage(masteredStudentModel));

    }








}

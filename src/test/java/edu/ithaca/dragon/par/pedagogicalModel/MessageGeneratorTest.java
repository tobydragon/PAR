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
        masteredStudentModel.setCurrentLevel(7);
        masteredStudentModel.setPreviousLevel(6);
        String message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("You've mastered the material and started repeating questions", message);

        //not level 7, no message to display
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


        //6 to 5
        masteredStudentModel.setCurrentLevel(5);
        masteredStudentModel.setPreviousLevel(6);
        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with attachment/zone questions, go look at resources and come back if you need to", message);

        //5 to 4
        masteredStudentModel.setCurrentLevel(4);
        masteredStudentModel.setPreviousLevel(5);
        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with attachment questions, go look at resources and come back if you need to", message);

        //4 to 3
        masteredStudentModel.setCurrentLevel(3);
        masteredStudentModel.setPreviousLevel(4);
        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with structure/attachment questions, go look at resources and come back if you need to", message);

        //3 to 2
        masteredStudentModel.setCurrentLevel(2);
        masteredStudentModel.setPreviousLevel(3);
        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", message);

        //2 to 1
        masteredStudentModel.setCurrentLevel(1);
        masteredStudentModel.setPreviousLevel(2);
        message = MessageGenerator.generateMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with plane/structure questions, go look at resources and come back if you need to", message);

        //stay on level 7, repeated question
        masteredStudentModel.setPreviousLevel(7);
        masteredStudentModel.setCurrentLevel(7);
        for (Question question : it.getTaskQuestions()){
            masteredStudentModel.increaseTimesSeen(question.getId());
        }
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
    public void decreaseLevelMessageTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setPreviousLevel(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //7 to 6
        masteredStudentModel.setPreviousLevel(7);
        masteredStudentModel.setCurrentLevel(6);
        String message = MessageGenerator.decreaseLevelMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with zone questions, go look at resources and come back if you need to", message);


        //6 to 5
        masteredStudentModel.setPreviousLevel(6);
        masteredStudentModel.setCurrentLevel(5);
        message = MessageGenerator.decreaseLevelMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with attachment/zone questions, go look at resources and come back if you need to", message);

        //5 to 4
        masteredStudentModel.setPreviousLevel(5);
        masteredStudentModel.setCurrentLevel(4);
        message = MessageGenerator.decreaseLevelMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with attachment questions, go look at resources and come back if you need to", message);

        //4 to 3
        masteredStudentModel.setPreviousLevel(4);
        masteredStudentModel.setCurrentLevel(3);
        message = MessageGenerator.decreaseLevelMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with structure/attachment questions, go look at resources and come back if you need to", message);

        //3 to 2
        masteredStudentModel.setPreviousLevel(3);
        masteredStudentModel.setCurrentLevel(2);
        message = MessageGenerator.decreaseLevelMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", message);

        //2 to 1
        masteredStudentModel.setPreviousLevel(2);
        masteredStudentModel.setCurrentLevel(1);
        message = MessageGenerator.decreaseLevelMessage(masteredStudentModel, it);
        assertEquals("Looks like you're having trouble with plane/structure questions, go look at resources and come back if you need to", message);


        //1 to 1, no message
        masteredStudentModel.setCurrentLevel(1);
        masteredStudentModel.setPreviousLevel(1);
        it.setMessage("hey");
        message = MessageGenerator.decreaseLevelMessage(masteredStudentModel, it);
        assertNull(message);

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
        message = MessageGenerator.increaseLevelMessage(level2Student, it2);
        assertEquals("You're doing great!", message);

        //stays the same, no message
        level2Student.setPreviousLevel(2);
        message = MessageGenerator.increaseLevelMessage(level2Student, it2);
        assertNull(message);

        //goes down level, no message
        level2Student.setPreviousLevel(2);
        level2Student.setCurrentLevel(1);
        message = MessageGenerator.increaseLevelMessage(level2Student, it2);
        assertNull(message);

        //goes up level
        level2Student.setPreviousLevel(2);
        level2Student.setCurrentLevel(3);
        message = MessageGenerator.increaseLevelMessage(level2Student, it2);
        assertEquals("You're doing great!", message);
    }

    @Test
    public void level7MessageTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setPreviousLevel(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //mastered
        masteredStudentModel.setCurrentLevel(7);
        String message = MessageGenerator.level7Message(masteredStudentModel, it);
        assertEquals("You've mastered the material and started repeating questions", message);

        //not level 7, no message to display
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        level2Student.setCurrentLevel(2);
        level2Student.setPreviousLevel(1);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setPreviousLevel(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        message = MessageGenerator.level7Message(level2Student, it2);
        assertNull(message);

        //stay on level 7, repeated question
        masteredStudentModel.setPreviousLevel(7);
        masteredStudentModel.setCurrentLevel(7);
        for (Question question : it.getTaskQuestions()){
            masteredStudentModel.increaseTimesSeen(question.getId());
        }
        message = MessageGenerator.level7Message(masteredStudentModel, it);
        assertEquals("You've mastered the material and started repeating questions", message);

        //down level, no message
        masteredStudentModel.setPreviousLevel(7);
        masteredStudentModel.setCurrentLevel(6);
        message = MessageGenerator.level7Message(masteredStudentModel, it);
        assertNull(message);

        //up level, no message
        masteredStudentModel.setPreviousLevel(5);
        masteredStudentModel.setCurrentLevel(6);
        message = MessageGenerator.level7Message(masteredStudentModel, it);
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
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel student = smr30.buildStudentModel(myQP);
        ImageTask it = taskGenerator.makeTask(student, 4);
        student.setPreviousLevel(LevelTaskGenerator.calcLevel(student.calcKnowledgeEstimateByType(4)));
        message = MessageGenerator.repeatLevelMessage(student, it);
        assertNull(message);

    }
}

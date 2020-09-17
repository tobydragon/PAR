package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.studentModel.QuestionResponse;
import edu.ithaca.dragon.par.studentModel.ResponsesPerQuestion;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.io.IOException;
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
        masteredStudentModel.setLastLevelRecorded(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //mastered
        MessageGenerator.generateMessage(masteredStudentModel, it, -1);
        assertEquals("You've mastered the material and started repeating questions", it.getMessage());

        //not level 7, no message to display
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setLastLevelRecorded(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        MessageGenerator.generateMessage(level2Student, it2, -1);
        assertNull(it2.getMessage());


        //goes down level, structure
        MessageGenerator.generateMessage(level2Student, it2, 3);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", it2.getMessage());

        //goes up level
        MessageGenerator.generateMessage(level2Student, it2, 1);
        assertEquals("You're doing great!", it2.getMessage());


        //7 to 6
        masteredStudentModel.setLastLevelRecorded(6);
        MessageGenerator.generateMessage(masteredStudentModel, it, 7);
        assertEquals("Looks like you're having trouble with zone questions, go look at resources and come back if you need to", it.getMessage());


        //6 to 5
        masteredStudentModel.setLastLevelRecorded(5);
        MessageGenerator.generateMessage(masteredStudentModel, it, 6);
        assertEquals("Looks like you're having trouble with attachment/zone questions, go look at resources and come back if you need to", it.getMessage());

        //5 to 4
        masteredStudentModel.setLastLevelRecorded(4);
        MessageGenerator.generateMessage(masteredStudentModel, it, 5);
        assertEquals("Looks like you're having trouble with attachment questions, go look at resources and come back if you need to", it.getMessage());

        //4 to 3
        masteredStudentModel.setLastLevelRecorded(3);
        MessageGenerator.generateMessage(masteredStudentModel, it, 4);
        assertEquals("Looks like you're having trouble with structure/attachment questions, go look at resources and come back if you need to", it.getMessage());

        //3 to 2
        masteredStudentModel.setLastLevelRecorded(2);
        MessageGenerator.generateMessage(masteredStudentModel, it, 3);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", it.getMessage());

        //2 to 1
        masteredStudentModel.setLastLevelRecorded(1);
        MessageGenerator.generateMessage(masteredStudentModel, it, 2);
        assertEquals("Looks like you're having trouble with plane/structure questions, go look at resources and come back if you need to", it.getMessage());

        //stay on level 7, repeated question
        masteredStudentModel.setLastLevelRecorded(7);
        for (Question question : it.getTaskQuestions()){
            masteredStudentModel.increaseTimesSeen(question.getId());
        }
        MessageGenerator.generateMessage(masteredStudentModel, it, 7);
        assertEquals("You've mastered the material and started repeating questions", it.getMessage());



        //TODO: finish.
        //other levels, repeating questions

    }

    @Test
    public void decreaseLevelMessageTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setLastLevelRecorded(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //7 to 6
        masteredStudentModel.setLastLevelRecorded(6);
        MessageGenerator.decreaseLevelMessage(masteredStudentModel, it, 7);
        assertEquals("Looks like you're having trouble with zone questions, go look at resources and come back if you need to", it.getMessage());


        //6 to 5
        masteredStudentModel.setLastLevelRecorded(5);
        MessageGenerator.decreaseLevelMessage(masteredStudentModel, it, 6);
        assertEquals("Looks like you're having trouble with attachment/zone questions, go look at resources and come back if you need to", it.getMessage());

        //5 to 4
        masteredStudentModel.setLastLevelRecorded(4);
        MessageGenerator.decreaseLevelMessage(masteredStudentModel, it, 5);
        assertEquals("Looks like you're having trouble with attachment questions, go look at resources and come back if you need to", it.getMessage());

        //4 to 3
        masteredStudentModel.setLastLevelRecorded(3);
        MessageGenerator.decreaseLevelMessage(masteredStudentModel, it, 4);
        assertEquals("Looks like you're having trouble with structure/attachment questions, go look at resources and come back if you need to", it.getMessage());

        //3 to 2
        masteredStudentModel.setLastLevelRecorded(2);
        MessageGenerator.decreaseLevelMessage(masteredStudentModel, it, 3);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", it.getMessage());

        //2 to 1
        masteredStudentModel.setLastLevelRecorded(1);
        MessageGenerator.decreaseLevelMessage(masteredStudentModel, it, 2);
        assertEquals("Looks like you're having trouble with plane/structure questions, go look at resources and come back if you need to", it.getMessage());


        //1 to 1, no message
        masteredStudentModel.setLastLevelRecorded(1);
        it.setMessage("hey");
        MessageGenerator.decreaseLevelMessage(masteredStudentModel, it, 1);
        assertNull(it.getMessage());

    }

    @Test
    public void increaseLevelTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setLastLevelRecorded(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        MessageGenerator.generateMessage(level2Student, it2, -1);
        assertNull(it2.getMessage());


        //goes up level
        MessageGenerator.increaseLevelMessage(level2Student, it2, 1);
        assertEquals("You're doing great!", it2.getMessage());

        //stays the same, no message
        level2Student.setLastLevelRecorded(2);
        MessageGenerator.increaseLevelMessage(level2Student, it2, 2);
        assertNull(it2.getMessage());

        //goes down level, no message
        level2Student.setLastLevelRecorded(2);
        MessageGenerator.increaseLevelMessage(level2Student, it2, 3);
        assertNull(it2.getMessage());

        //goes up level
        level2Student.setLastLevelRecorded(2);
        MessageGenerator.increaseLevelMessage(level2Student, it2, 1);
        assertEquals("You're doing great!", it2.getMessage());
    }

    @Test
    public void level7MessageTest() throws IOException{
        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/TestQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setLastLevelRecorded(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //mastered
        MessageGenerator.level7Message(masteredStudentModel, it);
        assertEquals("You've mastered the material and started repeating questions", it.getMessage());

        //not level 7, no message to display
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setLastLevelRecorded(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        MessageGenerator.level7Message(level2Student, it2);
        assertNull(it2.getMessage());

        //stay on level 7, repeated question
        masteredStudentModel.setLastLevelRecorded(7);
        for (Question question : it.getTaskQuestions()){
            masteredStudentModel.increaseTimesSeen(question.getId());
        }
        MessageGenerator.level7Message(masteredStudentModel, it);
        assertEquals("You've mastered the material and started repeating questions", it.getMessage());

        //down level, no message
        masteredStudentModel.setLastLevelRecorded(6);
        MessageGenerator.level7Message(masteredStudentModel, it);
        assertNull(it.getMessage());

        //up level, no message
        masteredStudentModel.setLastLevelRecorded(5);
        MessageGenerator.level7Message(masteredStudentModel, it);
        assertNull(it.getMessage());
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
        student30.setLastLevelRecorded(LevelTaskGenerator.calcLevel(student30.calcKnowledgeEstimateByType(4)));
        MessageGenerator.repeatLevelMessage(student30, it30, 2);
        assertEquals("You've seen this question recently, you might be stuck on plane/structure questions.", it30.getMessage());

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
        student29.setLastLevelRecorded(LevelTaskGenerator.calcLevel(student29.calcKnowledgeEstimateByType(4)));
        MessageGenerator.repeatLevelMessage(student29, it29, 2);
        assertEquals("You've seen this question recently, you might be stuck on plane/structure questions.", it29.getMessage());

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
        student31.setLastLevelRecorded(LevelTaskGenerator.calcLevel(student31.calcKnowledgeEstimateByType(4)));
        MessageGenerator.repeatLevelMessage(student31, it31, 2);
        assertNull(it31.getMessage());

        //not repeated, no message
        StudentModelRecord  smr = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel student = smr30.buildStudentModel(myQP);
        ImageTask it = taskGenerator.makeTask(student, 4);
        student.setLastLevelRecorded(LevelTaskGenerator.calcLevel(student.calcKnowledgeEstimateByType(4)));
        MessageGenerator.repeatLevelMessage(student, it, -1);
        assertNull(it.getMessage());

    }
}

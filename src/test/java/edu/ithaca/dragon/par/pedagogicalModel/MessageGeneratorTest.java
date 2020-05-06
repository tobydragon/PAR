package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MessageGeneratorTest {
    @Test
    public void generateMessageTest() throws IOException { //TODO: will start failing once calcLevelFix stuff is in here
        //task generator

        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setLevel(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        //mastered
        MessageGenerator.generateMessage(masteredStudentModel, it, -1);
        assertEquals("You've mastered the material and started repeating questions", it.getMessage());

        //not level 7, no message to display
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel level2Student = smr2.buildStudentModel(myQP);
        ImageTask it2 = taskGenerator.makeTask(level2Student, 4);
        level2Student.setLevel(LevelTaskGenerator.calcLevel(level2Student.calcKnowledgeEstimateByType(4)));
        MessageGenerator.generateMessage(level2Student, it2, -1);
        assertEquals("No message to display", it2.getMessage());


        //goes down level, structure
        MessageGenerator.generateMessage(level2Student, it2, 3);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", it2.getMessage());

        //goes up level
        MessageGenerator.generateMessage(level2Student, it2, 1);
        assertEquals("You're doing great!", it2.getMessage());


        //7 to 6
        masteredStudentModel.setLevel(6);
        MessageGenerator.generateMessage(masteredStudentModel, it, 7);
        assertEquals("Looks like you're having trouble with zone questions, go look at resources and come back if you need to", it.getMessage());


        //6 to 5
        masteredStudentModel.setLevel(5);
        MessageGenerator.generateMessage(masteredStudentModel, it, 6);
        assertEquals("Looks like you're having trouble with attachment/zone questions, go look at resources and come back if you need to", it.getMessage());

        //5 to 4
        masteredStudentModel.setLevel(4);
        MessageGenerator.generateMessage(masteredStudentModel, it, 5);
        assertEquals("Looks like you're having trouble with attachment questions, go look at resources and come back if you need to", it.getMessage());

        //4 to 3
        masteredStudentModel.setLevel(3);
        MessageGenerator.generateMessage(masteredStudentModel, it, 4);
        assertEquals("Looks like you're having trouble with structure/attachment questions, go look at resources and come back if you need to", it.getMessage());

        //3 to 2
        masteredStudentModel.setLevel(2);
        MessageGenerator.generateMessage(masteredStudentModel, it, 3);
        assertEquals("Looks like you're having trouble with structure questions, go look at resources and come back if you need to", it.getMessage());

        //2 to 1
        masteredStudentModel.setLevel(1);
        MessageGenerator.generateMessage(masteredStudentModel, it, 2);
        assertEquals("Looks like you're having trouble with plane/structure questions, go look at resources and come back if you need to", it.getMessage());

        //stay on level 7, repeated question
        masteredStudentModel.setLevel(7);
        for (Question question : it.getTaskQuestions()){
            masteredStudentModel.increaseTimesSeen(question.getId());
        }
        MessageGenerator.generateMessage(masteredStudentModel, it, 7);
        assertEquals("You've mastered the material and started repeating questions", it.getMessage());




        //other levels, repeating questions

    }
}

package edu.ithaca.dragon.par.deprecated.pedagogicalModel;

import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.deprecated.io.ImageTask;
import edu.ithaca.dragon.par.deprecated.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.deprecated.io.StudentModelRecord;
import edu.ithaca.dragon.par.deprecated.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SilentMessageGeneratorTest {

    @Test
    public void messageGeneratorTest() throws IOException {
        SilentMessageGenerator smg = new SilentMessageGenerator();

        TaskGenerator taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());

        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        masteredStudentModel.setPreviousLevel(LevelTaskGenerator.calcLevel(masteredStudentModel.calcKnowledgeEstimateByType(4)));
        ImageTask it = taskGenerator.makeTask(masteredStudentModel, 4);

        masteredStudentModel.setCurrentLevel(7);
        masteredStudentModel.setPreviousLevel(6);
        String message = smg.generateMessage(masteredStudentModel, it);
        assertEquals(null, message);


        QuestionPool myQP2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/testFullQP.json").getAllQuestions());

        //repeated within 30 min, message
        StudentModelRecord  smr30 = JsonUtil.fromJsonFile("src/test/resources/author/students/buckmank.json", StudentModelRecord.class);
        StudentModel student30 = smr30.buildStudentModel(myQP2);

        ImageTask it30 = taskGenerator.makeTask(student30, 4);
        student30.setPreviousLevel(LevelTaskGenerator.calcLevel(student30.calcKnowledgeEstimateByType(4)));
        student30.setCurrentLevel(LevelTaskGenerator.calcLevel(student30.calcKnowledgeEstimateByType(4)));
        message = LevelMessageGenerator.repeatLevelMessage(student30, it30);
        assertEquals(null, message);

        StudentModel newStud = new StudentModel("newStudent", myQP2.getAllQuestions());
        message = LevelMessageGenerator.repeatLevelMessage(newStud, it30);
        assertEquals(null, message);
    }
}

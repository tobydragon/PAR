package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserQuestionSetRecordTest {
    @Test
    public void toJsonAndBackTest() throws IOException {
        QuestionPool qp = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestions2.json"));
        UserQuestionSet que = new UserQuestionSet("99", qp.getAllQuestions());

        UserQuestionSetRecord myUQSR = new UserQuestionSetRecord(que);
        //write to Json
        JsonUtil.toJsonFile("src/test/resources/autoGenerated/UserQuestionSetRecordTest-toJsonAndBackTest.json", myUQSR);
        //read from Json
        UserQuestionSetRecord UQSRFromJson = JsonUtil.fromJsonFile("src/test/resources/autoGenerated/UserQuestionSetRecordTest-toJsonAndBackTest.json",UserQuestionSetRecord.class);
        //make a UserQuestionSet from a UserQuestionSetRecord
        UserQuestionSet fromRecord = UQSRFromJson.buildUserQuestionSet(qp);
        //compare the two UserQuestionSets
        assertEquals(que, fromRecord);
    }
}

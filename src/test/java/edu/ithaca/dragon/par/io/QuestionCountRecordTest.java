package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionCountRecordTest {


    @Test
    public void constructorTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        QuestionCount qc1 = new QuestionCount(questionsFromFile.get(0));
        QuestionCountRecord qc1Record = new QuestionCountRecord(qc1);
        assertEquals(qc1.getQuestion().getId(), qc1Record.getQuestionId());
        assertEquals(qc1.getTimesSeen(), qc1Record.getTimesSeen());
        assertEquals(0, qc1Record.getFollowupCountRecords().size());

        QuestionCount qc2 = new QuestionCount(questionsFromFile.get(1));
        QuestionCountRecord qc2Record = new QuestionCountRecord(qc2);
        assertEquals(qc2.getQuestion().getId(), qc2Record.getQuestionId());
        assertEquals(qc2.getTimesSeen(), qc2Record.getTimesSeen());
        assertEquals(3, qc2Record.getFollowupCountRecords().size());
        assertEquals(0, qc2Record.getFollowupCountRecords().get(0).getFollowupCountRecords().size());
        assertEquals(0, qc2Record.getFollowupCountRecords().get(1).getFollowupCountRecords().size());
        assertEquals(1, qc2Record.getFollowupCountRecords().get(2).getFollowupCountRecords().size());
    }

    @Test
    public void buildQuestionCountTest() throws IOException {
        QuestionPool myQP = new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionPool.json"));
        QuestionCount qc1 = new QuestionCount(myQP.getAllQuestions().get(0));
        QuestionCountRecord qc1Record = new QuestionCountRecord(qc1);
        QuestionCount fromRecord = qc1Record.buildQuestionCount(myQP);
        assertEquals("plane./images/demoEquine14.jpg", fromRecord.getQuestion().getId());
        assertEquals(0, fromRecord.getTimesSeen());
        assertEquals(0, fromRecord.getFollowupCounts().size());

        QuestionCount qc2 = new QuestionCount(myQP.getAllQuestions().get(0));;
        QuestionCountRecord qc2Record = new QuestionCountRecord(qc2);
        fromRecord = qc2Record.buildQuestionCount(myQP);
        assertEquals("plane./images/demoEquine14.jpg", fromRecord.getQuestion().getId());
        assertEquals(0, fromRecord.getTimesSeen());
        assertEquals(3, fromRecord.getFollowupCounts().size());
        assertEquals(0, fromRecord.getFollowupCounts().get(0).getFollowupCounts().size());
        assertEquals(0, fromRecord.getFollowupCounts().get(1).getFollowupCounts().size());
        assertEquals(1, fromRecord.getFollowupCounts().get(2).getFollowupCounts().size());
    }

    @Test
    public void questionCountRecordToQuestionCountTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        List<QuestionCount>  questionCountList = new ArrayList<>();
        for (int i = 0; i<questionsFromFile.size(); i++){
            QuestionCount currQuestionCount = new QuestionCount(questionsFromFile.get(i));
            questionCountList.add(currQuestionCount);
        }
        List<QuestionCountRecord> questionCountRecordList = new ArrayList<>();




    }

}

package edu.ithaca.dragon.par.student.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.ithaca.dragon.par.analysis.QuestionHistorySummary;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceSimple;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentModelTest {

    @Test
    public void findQuestionSeenLeastRecentlyTest(){
        StudentModelJson poor = new StudentModelJson("poor", QuestionHistoryTest.poorStudent());
        assertEquals("googleQ", poor.findQuestionSeenLeastRecently(List.of("skyQ","mathQ","majorQ","yearQ","googleQ")));
        poor.addTimeSeen("googleQ", 20000L);
        assertEquals("skyQ", poor.findQuestionSeenLeastRecently(List.of("skyQ","mathQ","majorQ","yearQ","googleQ")));

    }

    @Test
    public void findQuestionsSeenLeastRecentlyTest(){
        StudentModelJson poor = new StudentModelJson("poor", QuestionHistoryTest.poorStudent());
        List<String> leastSeenQs = poor.findQuestionsSeenLeastRecently(List.of("skyQ","mathQ","majorQ","yearQ","googleQ"));
        assertEquals(1,leastSeenQs.size());
        assertEquals("googleQ", leastSeenQs.get(0));
        poor.addTimeSeen("googleQ", 20000L);
        leastSeenQs = poor.findQuestionsSeenLeastRecently(List.of("skyQ","mathQ","majorQ","yearQ","googleQ"));
        assertEquals(1,leastSeenQs.size());
        assertTrue(leastSeenQs.contains("skyQ"));
        
        
    }

    @Test
    void checkTimeLastSeen() {
        StudentModelJson poor = new StudentModelJson("poor", QuestionHistoryTest.poorStudent());

        assertEquals(5000L, poor.checkTimeLastSeen("googleQ"));
        assertEquals(6000L, poor.checkTimeLastSeen("skyQ"));
        assertEquals(8000L, poor.checkTimeLastSeen("majorQ"));
        assertEquals(11000L, poor.checkTimeLastSeen("yearQ"));

    }

    @Test
    public void ToAndFromJsonTest() throws IOException{
        StudentModelJson improvingStudent = new StudentModelJson("improvingStudent", QuestionHistoryTest.improvingStudent());
        DomainDatasourceSimple domainData = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
        QuestionHistorySummary qhs = new QuestionHistorySummary(improvingStudent.getQuestionHistories().values(), domainData);

        assertEquals(5, qhs.getQuestionIdsSeen().size());
        assertEquals(4, qhs.getQuestionIdsRespondedTo().size());  
        assertTrue(qhs.getQuestionIdsCorrectFirstTime().contains("mathQ"));
        assertTrue(qhs.getQuestionIdsCorrectFirstTime().contains("majorQ"));

        
        
    }
}
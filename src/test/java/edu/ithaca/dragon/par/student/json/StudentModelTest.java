package edu.ithaca.dragon.par.student.json;

import org.junit.jupiter.api.Test;

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
    void checkTimeLastSeen() {
        StudentModelJson poor = new StudentModelJson("poor", QuestionHistoryTest.poorStudent());

        assertEquals(5000L, poor.checkTimeLastSeen("googleQ"));
        assertEquals(6000L, poor.checkTimeLastSeen("skyQ"));
        assertEquals(8000L, poor.checkTimeLastSeen("majorQ"));
        assertEquals(11000L, poor.checkTimeLastSeen("yearQ"));

    }
}
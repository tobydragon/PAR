package edu.ithaca.dragon.par.student.json;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentModelTest {

    @Test
    public void findQuestionSeenLeastRecentlyTest(){
        StudentModelJson poor = new StudentModelJson("poor", QuestionHistoryTest.poorStudent());
        assertEquals("generic4", poor.findQuestionSeenLeastRecently(List.of("generic0","generic1","generic2","generic3","generic4")));
        poor.addTimeSeen("generic4", 20000L);
        assertEquals("generic0", poor.findQuestionSeenLeastRecently(List.of("generic0","generic1","generic2","generic3","generic4")));

    }

    @Test
    void checkTimeLastSeen() {
        StudentModelJson poor = new StudentModelJson("poor", QuestionHistoryTest.poorStudent());

        assertEquals(5000L, poor.checkTimeLastSeen("generic4"));
        assertEquals(6000L, poor.checkTimeLastSeen("generic0"));
        assertEquals(8000L, poor.checkTimeLastSeen("generic2"));
        assertEquals(11000L, poor.checkTimeLastSeen("generic3"));

    }
}
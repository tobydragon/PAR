package edu.ithaca.dragon.par.student.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentModelTest {

    @Test
    void checkTimeLastSeen() {
        StudentModel poor = new StudentModel("poor", QuestionHistoryTest.poorStudent());

        assertEquals(5000L, poor.checkTimeLastSeen("generic4"));
        assertEquals(6000L, poor.checkTimeLastSeen("generic0"));
        assertEquals(8000L, poor.checkTimeLastSeen("generic2"));
        assertEquals(11000L, poor.checkTimeLastSeen("generic3"));

    }
}
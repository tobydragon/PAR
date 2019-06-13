package edu.ithaca.dragon.par.studentModel;


import edu.ithaca.dragon.par.domain.Question;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class UserQuestionSetTest {

    @Test
    public void getLenTest(){
        UserQuestionSet que = new UserQuestionSet("99");
        int len = que.getLenOfSeenQuestions();
        assertEquals(0, len);

        que.givenQuestion("2");
        len = que.getLenOfSeenQuestions();
        assertEquals(1,len);

        que.givenQuestion("3");
        len = que.getLenOfSeenQuestions();
        assertEquals(2,len);

        len = que.getLenOfSeenQuestions();
        assertEquals(2,len);

        que.givenQuestion("5");
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

        que.givenQuestion("4");
        len = que.getLenOfSeenQuestions();
        assertEquals(4,len);

        que.givenQuestion("1");
        len = que.getLenOfSeenQuestions();
        assertEquals(5,len);

        que.givenQuestion("16");
        len = que.getLenOfSeenQuestions();
        assertEquals(5,len);

        que.givenQuestion("1");
        len = que.getLenOfSeenQuestions();
        assertEquals(5,len);


    }

    @Test
    public void getQTest(){
        UserQuestionSet que = new UserQuestionSet("100");
        Question q = que.getQ("2");
        assertEquals("What is your favorite color?", q.getQuestionText());

        q = que.getQ("1");
        assertEquals("What is your eye color?", q.getQuestionText());

        q = que.getQ("3");
        assertEquals("What color is the sky?", q.getQuestionText());

        q = que.getQ("5");
        assertEquals("How many ounces are in a pound?", q.getQuestionText());

        q = que.getQ("5");
        assertEquals("How many ounces are in a pound?", q.getQuestionText());

        q = que.getQ("4");
        assertEquals("What color is the grass?", q.getQuestionText());


    }

    @Test
    public void getTimesSeenTest(){
        UserQuestionSet que = new UserQuestionSet("101");
        int seen1 = que.getTimesSeen("1");
        assertEquals(-1, seen1);

        que.givenQuestion("2");
        int seen2 = que.getTimesSeen("2");
        assertEquals(0, seen2);

        que.givenQuestion("3");
        que.increaseTimesSeen("3");
        int seen3 = que.getTimesSeen("3");
        assertEquals(1, seen3);

        que.givenQuestion("4");
        que.increaseTimesSeen("4");
        que.increaseTimesSeen("4");
        que.increaseTimesSeen("4");
        int seen4 = que.getTimesSeen("4");
        assertEquals(3, seen4);

        int seen5 = que.getTimesSeen("5");
        assertEquals(-1, seen5);

        int seen6 = que.getTimesSeen("6");
        assertEquals(-1, seen6);

        int seen42 = que.getTimesSeen("42");
        assertEquals(-1, seen42);

    }


    @Test
    public void getUserIdTest(){
        UserQuestionSet que1 = new UserQuestionSet("1");
        String userId1 = que1.getUserId();
        assertEquals("1", userId1);

        UserQuestionSet que2 = new UserQuestionSet("2");
        String userId2 = que2.getUserId();
        assertEquals("2", userId2);

        UserQuestionSet que3 = new UserQuestionSet("3");
        String userId3 = que3.getUserId();
        assertEquals("3", userId3);

        UserQuestionSet que4 = new UserQuestionSet("557");
        String userId4 = que4.getUserId();
        assertEquals("557", userId4);

    }

    @Test
    public void increaseTimesSeenTest(){
        UserQuestionSet que = new UserQuestionSet("1");

        que.givenQuestion("3");
        int seen = que.getTimesSeen("3");
        assertEquals(0, seen);
        que.increaseTimesSeen("3");
        seen = que.getTimesSeen("3");
        assertEquals(1, seen);

        que.givenQuestion("4");
        seen = que.getTimesSeen("3");
        assertEquals(1, seen);
        que.increaseTimesSeen("3");
        que.increaseTimesSeen("3");
        seen = que.getTimesSeen("3");
        assertEquals(3, seen);

        seen = que.getTimesSeen("4");
        assertEquals(0, seen);
        que.increaseTimesSeen("4");
        seen = que.getTimesSeen("4");
        assertEquals(1, seen);
        seen = que.getTimesSeen("3");
        assertEquals(3, seen);

        seen = que.getTimesSeen("2");
        assertEquals(-1, seen);

        seen = que.getTimesSeen("7");
        assertEquals(-1, seen);

        seen = que.getTimesSeen("4");
        assertEquals(1, seen);

        seen = que.getTimesSeen("3");
        assertEquals(3, seen);

    }

    @Test
    public void getUnseenQuestionsTest(){
        UserQuestionSet que = new UserQuestionSet("1");
        List<Question> unseenList = que.getUnseenQuestions();
        int len = unseenList.size();
        assertEquals(5, len);

        que.givenQuestion("2");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(4, len);

        que.givenQuestion("2");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(4, len);

        que.givenQuestion("3");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(3, len);

        que.givenQuestion("6");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(3, len);

        que.givenQuestion("5");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(2, len);

        que.givenQuestion("4");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(1, len);

        que.givenQuestion("2");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(1, len);

        que.givenQuestion("1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(0, len);
    }

    @Test
    public void getSeenQuestionsTest(){
        UserQuestionSet que = new UserQuestionSet("1");
        List<Question> seenList = que.getSeenQuestions();
        int len = seenList.size();
        assertEquals(0, len);

        que.givenQuestion("2");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(1, len);

        que.givenQuestion("2");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(1, len);

        que.givenQuestion("3");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(2, len);

        que.givenQuestion("6");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(2, len);

        que.givenQuestion("5");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(3, len);

        que.givenQuestion("4");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(4, len);

        que.givenQuestion("2");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(4, len);

        que.givenQuestion("1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(5, len);

        que.givenQuestion("1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(5, len);
    }

    @Test
    public void givenQuestionTest(){
        UserQuestionSet que = new UserQuestionSet("14");

        List<Question> seen = que.getSeenQuestions();
        List<Question> unseen = que.getUnseenQuestions();
        int unseenLen = unseen.size();
        int seenLen = seen.size();
        assertEquals(5, unseenLen);
        assertEquals(0, seenLen);

        que.givenQuestion("2");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(4, unseenLen);
        assertEquals(1, seenLen);

        que.givenQuestion("2");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(4, unseenLen);
        assertEquals(1, seenLen);

        que.givenQuestion("3");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(3, unseenLen);
        assertEquals(2, seenLen);

        que.givenQuestion("5");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(2, unseenLen);
        assertEquals(3, seenLen);

        que.givenQuestion("1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(1, unseenLen);
        assertEquals(4, seenLen);

        que.givenQuestion("1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(1, unseenLen);
        assertEquals(4, seenLen);

        que.givenQuestion("4");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(0, unseenLen);
        assertEquals(5, seenLen);


    }

}

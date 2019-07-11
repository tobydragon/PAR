package edu.ithaca.dragon.par.studentModel;


import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.util.List;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UserQuestionSetOldTest {

    //two different questionSet objects that hold on to two different lists, but contain the same things
    //.equals is checking if the contents of the lists are the same, NOT if they are they same memory address - test this!
    @Test
    public void userQuestionSetEqualsTest() throws IOException{

        //compare two UserQuestionSets with different content (different Json files)
        List<Question> questionsFromFile1a = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<Question> questionsFromFile2a = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool2.json", Question.class);
        UserQuestionSetOld UQS1a = new UserQuestionSetOld("99", questionsFromFile1a);
        UserQuestionSetOld UQS2a = new UserQuestionSetOld("99", questionsFromFile2a);
        assertFalse(UQS1a.equals(UQS2a));

        //compare two UserQuestionSets with the same content, but different memory addresses
        List<Question> questionsFromFile1 = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<Question> questionsFromFile2 = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld UQS1 = new UserQuestionSetOld("99", questionsFromFile1);
        UserQuestionSetOld UQS2 = new UserQuestionSetOld("99", questionsFromFile2);
        assertEquals(UQS1, UQS2);

        //give a question from a UserQuestionSetOld, then compare
        UQS1.givenQuestion("StructureQ1");
        assertFalse(UQS1.equals(UQS2));

        //insure that the timesSeen of that question are different between UQS1 and UQS2
        assertEquals(1, UQS1.getTimesSeen("StructureQ1"));
        assertEquals(0, UQS2.getTimesSeen("StructureQ1"));

        //give the same question from the other UserQuestionSetOld, then compare
        UQS2.givenQuestion("StructureQ1");
        assertTrue(UQS1.equals(UQS2));
    }

    @Test
    public void getLenSeenTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que = new UserQuestionSetOld("99", questionsFromFile);

        //checks all questions are unseen
        int len = que.getLenOfSeenQuestions();
        assertEquals(0, len);

        //checks 1 question has been seen
        que.givenQuestion("PlaneQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(1,len);

        //checks 2 questions have been seen
        que.givenQuestion("StructureQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(2,len);

        //checks getLen does not change the length of seen list
        len = que.getLenOfSeenQuestions();
        assertEquals(2,len);

        //checks 3 questions have been seen
        que.givenQuestion("ZoneQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

        //checks length of seen questions list remains the same for a repeat question
        que.givenQuestion("ZoneQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

        //checks length of seen list does not change when invalid question ID is used
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("4");});
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

        //checks length of seen list does not change when invalid question ID is used
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("1");});
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

    }

    @Test
    public void getLenUnseenTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que = new UserQuestionSetOld("99", questionsFromFile);

        //checks all questions are seen
        int len = que.getLenOfUnseenQuestions();
        assertEquals(15, len);

        //checks 2 questions are unseen
        que.givenQuestion("PlaneQ1");
        len = que.getLenOfUnseenQuestions();
        assertEquals(14,len);

        //checks 1 question is unseen
        que.givenQuestion("StructureQ1");
        len = que.getLenOfUnseenQuestions();
        assertEquals(13,len);

        //checks getLen does not change the length of seen list
        len = que.getLenOfUnseenQuestions();
        assertEquals(13,len);

        //checks 0 questions are unseen
        que.givenQuestion("ZoneQ1");
        len = que.getLenOfUnseenQuestions();
        assertEquals(12,len);

        //checks length of unseen questions list remains the same for a repeat question
        que.givenQuestion("ZoneQ1");
        len = que.getLenOfUnseenQuestions();
        assertEquals(12,len);

        //checks length of unseen list does not change when invalid question ID is used
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("4");});
        len = que.getLenOfUnseenQuestions();
        assertEquals(12,len);

        //checks length of unseen list does not change when invalid question ID is used
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("1");});
        len = que.getLenOfUnseenQuestions();
        assertEquals(12,len);

    }

    @Test
    public void getTimesSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que = new UserQuestionSetOld("101", questionsFromFile);

        //checks question has not been seen
        int seen1 = que.getTimesSeen("PlaneQ1");
        assertEquals(0, seen1);

        //checks question has been seen once
        que.givenQuestion("PlaneQ1");
        int seen2 = que.getTimesSeen("PlaneQ1");
        assertEquals(1, seen2);

        //checks question has been seen twice
        que.givenQuestion("StructureQ1");
        que.increaseTimesSeen("StructureQ1");
        int seen3 = que.getTimesSeen("StructureQ1");
        assertEquals(2, seen3);

        //checks seeing question multiple times in a row
        que.givenQuestion("ZoneQ1");
        que.increaseTimesSeen("ZoneQ1");
        que.increaseTimesSeen("ZoneQ1");
        que.increaseTimesSeen("ZoneQ1");
        int seen4 = que.getTimesSeen("ZoneQ1");
        assertEquals(4, seen4);

        //checks with invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("5");});

        //checks with invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("6");});

        //checks with invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("42");});

    }


    @Test
    public void getUserIdTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que1 = new UserQuestionSetOld("1", questionsFromFile);

        //checks correct user ID is retrieved
        String userId1 = que1.getUserId();
        assertEquals("1", userId1);

        //checks correct user ID is retrieved
        UserQuestionSetOld que2 = new UserQuestionSetOld("2", questionsFromFile);
        String userId2 = que2.getUserId();
        assertEquals("2", userId2);

        //checks correct user ID is retrieved
        UserQuestionSetOld que3 = new UserQuestionSetOld("3", questionsFromFile);
        String userId3 = que3.getUserId();
        assertEquals("3", userId3);

        //checks correct user ID is retrieved
        UserQuestionSetOld que4 = new UserQuestionSetOld("557", questionsFromFile);
        String userId4 = que4.getUserId();
        assertEquals("557", userId4);

        //checks most previously accessed userQuestionSets to make sure user IDs have not been changed
        userId2 = que2.getUserId();
        assertEquals("2", userId2);
        userId1 = que1.getUserId();
        assertEquals("1", userId1);
        userId3 = que3.getUserId();
        assertEquals("3", userId3);

    }

    @Test
    public void increaseTimesSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que = new UserQuestionSetOld("1", questionsFromFile);

        //checks times seen increases consecutively
        que.givenQuestion("PlaneQ1");
        int seen = que.getTimesSeen("PlaneQ1");
        assertEquals(1, seen);
        boolean found = que.increaseTimesSeen("PlaneQ1");
        assertTrue(found);
        seen = que.getTimesSeen("PlaneQ1");
        assertEquals(2, seen);

        //checks times seen increases multiple times consecutively
        que.givenQuestion("StructureQ1");
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(1, seen);
        found = que.increaseTimesSeen("StructureQ1");
        assertTrue(found);
        found = que.increaseTimesSeen("StructureQ1");
        assertTrue(found);
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

        //checks unseen question doesn't have an increase in times seen(has not been seen)
        //checks other question does not have an increase in times seen
        seen = que.getTimesSeen("ZoneQ1");
        assertEquals(0, seen);
        que.increaseTimesSeen("ZoneQ1");
        seen = que.getTimesSeen("ZoneQ1");
        assertEquals(0, seen);
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

        found = que.increaseTimesSeen("hey hey hey :)");
        assertFalse(found);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("2");});

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("7");});

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("4");});

        //checks question has no increase in times seen
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

    }

    @Test
    public void getUnseenQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que = new UserQuestionSetOld("1", questionsFromFile);

        //checks all questions are unseen
        List<Question> unseenList = que.getUnseenQuestions();
        int len = unseenList.size();
        assertEquals(15, len);

        //decrease in number unseen
        que.givenQuestion("StructureQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(14, len);

        //no decrease in number unseen
        que.givenQuestion("StructureQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(14, len);

        //decrease in number unseen
        que.givenQuestion("ZoneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(13, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("6");});
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(13, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("5");});
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(13, len);

        //decrease in number unseen
        que.givenQuestion("PlaneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(12, len);

        //no decrease in number unseen
        que.givenQuestion("ZoneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(12, len);

        //no decrease in number unseen
        que.givenQuestion("PlaneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(12, len);
    }

    @Test
    public void getSeenQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que = new UserQuestionSetOld("1", questionsFromFile);

        //checks all questions are seen
        List<Question> seenList = que.getSeenQuestions();
        int len = seenList.size();
        assertEquals(0, len);

        //increase in number seen
        que.givenQuestion("StructureQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(1, len);

        //no increase in number seen
        que.givenQuestion("StructureQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(1, len);

        //increase in number seen
        que.givenQuestion("ZoneQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(2, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("6");});
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(2, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("5");});
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(2, len);

        //increase in number seen
        que.givenQuestion("PlaneQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(3, len);

        //no increase in number seen
        que.givenQuestion("ZoneQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(3, len);

        //no increase in number seen
        que.givenQuestion("PlaneQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(3, len);
    }

    @Test
    public void givenQuestionTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld que = new UserQuestionSetOld("14", questionsFromFile);

        //checks all questions are unseen before tests start
        List<Question> seen = que.getSeenQuestions();
        List<Question> unseen = que.getUnseenQuestions();
        int unseenLen = unseen.size();
        int seenLen = seen.size();
        assertEquals(15, unseenLen);
        assertEquals(0, seenLen);
        for (int i = 0; i <unseen.size(); i++){
            int numSeen = que.getTimesSeen(unseen.get(i).getId());
            assertEquals(0, numSeen);
        }


        //Time seen increase for PlaneQ1, length of seen and unseen list change
        que.givenQuestion("PlaneQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(14, unseenLen);
        assertEquals(1, seenLen);
        int ts = que.getTimesSeen("PlaneQ1");
        assertEquals(1, ts);

        //Time seen increase for ZoneQ1, length of seen and unseen list change
        que.givenQuestion("ZoneQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(13, unseenLen);
        assertEquals(2, seenLen);
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(1, ts);

        //Invalid Question ID, no values change
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("3");});
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(13, unseenLen);
        assertEquals(2, seenLen);
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("3");});

        //already seen question, seen and unseen list lengths do not change, times seen increases
        que.givenQuestion("PlaneQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(13, unseenLen);
        assertEquals(2, seenLen);
        ts = que.getTimesSeen("PlaneQ1");
        assertEquals(2, ts);

        //new question, seen and unseen question lists change in length, times seen is 1
        que.givenQuestion("StructureQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(12, unseenLen);
        assertEquals(3, seenLen);
        ts = que.getTimesSeen("StructureQ1");
        assertEquals(1, ts);

        //invalid question ID, no values change
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("1");});
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(12, unseenLen);
        assertEquals(3, seenLen);
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("1");});

        //already seen question, times seen increases, seen and unseen lists remain unchanged
        que.givenQuestion("ZoneQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(12, unseenLen);
        assertEquals(3, seenLen);
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(2, ts);

        //increase in times seen
        que.givenQuestion("ZoneQ1");
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(3, ts);

        //increase in times seen
        que.givenQuestion("ZoneQ1");
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(4, ts);

        //increase in times seen
        que.givenQuestion("ZoneQ1");
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(5, ts);

        //increase in times seen
        que.givenQuestion("ZoneQ1");
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(6, ts);

        //no change in times seen
        ts = que.getTimesSeen("PlaneQ1");
        assertEquals(2, ts);

        //no change in times seen
        ts = que.getTimesSeen("StructureQ1");
        assertEquals(1, ts);

        //no change in times seen
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(6, ts);

        //increase in times seen
        que.givenQuestion("PlaneQ1");
        ts = que.getTimesSeen("PlaneQ1");
        assertEquals(3, ts);


        //new question set
        List<Question> qFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSetOld qSet = new UserQuestionSetOld("14", qFromFile);

        //checks all questions have been seen 0 times
        List<Question> unseenQ = qSet.getUnseenQuestions();
        for (int i = 0; i <unseenQ.size(); i++){
            int numSeen = qSet.getTimesSeen(unseenQ.get(i).getId());
            assertEquals(0, numSeen);
        }

        //increase in times seen
        qSet.givenQuestion("PlaneQ1");
        qSet.givenQuestion("PlaneQ1");
        ts = qSet.getTimesSeen("PlaneQ1");
        assertEquals(2, ts);

        //other question seen values have not been changed
        ts = qSet.getTimesSeen("ZoneQ1");
        assertEquals(0, ts);
        ts = qSet.getTimesSeen("StructureQ1");
        assertEquals(0, ts);

        //increase in times seen
        qSet.givenQuestion("ZoneQ1");
        qSet.givenQuestion("ZoneQ1");
        qSet.givenQuestion("ZoneQ1");
        ts = qSet.getTimesSeen("ZoneQ1");
        assertEquals(3, ts);

        //other question seen values have not been changed
        ts = qSet.getTimesSeen("PlaneQ1");
        assertEquals(2, ts);
        ts = qSet.getTimesSeen("StructureQ1");
        assertEquals(0, ts);

    }

}

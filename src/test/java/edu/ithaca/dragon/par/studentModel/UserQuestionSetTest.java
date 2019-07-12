package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.UserQuestionSetRecord;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UserQuestionSetTest {

    //two different questionSet objects that hold on to two different lists, but contain the same things
    //.equals is checking if the contents of the lists are the same, NOT if they are they same memory address - test this!
    @Test
    public void userQuestionSetEqualsTest() throws IOException{

        //compare two UserQuestionSets with different content (different Json files)
        List<Question> questionsFromFile1a = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<Question> questionsFromFile2a = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool2.json", Question.class);
        UserQuestionSet UQS1a = UserQuestionSet.buildNewUserQuestionSetFromQuestions("99", questionsFromFile1a);
        UserQuestionSet UQS2a = UserQuestionSet.buildNewUserQuestionSetFromQuestions("99", questionsFromFile2a);
        assertFalse(UQS1a.equals(UQS2a));

        //compare two UserQuestionSets with the same content, but different memory addresses
        List<Question> questionsFromFile1 = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<Question> questionsFromFile2 = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet UQS1 = UserQuestionSet.buildNewUserQuestionSetFromQuestions("99", questionsFromFile1);
        UserQuestionSet UQS2 = UserQuestionSet.buildNewUserQuestionSetFromQuestions("99", questionsFromFile2);
        assertEquals(UQS1, UQS2);

        //give a question from a UserQuestionSet, then compare
        UQS1.givenQuestion("StructureQ1");
        assertFalse(UQS1.equals(UQS2));

        //insure that the timesSeen of that question are different between UQS1 and UQS2
        assertEquals(1, UQS1.getTimesSeen("StructureQ1"));
        assertEquals(0, UQS2.getTimesSeen("StructureQ1"));

        //give the same question from the other UserQuestionSet, then compare
        UQS2.givenQuestion("StructureQ1");
        assertTrue(UQS1.equals(UQS2));
    }

    @Test
    public void getTimesSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("101", questionsFromFile);

        //checks question has not been seen
        int seen1 = que.getTimesSeen("PlaneQ1");
        assertEquals(0, seen1);

        //checks question has been seen once
        que.givenQuestion("PlaneQ1");
        int seen2 = que.getTimesSeen("PlaneQ1");
        assertEquals(1, seen2);

        //checks question has been seen twice
        que.givenQuestion("StructureQ1");
        que.givenQuestion("StructureQ1");
        int seen3 = que.getTimesSeen("StructureQ1");
        assertEquals(2, seen3);

        //checks seeing question multiple times in a row
        que.givenQuestion("ZoneQ1");
        que.givenQuestion("ZoneQ1");
        que.givenQuestion("ZoneQ1");
        que.givenQuestion("ZoneQ1");
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
        UserQuestionSet que1 = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questionsFromFile);

        //checks correct user ID is retrieved
        String userId1 = que1.getUserId();
        assertEquals("1", userId1);

        //checks correct user ID is retrieved
        UserQuestionSet que2 = UserQuestionSet.buildNewUserQuestionSetFromQuestions("2", questionsFromFile);
        String userId2 = que2.getUserId();
        assertEquals("2", userId2);

        //checks correct user ID is retrieved
        UserQuestionSet que3 = UserQuestionSet.buildNewUserQuestionSetFromQuestions("3", questionsFromFile);
        String userId3 = que3.getUserId();
        assertEquals("3", userId3);

        //checks correct user ID is retrieved
        UserQuestionSet que4 = UserQuestionSet.buildNewUserQuestionSetFromQuestions("557", questionsFromFile);
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
    public void givenQuestionTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questionsFromFile);

        //checks times seen increases consecutively
        que.givenQuestion("PlaneQ1");
        int seen = que.getTimesSeen("PlaneQ1");
        assertEquals(1, seen);
        que.givenQuestion("PlaneQ1");
        seen = que.getTimesSeen("PlaneQ1");
        assertEquals(2, seen);

        //checks times seen increases multiple times consecutively
        que.givenQuestion("StructureQ1");
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(1, seen);
        que.givenQuestion("StructureQ1");
        que.givenQuestion("StructureQ1");
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

        //checks unseen question doesn't have an increase in times seen(has not been seen)
        //checks other question does not have an increase in times seen
        seen = que.getTimesSeen("ZoneQ1");
        assertEquals(0, seen);
        que.givenQuestion("ZoneQ1");
        seen = que.getTimesSeen("ZoneQ1");
        assertEquals(1, seen);
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

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
    public void getQuestionsTest() throws IOException{

    }


    @Test
    public void givenQuestionTest2() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("14", questionsFromFile);

        //checks all questions are unseen before tests start
        for (int i = 0; i <que.getQuestionCounts().size(); i++){
            int numSeen = que.getTimesSeen(que.getQuestionCounts().get(i).getQuestion().getId());
            assertEquals(0, numSeen);
        }


        //Time seen increase for PlaneQ1, length of seen and unseen list change
        que.givenQuestion("PlaneQ1");
        int ts = que.getTimesSeen("PlaneQ1");
        assertEquals(1, ts);

        //Time seen increase for ZoneQ1, length of seen and unseen list change
        que.givenQuestion("ZoneQ1");
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(1, ts);

        //Invalid Question ID, no values change
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("3");});
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("3");});

        //already seen question, seen and unseen list lengths do not change, times seen increases
        que.givenQuestion("PlaneQ1");
        ts = que.getTimesSeen("PlaneQ1");
        assertEquals(2, ts);

        //new question, seen and unseen question lists change in length, times seen is 1
        que.givenQuestion("StructureQ1");
        ts = que.getTimesSeen("StructureQ1");
        assertEquals(1, ts);

        //invalid question ID, no values change
        assertThrows(RuntimeException.class, () -> { que.givenQuestion("1");});
        assertThrows(RuntimeException.class, () -> { que.getTimesSeen("1");});

        //already seen question, times seen increases, seen and unseen lists remain unchanged
        que.givenQuestion("ZoneQ1");
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
        UserQuestionSet qSet = UserQuestionSet.buildNewUserQuestionSetFromQuestions("14", qFromFile);


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

    @Test
    public void getUnseenQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questionsFromFile);

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
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questionsFromFile);

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
    public void writeUserQuestionSet() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet userQuestionSet = UserQuestionSet.buildNewUserQuestionSetFromQuestions("kerryAnne", questionsFromFile);
        userQuestionSet.givenQuestion("PlaneQ1");
        userQuestionSet.givenQuestion("ZoneQ1");
        UserQuestionSetRecord uqsr = new UserQuestionSetRecord(userQuestionSet);

        JsonUtil.toJsonFile("src/test/resources/autoGenerated/newUserQuestionSet.json", uqsr);

        Path path1 = Paths.get("src/test/resources/autoGenerated/newUserQuestionSet.json");
        Files.deleteIfExists(path1);
        //TODO : what should we do with this
    }


}

package edu.ithaca.dragon.par.studentModel;


import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.util.List;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UserQuestionSetTest {

    @Test
    public void getLenTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que = new UserQuestionSet("99", questionsFromFile);
        int len = que.getLenOfSeenQuestions();
        assertEquals(0, len);

        que.givenQuestion("PlaneQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(1,len);

        que.givenQuestion("StructureQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(2,len);

        len = que.getLenOfSeenQuestions();
        assertEquals(2,len);

        que.givenQuestion("ZoneQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

        que.givenQuestion("ZoneQ1");
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

        que.givenQuestion("4");
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

        que.givenQuestion("1");
        len = que.getLenOfSeenQuestions();
        assertEquals(3,len);

    }

    @Test
    public void getQTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que = new UserQuestionSet("100", questionsFromFile);
        Question q = que.getQ("PlaneQ1");
        assertEquals("On which plane is the ultrasound taken?", q.getQuestionText());

        q = que.getQ("StructureQ1");
        assertEquals("What structure is in the near field?", q.getQuestionText());

        q = que.getQ("ZoneQ1");
        assertEquals("In what zone is this ultrasound taken?", q.getQuestionText());

        q = que.getQ("ZoneQ1");
        assertEquals("In what zone is this ultrasound taken?", q.getQuestionText());

        q = que.getQ("5");
        assertNull(q);

        q = que.getQ("4");
        assertNull(q);


    }

    @Test
    public void getTimesSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que = new UserQuestionSet("101", questionsFromFile);
        int seen1 = que.getTimesSeen("PlaneQ1");
        assertEquals(0, seen1);

        que.givenQuestion("PlaneQ1");
        int seen2 = que.getTimesSeen("PlaneQ1");
        assertEquals(1, seen2);

        que.givenQuestion("StructureQ1");
        que.increaseTimesSeen("StructureQ1");
        int seen3 = que.getTimesSeen("StructureQ1");
        assertEquals(2, seen3);

        que.givenQuestion("ZoneQ1");
        que.increaseTimesSeen("ZoneQ1");
        que.increaseTimesSeen("ZoneQ1");
        que.increaseTimesSeen("ZoneQ1");
        int seen4 = que.getTimesSeen("ZoneQ1");
        assertEquals(4, seen4);

        int seen5 = que.getTimesSeen("5");
        assertEquals(0, seen5);

        int seen6 = que.getTimesSeen("6");
        assertEquals(0, seen6);

        int seen42 = que.getTimesSeen("42");
        assertEquals(0, seen42);

    }


    @Test
    public void getUserIdTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que1 = new UserQuestionSet("1", questionsFromFile);
        String userId1 = que1.getUserId();
        assertEquals("1", userId1);

        UserQuestionSet que2 = new UserQuestionSet("2", questionsFromFile);
        String userId2 = que2.getUserId();
        assertEquals("2", userId2);

        UserQuestionSet que3 = new UserQuestionSet("3", questionsFromFile);
        String userId3 = que3.getUserId();
        assertEquals("3", userId3);

        UserQuestionSet que4 = new UserQuestionSet("557", questionsFromFile);
        String userId4 = que4.getUserId();
        assertEquals("557", userId4);

    }

    @Test
    public void increaseTimesSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que = new UserQuestionSet("1", questionsFromFile);

        que.givenQuestion("PlaneQ1");
        int seen = que.getTimesSeen("PlaneQ1");
        assertEquals(1, seen);
        que.increaseTimesSeen("PlaneQ1");
        seen = que.getTimesSeen("PlaneQ1");
        assertEquals(2, seen);

        que.givenQuestion("StructureQ1");
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(1, seen);
        que.increaseTimesSeen("StructureQ1");
        que.increaseTimesSeen("StructureQ1");
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

        seen = que.getTimesSeen("ZoneQ1");
        assertEquals(0, seen);
        que.increaseTimesSeen("ZoneQ1");
        seen = que.getTimesSeen("ZoneQ1");
        assertEquals(0, seen);
        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

        seen = que.getTimesSeen("2");
        assertEquals(0, seen);

        seen = que.getTimesSeen("7");
        assertEquals(0, seen);

        seen = que.getTimesSeen("4");
        assertEquals(0, seen);

        seen = que.getTimesSeen("StructureQ1");
        assertEquals(3, seen);

    }

    @Test
    public void getUnseenQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que = new UserQuestionSet("1", questionsFromFile);
        List<Question> unseenList = que.getUnseenQuestions();
        int len = unseenList.size();
        assertEquals(3, len);

        que.givenQuestion("StructureQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(2, len);

        que.givenQuestion("StructureQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(2, len);

        que.givenQuestion("ZoneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(1, len);

        que.givenQuestion("6");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(1, len);

        que.givenQuestion("5");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(1, len);

        que.givenQuestion("PlaneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(0, len);

        que.givenQuestion("ZoneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(0, len);

        que.givenQuestion("PlaneQ1");
        unseenList = que.getUnseenQuestions();
        len = unseenList.size();
        assertEquals(0, len);
    }

    @Test
    public void getSeenQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que = new UserQuestionSet("1", questionsFromFile);
        List<Question> seenList = que.getSeenQuestions();
        int len = seenList.size();
        assertEquals(0, len);

        que.givenQuestion("StructureQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(1, len);

        que.givenQuestion("StructureQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(1, len);

        que.givenQuestion("ZoneQ1");
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
        assertEquals(2, len);

        que.givenQuestion("PlaneQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(3, len);

        que.givenQuestion("ZoneQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(3, len);

        que.givenQuestion("PlaneQ1");
        seenList = que.getSeenQuestions();
        len = seenList.size();
        assertEquals(3, len);
    }

    @Test
    public void givenQuestionTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet que = new UserQuestionSet("14", questionsFromFile);

        //checks all questions are unseen before tests start
        List<Question> seen = que.getSeenQuestions();
        List<Question> unseen = que.getUnseenQuestions();
        int unseenLen = unseen.size();
        int seenLen = seen.size();
        assertEquals(3, unseenLen);
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
        assertEquals(2, unseenLen);
        assertEquals(1, seenLen);
        int ts = que.getTimesSeen("PlaneQ1");
        assertEquals(1, ts);

        //Time seen increase for ZoneQ1, length of seen and unseen list change
        que.givenQuestion("ZoneQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(1, unseenLen);
        assertEquals(2, seenLen);
        ts = que.getTimesSeen("ZoneQ1");
        assertEquals(1, ts);

        //Invalid Question ID, no values change
        que.givenQuestion("3");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(1, unseenLen);
        assertEquals(2, seenLen);
        ts = que.getTimesSeen("3");
        assertEquals(0, ts);

        //already seen question, seen and unseen list lengths do not change, times seen increases
        que.givenQuestion("PlaneQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(1, unseenLen);
        assertEquals(2, seenLen);
        ts = que.getTimesSeen("PlaneQ1");
        assertEquals(2, ts);

        //new question, seen and unseen question lists change in length, times seen is 1
        que.givenQuestion("StructureQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(0, unseenLen);
        assertEquals(3, seenLen);
        ts = que.getTimesSeen("StructureQ1");
        assertEquals(1, ts);

        //invalid question ID, no values change
        que.givenQuestion("1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(0, unseenLen);
        assertEquals(3, seenLen);
        ts = que.getTimesSeen("1");
        assertEquals(0, ts);

        //already seen question, times seen increases, seen and unseen lists remain unchanged
        que.givenQuestion("ZoneQ1");
        seen = que.getSeenQuestions();
        unseen = que.getUnseenQuestions();
        unseenLen = unseen.size();
        seenLen = seen.size();
        assertEquals(0, unseenLen);
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
        List<Question> qFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet qSet = new UserQuestionSet("14", qFromFile);

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

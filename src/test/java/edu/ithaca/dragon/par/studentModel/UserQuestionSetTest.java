package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.par.io.StudentModelRecord;
import edu.ithaca.dragon.par.io.UserQuestionSetRecord;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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
        UQS1.increaseTimesAttempted("StructureQ1");
        assertFalse(UQS1.equals(UQS2));

        //insure that the timesAttempted of that question are different between UQS1 and UQS2
        assertEquals(1, UQS1.getTimesAttempted("StructureQ1"));
        assertEquals(0, UQS2.getTimesAttempted("StructureQ1"));

        //give the same question from the other UserQuestionSet, then compare
        UQS2.increaseTimesAttempted("StructureQ1");
        assertTrue(UQS1.equals(UQS2));
    }

    @Test
    public void getTimesSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("101", questionsFromFile);

        //checks question has not been seen
        int seen1 = que.getTimesAttempted("PlaneQ1");
        assertEquals(0, seen1);

        //checks question has been seen once
        que.increaseTimesAttempted("PlaneQ1");
        int seen2 = que.getTimesAttempted("PlaneQ1");
        assertEquals(1, seen2);

        //checks question has been seen twice
        que.increaseTimesAttempted("StructureQ1");
        que.increaseTimesAttempted("StructureQ1");
        int seen3 = que.getTimesAttempted("StructureQ1");
        assertEquals(2, seen3);

        //checks seeing question multiple times in a row
        que.increaseTimesAttempted("ZoneQ1");
        que.increaseTimesAttempted("ZoneQ1");
        que.increaseTimesAttempted("ZoneQ1");
        que.increaseTimesAttempted("ZoneQ1");
        int seen4 = que.getTimesAttempted("ZoneQ1");
        assertEquals(4, seen4);

        //checks with invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("5");});

        //checks with invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("6");});

        //checks with invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("42");});

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
        que.increaseTimesAttempted("PlaneQ1");
        int seen = que.getTimesAttempted("PlaneQ1");
        assertEquals(1, seen);
        que.increaseTimesAttempted("PlaneQ1");
        seen = que.getTimesAttempted("PlaneQ1");
        assertEquals(2, seen);

        //checks times seen increases multiple times consecutively
        que.increaseTimesAttempted("StructureQ1");
        seen = que.getTimesAttempted("StructureQ1");
        assertEquals(1, seen);
        que.increaseTimesAttempted("StructureQ1");
        que.increaseTimesAttempted("StructureQ1");
        seen = que.getTimesAttempted("StructureQ1");
        assertEquals(3, seen);

        //checks unseen question doesn't have an increase in times seen(has not been seen)
        //checks other question does not have an increase in times seen
        seen = que.getTimesAttempted("ZoneQ1");
        assertEquals(0, seen);
        que.increaseTimesAttempted("ZoneQ1");
        seen = que.getTimesAttempted("ZoneQ1");
        assertEquals(1, seen);
        seen = que.getTimesAttempted("StructureQ1");
        assertEquals(3, seen);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("2");});

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("7");});

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("4");});

        //checks question has no increase in times seen
        seen = que.getTimesAttempted("StructureQ1");
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
            int numSeen = que.getTimesAttempted(que.getQuestionCounts().get(i).getQuestion().getId());
            assertEquals(0, numSeen);
        }


        //Time seen increase for PlaneQ1, length of seen and unseen list change
        que.increaseTimesAttempted("PlaneQ1");
        int ts = que.getTimesAttempted("PlaneQ1");
        assertEquals(1, ts);

        //Time seen increase for ZoneQ1, length of seen and unseen list change
        que.increaseTimesAttempted("ZoneQ1");
        ts = que.getTimesAttempted("ZoneQ1");
        assertEquals(1, ts);

        //Invalid Question ID, no values change
        assertThrows(RuntimeException.class, () -> { que.increaseTimesAttempted("3");});
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("3");});

        //already seen question, seen and unseen list lengths do not change, times seen increases
        que.increaseTimesAttempted("PlaneQ1");
        ts = que.getTimesAttempted("PlaneQ1");
        assertEquals(2, ts);

        //new question, seen and unseen question lists change in length, times seen is 1
        que.increaseTimesAttempted("StructureQ1");
        ts = que.getTimesAttempted("StructureQ1");
        assertEquals(1, ts);

        //invalid question ID, no values change
        assertThrows(RuntimeException.class, () -> { que.increaseTimesAttempted("1");});
        assertThrows(RuntimeException.class, () -> { que.getTimesAttempted("1");});

        //already seen question, times seen increases, seen and unseen lists remain unchanged
        que.increaseTimesAttempted("ZoneQ1");
        ts = que.getTimesAttempted("ZoneQ1");
        assertEquals(2, ts);

        //increase in times seen
        que.increaseTimesAttempted("ZoneQ1");
        ts = que.getTimesAttempted("ZoneQ1");
        assertEquals(3, ts);

        //increase in times seen
        que.increaseTimesAttempted("ZoneQ1");
        ts = que.getTimesAttempted("ZoneQ1");
        assertEquals(4, ts);

        //increase in times seen
        que.increaseTimesAttempted("ZoneQ1");
        ts = que.getTimesAttempted("ZoneQ1");
        assertEquals(5, ts);

        //increase in times seen
        que.increaseTimesAttempted("ZoneQ1");
        ts = que.getTimesAttempted("ZoneQ1");
        assertEquals(6, ts);

        //no change in times seen
        ts = que.getTimesAttempted("PlaneQ1");
        assertEquals(2, ts);

        //no change in times seen
        ts = que.getTimesAttempted("StructureQ1");
        assertEquals(1, ts);

        //no change in times seen
        ts = que.getTimesAttempted("ZoneQ1");
        assertEquals(6, ts);

        //increase in times seen
        que.increaseTimesAttempted("PlaneQ1");
        ts = que.getTimesAttempted("PlaneQ1");
        assertEquals(3, ts);


        //new question set
        List<Question> qFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet qSet = UserQuestionSet.buildNewUserQuestionSetFromQuestions("14", qFromFile);


        //increase in times seen
        qSet.increaseTimesAttempted("PlaneQ1");
        qSet.increaseTimesAttempted("PlaneQ1");
        ts = qSet.getTimesAttempted("PlaneQ1");
        assertEquals(2, ts);

        //other question seen values have not been changed
        ts = qSet.getTimesAttempted("ZoneQ1");
        assertEquals(0, ts);
        ts = qSet.getTimesAttempted("StructureQ1");
        assertEquals(0, ts);

        //increase in times seen
        qSet.increaseTimesAttempted("ZoneQ1");
        qSet.increaseTimesAttempted("ZoneQ1");
        qSet.increaseTimesAttempted("ZoneQ1");
        ts = qSet.getTimesAttempted("ZoneQ1");
        assertEquals(3, ts);

        //other question seen values have not been changed
        ts = qSet.getTimesAttempted("PlaneQ1");
        assertEquals(2, ts);
        ts = qSet.getTimesAttempted("StructureQ1");
        assertEquals(0, ts);

    }

    @Test
    public void getUnseenQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questionsFromFile);

        //checks all questions are unseen
        List<Question> unseenList = que.getTopLevelUnattemptedQuestions();
        int len = unseenList.size();
        assertEquals(15, len);

        //decrease in number unseen
        que.increaseTimesAttempted("StructureQ1");
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(14, len);

        //no decrease in number unseen
        que.increaseTimesAttempted("StructureQ1");
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(14, len);

        //decrease in number unseen
        que.increaseTimesAttempted("ZoneQ1");
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(13, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.increaseTimesAttempted("6");});
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(13, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.increaseTimesAttempted("5");});
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(13, len);

        //decrease in number unseen
        que.increaseTimesAttempted("PlaneQ1");
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(12, len);

        //no decrease in number unseen
        que.increaseTimesAttempted("ZoneQ1");
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(12, len);

        //no decrease in number unseen
        que.increaseTimesAttempted("PlaneQ1");
        unseenList = que.getTopLevelUnattemptedQuestions();
        len = unseenList.size();
        assertEquals(12, len);
    }


    @Test
    public void getSeenQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questionsFromFile);

        //checks all questions are seen
        List<Question> seenList = que.getTopLevelAttemptedQuestions();
        int len = seenList.size();
        assertEquals(0, len);

        //increase in number seen
        que.increaseTimesAttempted("StructureQ1");
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(1, len);

        //no increase in number seen
        que.increaseTimesAttempted("StructureQ1");
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(1, len);

        //increase in number seen
        que.increaseTimesAttempted("ZoneQ1");
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(2, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.increaseTimesAttempted("6");});
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(2, len);

        //invalid question ID
        assertThrows(RuntimeException.class, () -> { que.increaseTimesAttempted("5");});
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(2, len);

        //increase in number seen
        que.increaseTimesAttempted("PlaneQ1");
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(3, len);

        //no increase in number seen
        que.increaseTimesAttempted("ZoneQ1");
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(3, len);

        //no increase in number seen
        que.increaseTimesAttempted("PlaneQ1");
        seenList = que.getTopLevelAttemptedQuestions();
        len = seenList.size();
        assertEquals(3, len);
    }
    @Test
    public void getAllQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/simpleTestSet/currentQuestionPool.json", Question.class);
        UserQuestionSet que = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questionsFromFile);
        List<Question> allQuestions = que.getAllQuestions();
        que.increaseTimesAttemptedAllQuestions(allQuestions);
        assertEquals(16,allQuestions.size());

    }


    @Test
    public void writeUserQuestionSet() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet userQuestionSet = UserQuestionSet.buildNewUserQuestionSetFromQuestions("kerryAnne", questionsFromFile);
        userQuestionSet.increaseTimesAttempted("PlaneQ1");
        userQuestionSet.increaseTimesAttempted("ZoneQ1");
        UserQuestionSetRecord uqsr = new UserQuestionSetRecord(userQuestionSet);

        JsonUtil.toJsonFile("src/test/resources/autoGenerated/newUserQuestionSet.json", uqsr);

        Path path1 = Paths.get("src/test/resources/autoGenerated/newUserQuestionSet.json");
        Files.deleteIfExists(path1);
        //TODO : what should we do with this
    }


    @Test
    public void increaseTimesSeenAllQuestionsTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        questions = questions.subList(0,5);
        StudentModel studentModel = new StudentModel("TestUser1", questions);
        studentModel.getUserQuestionSet().increaseTimesAttemptedAllQuestions(questions);
        assertEquals(1, studentModel.getUserQuestionSet().getQuestionCounts().get(0).getTimesAttempted());
        assertEquals(1, studentModel.getUserQuestionSet().getQuestionCounts().get(1).getFollowupCounts().get(0).getTimesAttempted());
        assertEquals(1, studentModel.getUserQuestionSet().getQuestionCounts().get(1).getFollowupCounts().get(2).getFollowupCounts().get(0).getTimesAttempted());
        studentModel.getUserQuestionSet().increaseTimesAttemptedAllQuestions(questions);
        assertEquals(2, studentModel.getUserQuestionSet().getQuestionCounts().get(0).getTimesAttempted());
        assertEquals(2, studentModel.getUserQuestionSet().getQuestionCounts().get(1).getFollowupCounts().get(0).getTimesAttempted());
        assertEquals(2, studentModel.getUserQuestionSet().getQuestionCounts().get(1).getFollowupCounts().get(2).getFollowupCounts().get(0).getTimesAttempted());


        StudentModel studentModel2 = new StudentModel("TestUser1", questions);
        questions = questions.subList(0,4);
        studentModel2.getUserQuestionSet().increaseTimesAttemptedAllQuestions(questions);
        assertEquals(1, studentModel2.getUserQuestionSet().getQuestionCounts().get(0).getTimesAttempted());
        assertEquals(1, studentModel2.getUserQuestionSet().getQuestionCounts().get(0).getTimesAttempted());



    }

    @Test
    public void addQuestionTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFollowup.json", Question.class);
        StudentModel studentModel = new StudentModel("TestUser1", questions);
        Question q = new Question("Question1", "What is this question?", "Good", "A very good one", Arrays.asList("A very good one", "A great one", ":("), "/images/AnImage");
        studentModel.getUserQuestionSet().addQuestion(q);
        assertEquals(48, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().size());
        assertEquals(q, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().get(47));


        Question q2 = new Question("Question2", "What is a question?", "Question", "Something important", Arrays.asList("Something important", ":)", ">:/"), "/images/aBetterImage");
        studentModel.getUserQuestionSet().addQuestion(q2);
        assertEquals(49, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().size());
        assertEquals(q2, studentModel.getUserQuestionSet().getTopLevelUnattemptedQuestions().get(48));

        //TODO: see implementation for detail
        //assertThrows(RuntimeException.class, ()-> {studentModel.getUserQuestionSet().addQuestion(q2);});
    }

    @Test
    public void questionTypesMapTest() throws IOException{
        List<Question> questions= JsonUtil.listFromJsonFile("src/test/resources/author/DemoQuestionPoolFewFollowups.json", Question.class);
        UserQuestionSet que1 = UserQuestionSet.buildNewUserQuestionSetFromQuestions("1", questions);

        Map<String,List<QuestionCount>> questionByTypesMap= que1.questionCountsByTypeMap();

        assertEquals(4,questionByTypesMap.size());
        //do bonus and attachment not being recorded
        assertEquals(13,questionByTypesMap.get(EquineQuestionTypes.plane.toString()).size());
        assertEquals(27,questionByTypesMap.get(EquineQuestionTypes.structure.toString()).size());
        assertEquals(7,questionByTypesMap.get(EquineQuestionTypes.attachment.toString()).size());
        assertEquals(10,questionByTypesMap.get(EquineQuestionTypes.zone.toString()).size());
    }

    @Test
    public void getAllResponseCountTest() throws IOException{
        QuestionPool myQP = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/testFullQP.json").getAllQuestions());

        //mastered student
        StudentModelRecord smr = JsonUtil.fromJsonFile("src/test/resources/author/students/masteredStudent.json", StudentModelRecord.class);
        StudentModel masteredStudentModel = smr.buildStudentModel(myQP);
        assertEquals(26, masteredStudentModel.getUserResponseSet().getAllResponseCount());

        //level 3 student
        StudentModelRecord  smr2 = JsonUtil.fromJsonFile("src/test/resources/author/students/level4Student.json", StudentModelRecord.class);
        StudentModel level4Student = smr2.buildStudentModel(myQP);
        assertEquals(11, level4Student.getUserResponseSet().getAllResponseCount());

        //brand new student
        List<Question> noQuestions = new ArrayList<Question>();
        StudentModel student = new StudentModel("student", noQuestions);
        assertEquals(0, student.getUserResponseSet().getAllResponseCount());

    }

}

package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;
import edu.ithaca.dragon.util.JsonUtil;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImageTaskChooserTest {


    @Test
    public void nextImageTaskSingleTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("2", questionsFromFile);
        ImageTask im1 = ImageTaskChooser.nextImageTaskSingle(q, 1);
        assertEquals(1, im1.getTaskQuestions().get(0).getType());
        int len = q.getLenOfSeenQuestions();
        assertEquals(1, len);

        ImageTask im2 = ImageTaskChooser.nextImageTaskSingle(q, 2);
        assertEquals(2, im2.getTaskQuestions().get(0).getType());
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        ImageTask im3 = ImageTaskChooser.nextImageTaskSingle(q, 9);
        assertNull(im3);
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        ImageTask im4 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        assertEquals(4, im4.getTaskQuestions().get(0).getType());
        len = q.getLenOfSeenQuestions();
        assertEquals(3, len);

    }

    @Test
    public void nextImageTaskSingleIT() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);


        ImageTask im1 = ImageTaskChooser.nextImageTaskSingle(q, 2);
        Question q1 = new Question("PlaneQ4","On which plane is the ultrasound taken?",2, "Lateral", Arrays.asList("Transverse", "Lateral", "I don't know"), "../static/images/demoEquine09.jpg");
        ImageTask im2 = new ImageTask("../static/images/demoEquine09.jpg", Arrays.asList(q1));
        assertTrue(im1.equals(im2));

        ImageTask im3 = ImageTaskChooser.nextImageTaskSingle(q, 2);
        Question q2 = new Question("PlaneQ5","On which plane is the ultrasound taken?",2, "Transverse", Arrays.asList("Transverse", "Lateral", "I don't know"), "../static/images/demoEquine05.jpg");
        ImageTask im4 = new ImageTask("../static/images/demoEquine05.jpg", Arrays.asList(q2));
        assertTrue(im3.equals(im4));

        ImageTask im5 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q3 = new Question("StructureQ2","What structure is in the near field?",4, "ligament", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine09.jpg");
        ImageTask im6 = new ImageTask("../static/images/demoEquine09.jpg", Arrays.asList(q3));
        assertTrue(im5.equals(im6));

        ImageTask im7 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q4 = new Question("StructureQ4","What structure is in the near field?",4, "bone", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im8 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q4));
        assertTrue(im7.equals(im8));

        ImageTask im9 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q5 = new Question("StructureQ2","What structure is in the near field?",4, "ligament", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine09.jpg");
        ImageTask im10 = new ImageTask("../static/images/demoEquine09.jpg", Arrays.asList(q5));
        assertTrue(im9.equals(im10));

        ImageTask im11 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q6 = new Question("StructureQ4","What structure is in the near field?",4, "bone", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im12 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q6));
        assertTrue(im11.equals(im12));

        ImageTask im13 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q7 = new Question("StructureQ2","What structure is in the near field?",4, "ligament", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine09.jpg");
        ImageTask im14 = new ImageTask("../static/images/demoEquine09.jpg", Arrays.asList(q7));
        assertTrue(im13.equals(im14));

        ImageTask im15 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q8 = new Question("StructureQ4","What structure is in the near field?",4, "bone", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im16 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q8));
        assertTrue(im15.equals(im16));

        ImageTask im17 = ImageTaskChooser.nextImageTaskSingle(q, 8);
        Question q9 = new Question("ZoneQ5","In what zone is this ultrasound taken?",8, "2b", Arrays.asList("1a", "1b", "2a", "2b", "2c", "3a", "3b", "3c"), "../static/images/demoEquine02.jpg");
        ImageTask im18 = new ImageTask("../static/images/demoEquine02.jpg", Arrays.asList(q9));
        assertTrue(im17.equals(im18));

    }

    @Test
    public void getIndexesOfSameDifficultyTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        List<Integer> sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 1);
        assertEquals(3, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 2);
        assertEquals(2, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 3);
        assertEquals(2, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 4);
        assertEquals(2, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 5);
        assertEquals(1, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 6);
        assertEquals(2, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 7);
        assertEquals(2, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 8);
        assertEquals(1, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 90);
        assertEquals(0, sameDiffIndexes.size());

    }

    @Test
    public void indexOfSeenLeastTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        q.givenQuestion("StructureQ1");
        q.givenQuestion("StructureQ5");
        q.givenQuestion("StructureQ1");
        q.givenQuestion("StructureQ5");
        q.givenQuestion("StructureQ3");
        q.givenQuestion("StructureQ5");
        q.givenQuestion("StructureQ1");
        q.givenQuestion("StructureQ3");
        q.givenQuestion("StructureQ5");
        //Q1 seen 3 times
        //Q3 seen 2 times
        //Q5 seen 4 times

        int ind = ImageTaskChooser.indexOfSeenLeast(q, Arrays.asList(0, 1, 2));
        assertEquals(2, ind);

        q.givenQuestion("ZoneQ5");
        q.givenQuestion("PlaneQ1");
        q.givenQuestion("PlaneQ1");
        ind = ImageTaskChooser.indexOfSeenLeast(q, Arrays.asList(2, 3, 4));
        assertEquals(3, ind);
        q.givenQuestion("ZoneQ5");
        ind = ImageTaskChooser.indexOfSeenLeast(q, Arrays.asList(2, 3, 4));
        assertEquals(2, ind);
        q.givenQuestion("StructureQ3");
        ind = ImageTaskChooser.indexOfSeenLeast(q, Arrays.asList(0, 1, 2, 3, 4));
        assertEquals(3, ind);
        q.givenQuestion("PlaneQ1");
        q.givenQuestion("PlaneQ1");
        q.givenQuestion("PlaneQ1");
        q.givenQuestion("PlaneQ1");
        ind = ImageTaskChooser.indexOfSeenLeast(q, Arrays.asList(0, 1, 2, 3, 4));
        assertEquals(3, ind);
        q.givenQuestion("ZoneQ5");
        q.givenQuestion("ZoneQ5");
        q.givenQuestion("ZoneQ5");
        q.givenQuestion("ZoneQ5");
        q.givenQuestion("ZoneQ5");
        ind = ImageTaskChooser.indexOfSeenLeast(q, Arrays.asList(0, 1, 2, 3, 4));
        assertEquals(0, ind);
        q.givenQuestion("StructureQ1");
        q.givenQuestion("StructureQ1");
        q.givenQuestion("StructureQ1");
        q.givenQuestion("ZoneQ1");
        ind = ImageTaskChooser.indexOfSeenLeast(q, Arrays.asList(0, 1, 2, 3, 4, 5));
        assertEquals(5, ind);


    }

    @Test
    public void nextImageTaskTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/ImageTaskChooserTestsSampleQuestions.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        ImageTask it = ImageTaskChooser.nextImageTask(q, 4, 6);
        assertEquals(5, it.getTaskQuestions().size());

        List<Question> questionsFromFile2 = JsonUtil.listFromJsonFile("src/test/resources/author/ImageTaskChooserTestsSampleQuestions.json", Question.class);
        UserQuestionSet q2 = new UserQuestionSet("20", questionsFromFile2);
        ImageTask it2 = ImageTaskChooser.nextImageTask(q2, 7, 3);
        assertEquals(2, it2.getTaskQuestions().size());

        List<Question> questionsFromFile3 = JsonUtil.listFromJsonFile("src/test/resources/author/ImageTaskChooserTestsSampleQuestions.json", Question.class);
        UserQuestionSet q3 = new UserQuestionSet("20", questionsFromFile3);
        ImageTask it3 = ImageTaskChooser.nextImageTask(q3, 5, 1);
        assertEquals(1, it3.getTaskQuestions().size());

        List<Question> questionsFromFile4 = JsonUtil.listFromJsonFile("src/test/resources/author/ImageTaskChooserTestsSampleQuestions.json", Question.class);
        UserQuestionSet q4 = new UserQuestionSet("20", questionsFromFile4);
        ImageTask it4 = ImageTaskChooser.nextImageTask(q4, 8, 4);
        assertEquals(4, it4.getTaskQuestions().size());

        List<Question> questionsFromFile5 = JsonUtil.listFromJsonFile("src/test/resources/author/ImageTaskChooserTestsSampleQuestions.json", Question.class);
        UserQuestionSet q5 = new UserQuestionSet("20", questionsFromFile5);
        ImageTask it5 = ImageTaskChooser.nextImageTask(q5, 8, 4);
        assertEquals(4, it5.getTaskQuestions().size());
    }

    @Test
    public void lessOrEqualDifficultyAddedTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        ImageTask it = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 0);
        ImageTaskChooser.lessOrEqualDifficultyAdded(it, 3, q.getUnseenQuestions());
        assertEquals(3, it.getTaskQuestions().size());

        UserQuestionSet q2 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it2 = ImageTaskChooser.makeTask(q2.getUnseenQuestions(), 1);
        ImageTaskChooser.lessOrEqualDifficultyAdded(it2, 7, q2.getUnseenQuestions());
        assertEquals(3, it2.getTaskQuestions().size());

        UserQuestionSet q3 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it3 = ImageTaskChooser.makeTask(q3.getUnseenQuestions(), 1);
        ImageTaskChooser.lessOrEqualDifficultyAdded(it3, 2, q3.getUnseenQuestions());
        assertEquals(2, it3.getTaskQuestions().size());

        UserQuestionSet q4 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it4 = ImageTaskChooser.makeTask(q4.getUnseenQuestions(), 6);
        ImageTaskChooser.lessOrEqualDifficultyAdded(it4, 3, q4.getUnseenQuestions());
        assertEquals(3, it4.getTaskQuestions().size());

        UserQuestionSet q5 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it5 = ImageTaskChooser.makeTask(q5.getUnseenQuestions(), 14);
        ImageTaskChooser.lessOrEqualDifficultyAdded(it5, 15, q5.getUnseenQuestions());
        assertEquals(15, it5.getTaskQuestions().size());


    }

    @Test
    public void greaterDifficultyAddedTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        ImageTask it = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 0);
        ImageTaskChooser.greaterDifficultyAdded(it, 3, q.getUnseenQuestions());
        assertEquals(3, it.getTaskQuestions().size());

        UserQuestionSet q2 = new UserQuestionSet("21", questionsFromFile);
        ImageTask it2 = ImageTaskChooser.makeTask(q2.getUnseenQuestions(), 1);
        ImageTaskChooser.greaterDifficultyAdded(it2, 7, q2.getUnseenQuestions());
        assertEquals(7, it2.getTaskQuestions().size());

        UserQuestionSet q3 = new UserQuestionSet("22", questionsFromFile);
        ImageTask it3 = ImageTaskChooser.makeTask(q3.getUnseenQuestions(), 1);
        ImageTaskChooser.greaterDifficultyAdded(it3, 2, q3.getUnseenQuestions());
        assertEquals(2, it3.getTaskQuestions().size());

        UserQuestionSet q4 = new UserQuestionSet("23", questionsFromFile);
        ImageTask it4 = ImageTaskChooser.makeTask(q4.getUnseenQuestions(), 6);
        ImageTaskChooser.greaterDifficultyAdded(it4, 3, q4.getUnseenQuestions());
        assertEquals(3, it4.getTaskQuestions().size());

        UserQuestionSet q5 = new UserQuestionSet("24", questionsFromFile);
        ImageTask it5 = ImageTaskChooser.makeTask(q5.getUnseenQuestions(), 1);
        ImageTaskChooser.greaterDifficultyAdded(it5, 15, q5.getUnseenQuestions());
        assertEquals(13, it5.getTaskQuestions().size());

        UserQuestionSet q6 = new UserQuestionSet("25", questionsFromFile);
        ImageTask it6 = ImageTaskChooser.makeTask(q6.getUnseenQuestions(), 14);
        ImageTaskChooser.greaterDifficultyAdded(it6, 14, q6.getUnseenQuestions());
        assertEquals(1, it6.getTaskQuestions().size());


    }

    @Test
    public void addAllQuestionsTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        ImageTask it = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 0);
        ImageTaskChooser.addAllQuestions(it, q.getUnseenQuestions(), q.getSeenQuestions());
        assertEquals(15, it.getTaskQuestions().size());

        UserQuestionSet q2 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it2 = ImageTaskChooser.makeTask(q2.getUnseenQuestions(), 3);
        ImageTaskChooser.addAllQuestions(it2, q.getUnseenQuestions(), q2.getSeenQuestions());
        assertEquals(15, it2.getTaskQuestions().size());

        UserQuestionSet q3 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it3 = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 7);
        ImageTaskChooser.addAllQuestions(it3, q.getUnseenQuestions(), q3.getSeenQuestions());
        assertEquals(15, it3.getTaskQuestions().size());

        UserQuestionSet q4 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it4 = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 10);
        ImageTaskChooser.addAllQuestions(it4, q4.getUnseenQuestions(), q4.getSeenQuestions());
        assertEquals(15, it4.getTaskQuestions().size());

        UserQuestionSet q5 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it5 = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 14);
        ImageTaskChooser.addAllQuestions(it5, q5.getUnseenQuestions(), q5.getSeenQuestions());
        assertEquals(15, it5.getTaskQuestions().size());
    }

    @Test
    public void getSameUrlQuestionsUnseenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        ImageTask it = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 0);
        List<Question> u1 = ImageTaskChooser.getSameUrlQuestionsUnseen(q, it.getTaskQuestions().get(0));
        assertEquals(3, u1.size());

        UserQuestionSet q2 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it2 = ImageTaskChooser.makeTask(q2.getUnseenQuestions(), 5);
        List<Question> u2 = ImageTaskChooser.getSameUrlQuestionsUnseen(q2, it2.getTaskQuestions().get(0));
        assertEquals(4, u2.size());

        UserQuestionSet q3 = new UserQuestionSet("20", questionsFromFile);
        ImageTask it3 = ImageTaskChooser.makeTask(q3.getUnseenQuestions(), 11);
        List<Question> u3 = ImageTaskChooser.getSameUrlQuestionsUnseen(q3, it3.getTaskQuestions().get(0));
        assertEquals(3, u3.size());

    }

    @Test
    public void getSameUrlQuestionsSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        q.givenQuestion("PlaneQ1");
        q.givenQuestion("PlaneQ2");
        q.givenQuestion("PlaneQ3");
        q.givenQuestion("PlaneQ4");
        q.givenQuestion("PlaneQ5");
        q.givenQuestion("StructureQ1");
        q.givenQuestion("StructureQ2");
        q.givenQuestion("StructureQ3");
        q.givenQuestion("StructureQ4");
        q.givenQuestion("StructureQ5");
        q.givenQuestion("ZoneQ1");
        q.givenQuestion("ZoneQ2");
        q.givenQuestion("ZoneQ3");
        q.givenQuestion("ZoneQ4");
        q.givenQuestion("ZoneQ5");


        ImageTask it = ImageTaskChooser.makeTask(q.getSeenQuestions(), 0);
        List<Question> u1 = ImageTaskChooser.getSameUrlQuestionsSeen(q, it.getTaskQuestions().get(0));
        assertEquals(3, u1.size());

        ImageTask it2 = ImageTaskChooser.makeTask(q.getSeenQuestions(), 5);
        List<Question> u2 = ImageTaskChooser.getSameUrlQuestionsSeen(q, it2.getTaskQuestions().get(0));
        assertEquals(4, u2.size());

        ImageTask it3 = ImageTaskChooser.makeTask(q.getSeenQuestions(), 11);
        List<Question> u3 = ImageTaskChooser.getSameUrlQuestionsSeen(q, it3.getTaskQuestions().get(0));
        assertEquals(3, u3.size());

    }

    @Test
    public void markAllAsSeenTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestionPool.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);
        ImageTask it = ImageTaskChooser.makeTask(q.getUnseenQuestions(), 1);
        it.getTaskQuestions().add(q.getUnseenQuestions().get(5));
        it.getTaskQuestions().add(q.getUnseenQuestions().get(7));
        it.getTaskQuestions().add(q.getUnseenQuestions().get(8));
        it.getTaskQuestions().add(q.getUnseenQuestions().get(9));
        ImageTaskChooser.markAllAsSeen(it, q);
        q.givenQuestion(it.getTaskQuestions().get(0).getId());
        assertEquals(5, q.getSeenQuestions().size());

        it.getTaskQuestions().add(q.getUnseenQuestions().get(5));
        it.getTaskQuestions().add(q.getUnseenQuestions().get(2));
        it.getTaskQuestions().add(q.getUnseenQuestions().get(4));
        ImageTaskChooser.markAllAsSeen(it, q);
        assertEquals(8, q.getSeenQuestions().size());
        assertEquals(7, q.getUnseenQuestions().size());

    }

}

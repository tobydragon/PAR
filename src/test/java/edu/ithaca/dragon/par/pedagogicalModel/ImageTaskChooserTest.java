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
    public void nextImageTaskTest() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("2", questionsFromFile);
        ImageTask im1 = ImageTaskChooser.nextImageTaskSingle(q, 1);
        assertEquals(1, im1.getTaskQuestions().get(0).getDifficulty());
        int len = q.getLenOfSeenQuestions();
        assertEquals(1, len);

        ImageTask im2 = ImageTaskChooser.nextImageTaskSingle(q, 2);
        assertEquals(2, im2.getTaskQuestions().get(0).getDifficulty());
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        ImageTask im3 = ImageTaskChooser.nextImageTaskSingle(q, 3);
        assertNull(im3);
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        ImageTask im4 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        assertEquals(4, im4.getTaskQuestions().get(0).getDifficulty());
        len = q.getLenOfSeenQuestions();
        assertEquals(3, len);

    }

    @Test
    public void nextImageTaskIT() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions2.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);


        ImageTask im1 = ImageTaskChooser.nextImageTaskSingle(q, 2);
        Question q1 = new Question("PlaneQ2","On which plane is the ultrasound taken?",2, "Transverse", Arrays.asList("Transverse", "Lateral", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im2 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q1));
        assertTrue(im1.equals(im2));

        ImageTask im3 = ImageTaskChooser.nextImageTaskSingle(q, 2);
        Question q2 = new Question("PlaneQ3","On which plane is the ultrasound taken?",2, "Lateral", Arrays.asList("Transverse", "Lateral", "I don't know"), "../static/images/demoEquine05.jpg");
        ImageTask im4 = new ImageTask("../static/images/demoEquine05.jpg", Arrays.asList(q2));
        assertTrue(im3.equals(im4));

        ImageTask im5 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q3 = new Question("StructureQ1","What structure is in the near field?",4, "bone", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im6 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q3));
        assertTrue(im5.equals(im6));

        ImageTask im7 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q4 = new Question("StructureQ3","What structure is in the near field?",4, "tendon", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im8 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q4));
        assertTrue(im7.equals(im8));

        ImageTask im9 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q5 = new Question("StructureQ5","What structure is in the near field?",4, "tumor", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im10 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q5));
        assertTrue(im9.equals(im10));

        ImageTask im11 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q6 = new Question("StructureQ1","What structure is in the near field?",4, "bone", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im12 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q6));
        assertTrue(im11.equals(im12));

        ImageTask im13 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q7 = new Question("StructureQ3","What structure is in the near field?",4, "tendon", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im14 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q7));
        assertTrue(im13.equals(im14));

        ImageTask im15 = ImageTaskChooser.nextImageTaskSingle(q, 4);
        Question q8 = new Question("StructureQ5","What structure is in the near field?",4, "tumor", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im16 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q8));
        assertTrue(im15.equals(im16));

        ImageTask im17 = ImageTaskChooser.nextImageTaskSingle(q, 8);
        Question q9 = new Question("ZoneQ5","In what zone is this ultrasound taken?",8, "2b", Arrays.asList("1a", "1b", "2a", "2b", "2c", "3a", "3b", "3c"), "../static/images/demoEquine10.jpg");
        ImageTask im18 = new ImageTask("../static/images/demoEquine10.jpg", Arrays.asList(q9));
        assertTrue(im17.equals(im18));

    }

    @Test
    public void getIndexesOfSameDifficultyTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions2.json", Question.class);
        List<Integer> sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 1);
        assertEquals(1, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 2);
        assertEquals(4, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 3);
        assertEquals(1, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 4);
        assertEquals(3, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 5);
        assertEquals(1, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 6);
        assertEquals(0, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 7);
        assertEquals(4, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 8);
        assertEquals(1, sameDiffIndexes.size());

        sameDiffIndexes = ImageTaskChooser.getIndexesOfSameDifficulty(questionsFromFile, 90);
        assertEquals(0, sameDiffIndexes.size());

    }

    @Test
    public void indexOfSeenLeastTest() throws IOException{
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions2.json", Question.class);
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







}

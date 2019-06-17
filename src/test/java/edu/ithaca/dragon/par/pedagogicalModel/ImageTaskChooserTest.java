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
        ImageTask im1 = ImageTaskChooser.nextImageTask(q, 1);
        assertEquals(1, im1.getTaskQuestions().get(0).getDifficulty());
        int len = q.getLenOfSeenQuestions();
        assertEquals(1, len);

        ImageTask im2 = ImageTaskChooser.nextImageTask(q, 2);
        assertEquals(2, im2.getTaskQuestions().get(0).getDifficulty());
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        ImageTask im3 = ImageTaskChooser.nextImageTask(q, 3);
        assertNull(im3);
        len = q.getLenOfSeenQuestions();
        assertEquals(2, len);

        ImageTask im4 = ImageTaskChooser.nextImageTask(q, 4);
        assertEquals(4, im4.getTaskQuestions().get(0).getDifficulty());
        len = q.getLenOfSeenQuestions();
        assertEquals(3, len);

    }

    @Test
    public void nextImageTaskIT() throws IOException {
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions2.json", Question.class);
        UserQuestionSet q = new UserQuestionSet("20", questionsFromFile);


        ImageTask im1 = ImageTaskChooser.nextImageTask(q, 2);
        Question q1 = new Question("PlaneQ2","On which plane is the ultrasound taken?",2, "Transverse", Arrays.asList("Transverse", "Lateral", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im2 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q1));
        assertTrue(im1.equals(im2));

        ImageTask im3 = ImageTaskChooser.nextImageTask(q, 2);
        Question q2 = new Question("PlaneQ3","On which plane is the ultrasound taken?",2, "Lateral", Arrays.asList("Transverse", "Lateral", "I don't know"), "../static/images/demoEquine05.jpg");
        ImageTask im4 = new ImageTask("../static/images/demoEquine05.jpg", Arrays.asList(q2));
        assertTrue(im3.equals(im4));

        ImageTask im5 = ImageTaskChooser.nextImageTask(q, 4);
        Question q3 = new Question("StructureQ1","What structure is in the near field?",4, "bone", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im6 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q3));
        assertTrue(im5.equals(im6));

        ImageTask im7 = ImageTaskChooser.nextImageTask(q, 4);
        Question q4 = new Question("StructureQ3","What structure is in the near field?",4, "tendon", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im8 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q4));
        assertTrue(im7.equals(im8));

        ImageTask im9 = ImageTaskChooser.nextImageTask(q, 4);
        Question q5 = new Question("StructureQ5","What structure is in the near field?",4, "tumor", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im10 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q5));
        assertTrue(im9.equals(im10));

        ImageTask im11 = ImageTaskChooser.nextImageTask(q, 4);
        Question q6 = new Question("StructureQ1","What structure is in the near field?",4, "bone", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im12 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q6));
        assertTrue(im11.equals(im12));

        ImageTask im13 = ImageTaskChooser.nextImageTask(q, 4);
        Question q7 = new Question("StructureQ3","What structure is in the near field?",4, "tendon", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im14 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q7));
        assertTrue(im13.equals(im14));

        ImageTask im15 = ImageTaskChooser.nextImageTask(q, 4);
        Question q8 = new Question("StructureQ5","What structure is in the near field?",4, "tumor", Arrays.asList("bone", "ligament", "tumor", "tendon", "I don't know"), "../static/images/demoEquine04.jpg");
        ImageTask im16 = new ImageTask("../static/images/demoEquine04.jpg", Arrays.asList(q8));
        assertTrue(im15.equals(im16));


    }

}

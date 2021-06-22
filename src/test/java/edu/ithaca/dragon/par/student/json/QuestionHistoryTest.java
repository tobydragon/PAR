package edu.ithaca.dragon.par.student.json;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionHistoryTest {

    public static List<QuestionHistory> makeExamples(){
        //TODO: change Map to use List
        
        //Map<String, QuestionHistory> examples = new HashMap<>();
        List<QuestionHistory> examples = new ArrayList<>();

        QuestionHistory ex1 = new QuestionHistory("q1");
        ex1.addTimeSeen();
        ex1.addTimeSeen();
        ex1.addResponse("a");
        ex1.addResponse("b");
        examples.add(ex1);

        QuestionHistory ex2 = new QuestionHistory("q2");
        ex2.addTimeSeen();
        ex2.addTimeSeen();
        examples.add(ex2);

        QuestionHistory ex3 = new QuestionHistory("q3");
        ex3.addTimeSeen();
        ex3.addResponse("c");
        examples.add(ex3);

        QuestionHistory ex4 = new QuestionHistory("q4");
        ex4.addTimeSeen();
        examples.add(ex4);

        QuestionHistory ex5 = new QuestionHistory("q5");
        ex5.addTimeSeen();
        ex5.addTimeSeen();
        ex5.addResponse("d");
        ex5.addResponse("e");
        examples.add(ex5);

        return examples;
    }

    public static List<QuestionHistory> SampleQuestionsEx(){
        List<QuestionHistory> sample = new ArrayList<>();

        QuestionHistory q1 = new QuestionHistory("skyQ");
        q1.addTimeSeen();
        q1.addResponse("blue");
        sample.add(q1);

        QuestionHistory q2 = new QuestionHistory("mathQ");
        q2.addTimeSeen();
        q2.addResponse("2");
        sample.add(q2);

        QuestionHistory q3 = new QuestionHistory("majorQ");
        q3.addTimeSeen();
        q3.addResponse("ENG");
        q3.addResponse("MATH");
        q3.addResponse("CS");
        sample.add(q3);

        return sample;
    }

    public static List<QuestionHistory> poorStudent(){
        List<QuestionHistory> poor = new ArrayList<>();

        QuestionHistory sky = new QuestionHistory("skyQ");
        sky.addTimeSeen();
        poor.add(sky);

        QuestionHistory math = new QuestionHistory("mathQ");
        math.addTimeSeen();
        poor.add(math);

        QuestionHistory major = new QuestionHistory("majorQ");
        major.addTimeSeen();
        major.addResponse("ENG");
        poor.add(major);

        QuestionHistory year = new QuestionHistory("yearQ");
        year.addTimeSeen();
        year.addResponse("2021");
        poor.add(year);

        QuestionHistory google = new QuestionHistory("googleQ");
        google.addTimeSeen();
        poor.add(google);

        //flipped through until repeat
        sky.addTimeSeen(1000L);
        math.addTimeSeen(2000L);
        major.addTimeSeen(3000L);
        year.addTimeSeen(4000L);
        google.addTimeSeen(5000L);
        //flipping again for known ones
        sky.addTimeSeen(6000L);
        math.addTimeSeen(7000L);
        major.addTimeSeen(8000L);
        //putting in a few answers
        major.addResponse("ENG", 10000L);
        year.addTimeSeen(11000L);
        year.addResponse("2021", 13000L);

        return List.of(sky, math, major, year, google);
    }

    public static List<QuestionHistory> strongStudent(){
        List<QuestionHistory> strong = new ArrayList<>();

        QuestionHistory sky = new QuestionHistory("skyQ");
        sky.addTimeSeen();
        sky.addResponse("blue");
        strong.add(sky);

        QuestionHistory math = new QuestionHistory("mathQ");
        math.addTimeSeen();
        math.addResponse("2");
        strong.add(math);

        QuestionHistory major = new QuestionHistory("majorQ");
        major.addTimeSeen();
        major.addResponse("CS");
        strong.add(major);

        QuestionHistory year = new QuestionHistory("yearQ");
        year.addTimeSeen();
        year.addResponse("2020");
        strong.add(year);

        QuestionHistory google = new QuestionHistory("googleQ");
        google.addTimeSeen();
        google.addResponse("1998");
        strong.add(google);

        return strong;
    }

    public static List<QuestionHistory> improvingStudent(){
        List<QuestionHistory> improving = new ArrayList<>();

        QuestionHistory sky = new QuestionHistory("skyQ");
        sky.addTimeSeen();
        improving.add(sky);

        QuestionHistory math = new QuestionHistory("mathQ");
        math.addTimeSeen();
        math.addResponse("2");
        improving.add(math);

        QuestionHistory major = new QuestionHistory("majorQ");
        major.addTimeSeen();
        major.addResponse("CS");
        improving.add(major);

        QuestionHistory year = new QuestionHistory("yearQ");
        year.addTimeSeen();
        year.addResponse("2021");
        //year.addResponse("2020");
        improving.add(year);

        QuestionHistory google = new QuestionHistory("googleQ");
        google.addTimeSeen();
        google.addResponse("2005");
        google.addResponse("1998");
        improving.add(google);

        return improving;
    }
}
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

        QuestionHistory sky = new QuestionHistory("generic0");
        sky.addTimeSeen();
        sky.addResponse("blue");
        sample.add(sky);

        QuestionHistory math = new QuestionHistory("generic1");
        math.addTimeSeen();
        math.addResponse("2");
        sample.add(math);

        QuestionHistory major = new QuestionHistory("generic2");
        major.addTimeSeen();
        major.addResponse("ENG");
        sample.add(major);

        return sample;
    }

}
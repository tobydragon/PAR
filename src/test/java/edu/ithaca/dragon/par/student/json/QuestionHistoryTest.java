package edu.ithaca.dragon.par.student.json;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionHistoryTest {

    public static Map<String, QuestionHistory> makeExamples(){
        Map<String, QuestionHistory> examples = new HashMap<>();

        QuestionHistory ex1 = new QuestionHistory("q1");
        ex1.addTimeSeen();
        ex1.addTimeSeen();
        ex1.addResponse("a");
        ex1.addResponse("b");
        examples.put(ex1.getQuestionId(), ex1);

        QuestionHistory ex2 = new QuestionHistory("q2");
        ex2.addTimeSeen();
        ex2.addResponse("b");
        examples.put(ex2.getQuestionId(), ex2);

        QuestionHistory ex3 = new QuestionHistory("q3");
        ex1.addTimeSeen();
        ex1.addTimeSeen();
        examples.put(ex3.getQuestionId(), ex3);

        return examples;
    }

}
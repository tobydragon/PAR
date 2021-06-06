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
        ex2.addTimeSeen();
        examples.put(ex2.getQuestionId(), ex2);

        QuestionHistory ex3 = new QuestionHistory("q3");
        ex3.addTimeSeen();
        ex3.addResponse("c");
        examples.put(ex3.getQuestionId(), ex3);

        QuestionHistory ex4 = new QuestionHistory("q4");
        ex4.addTimeSeen();
        examples.put(ex4.getQuestionId(), ex4);

        QuestionHistory ex5 = new QuestionHistory("q5");
        ex5.addTimeSeen();
        ex5.addTimeSeen();
        ex5.addResponse("d");
        ex5.addResponse("e");
        examples.put(ex5.getQuestionId(), ex5);

        return examples;
    }

}
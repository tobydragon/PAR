package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.par.studentModel.QuestionCount;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionWithFeedbackTest {

    @Test
    public void getQuestionAndComponentsTest() {
        Question q1 = new Question("1", "what plane?", "plane", "transverse", Arrays.asList("transverse", "longitudinal"), "/imageURL");
        QuestionWithFeedback qf1 = new QuestionWithFeedback(q1, "pog");

        Question q2 = new Question("2", "what plane?", "plane", "longitudinal", Arrays.asList("transverse", "longitudinal"), "/imageURL2");
        QuestionWithFeedback qf2 = new QuestionWithFeedback(q2, "epic");

        assertEquals(q1, qf1.getQuestion());
        assertEquals(q2, qf2.getQuestion());

        assertEquals("pog", qf1.getFeedback());
        assertEquals("epic", qf2.getFeedback());

        assertEquals(q1.getId(), qf1.getQuestionId());
        assertEquals(q1.getFollowupQuestions(), qf1.getQuestionFollowupQuestions());
        assertEquals(q1.getImageUrl(), qf1.getQuestionImageUrl());
        assertEquals(q1.getCorrectAnswer(), qf1.getQuestionCorrectAnswer());
        assertEquals(q1.getType(), qf1.getQuestionType());
        assertEquals(q1.getQuestionText(), qf1.getQuestionText());
        assertEquals(q1.getPossibleAnswers(), qf1.getQuestionPossibleAnswers());

        assertEquals(q2.getId(), qf2.getQuestionId());
        assertEquals(q2.getFollowupQuestions(), qf2.getQuestionFollowupQuestions());
        assertEquals(q2.getImageUrl(), qf2.getQuestionImageUrl());
        assertEquals(q2.getCorrectAnswer(), qf2.getQuestionCorrectAnswer());
        assertEquals(q2.getType(), qf2.getQuestionType());
        assertEquals(q2.getQuestionText(), qf2.getQuestionText());
        assertEquals(q2.getPossibleAnswers(), qf2.getQuestionPossibleAnswers());

    }
}

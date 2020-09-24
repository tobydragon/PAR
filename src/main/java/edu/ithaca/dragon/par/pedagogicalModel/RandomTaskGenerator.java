package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.List;
import java.util.Random;

public class RandomTaskGenerator implements TaskGenerator {
    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        List<Question> questionsToSelect = studentModel.getUserQuestionSet().getAllQuestions();
        Question initialQuestion= questionsToSelect.get(new Random().nextInt(questionsToSelect.size()));
        List<Question> questionList = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getAllQuestions(), initialQuestion.getImageUrl());
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList);

        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(questionList);
        return imageTask;
    }
}

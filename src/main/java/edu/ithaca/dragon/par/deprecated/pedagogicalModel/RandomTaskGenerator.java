package edu.ithaca.dragon.par.deprecated.pedagogicalModel;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.io.ImageTask;
import edu.ithaca.dragon.par.deprecated.studentModel.StudentModel;

import java.util.List;
import java.util.Random;

public class RandomTaskGenerator implements TaskGenerator {
    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        List<Question> questionsToSelect = studentModel.getUserQuestionSet().getAllQuestions();
        Question initialQuestion= questionsToSelect.get(new Random().nextInt(questionsToSelect.size()));
        List<Question> questionList = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getAllQuestions(), initialQuestion.getImageUrl());
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList, "None");
        return imageTask;
    }
}

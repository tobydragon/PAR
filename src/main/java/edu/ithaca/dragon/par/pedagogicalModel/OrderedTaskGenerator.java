package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class OrderedTaskGenerator implements TaskGenerator {
    private List<String> questionIds;
    private int lastQuestionAskedIndex;

    public OrderedTaskGenerator (){
        this.questionIds = new ArrayList<>();
        hardcodedInitializeQuestionSet();
        this.lastQuestionAskedIndex = 0;
    }

    public OrderedTaskGenerator(QuestionPool questionsToAsk, boolean isFollowupIncluded) {
    }

    private void hardcodedInitializeQuestionSet() {
        this.questionIds.add("PlaneQ1");
        this.questionIds.add("PlaneQ2");
        this.questionIds.add("PlaneQ3");
        this.questionIds.add("PlaneQ4");
        this.questionIds.add("PlaneQ5");

        this.questionIds.add("StructureQ1");
        this.questionIds.add("StructureQ2");
        this.questionIds.add("StructureQ3");
        this.questionIds.add("StructureQ4");
        this.questionIds.add("StructureQ5");

        this.questionIds.add("ZoneQ1");
        this.questionIds.add("ZoneQ2");
        this.questionIds.add("ZoneQ3");
        this.questionIds.add("ZoneQ4");
        this.questionIds.add("ZoneQ5");
    }

    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        List<Question> questionsToSelect = studentModel.getUserQuestionSet().getAllQuestions();
        Question initialQuestion= questionsToSelect.get((lastQuestionAskedIndex + 1) % questionIds.size());
        List<Question> questionList = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getAllQuestions(), initialQuestion.getImageUrl());
        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList);

        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(questionList);
        return imageTask;
    }
}

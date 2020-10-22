package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class OrderedTaskGenerator implements TaskGenerator {
    private final List<QuestionOrderedInfo> questionIds;
    private int lastQuestionAskedIndex;
    private final QuestionPool questionPool;

    //orderedQuestionList assumed to be subset of StudentModel QuestionPool
    // TODO: 10/1/20 make robust by handling bad IDs
    public OrderedTaskGenerator(QuestionPool questionsToAsk, List<QuestionOrderedInfo> orderedQuestionList) {
        this.questionIds = orderedQuestionList;
        this.lastQuestionAskedIndex = 0;
        this.questionPool = questionsToAsk;
    }

    //isFollowupAttached works as a default value for all top level questions. This can be changed in the .json files manually for now
    //followup questions are not included in List<QuestionOrderedInfo> because they are retrieved from the QuestionPool in makeTask
    public static List<QuestionOrderedInfo> createOrderedQuestionInfoListFromQuestionPool(QuestionPool questionsToAdd, boolean isFollowupAttached) {
        List<QuestionOrderedInfo> toReturn = new ArrayList<>();
        List<Question> individualQuestions = questionsToAdd.getAllQuestions();
        for (Question temp : individualQuestions) {
            QuestionOrderedInfo tempInfo = new QuestionOrderedInfo(temp.getId(), isFollowupAttached);
            toReturn.add(tempInfo);
        }
        return toReturn;
    }

    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
        if(this.lastQuestionAskedIndex == questionIds.size() - 1){
            this.lastQuestionAskedIndex = 0;
        } else {
            this.lastQuestionAskedIndex = (this.lastQuestionAskedIndex + 1) % questionIds.size();
        }
        QuestionOrderedInfo nextQuestion = questionIds.get(lastQuestionAskedIndex);
        List<Question> addToTask = new ArrayList<>();
        Question topQuestion = this.questionPool.getQuestionFromId(nextQuestion.getQuestionID());
        if (!nextQuestion.isIncludesFollowup()){
            topQuestion.setFollowupQuestions(new ArrayList<>());
        }
        addToTask.add(topQuestion);
        ImageTask imageTask = new ImageTask(topQuestion.getImageUrl(), addToTask, "None");
        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(addToTask);
        return imageTask;
    }
}

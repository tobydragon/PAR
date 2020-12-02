package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoHelperSpring;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.apache.commons.lang3.ObjectUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderedTaskGenerator implements TaskGenerator {
    private final List<QuestionOrderedInfo> questionOrderedInfoList;
    private int lastQuestionAskedIndex;
    private final QuestionPool questionPool;

    //orderedQuestionList assumed to be subset of StudentModel QuestionPool
    public OrderedTaskGenerator(QuestionPool questionsToAsk, List<QuestionOrderedInfo> orderedQuestionList) {
        this.questionOrderedInfoList = orderedQuestionList;
        this.lastQuestionAskedIndex = 0;
        this.questionPool = questionsToAsk;
    }

    // creates a default QuestionOrderedInfoList
    public OrderedTaskGenerator(QuestionPool questionsToAsk) {
        this.questionPool = questionsToAsk;
        this.questionOrderedInfoList = createDefaultQuestionOrderedInfoList(questionPool, true);
        this.lastQuestionAskedIndex = 0;

    }

    public List<QuestionOrderedInfo> getQuestionOrderedInfoList() {
        return questionOrderedInfoList;
    }

    //isFollowupAttached works as a default value for all top level questions. This can be changed in the .json files manually for now
    //followup questions are not included in List<QuestionOrderedInfo> because they are retrieved from the QuestionPool in makeTask
    public static List<QuestionOrderedInfo> createDefaultQuestionOrderedInfoList(QuestionPool questionsToAdd, boolean isFollowupAttached) {
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

        QuestionOrderedInfo nextQuestion = questionOrderedInfoList.get(lastQuestionAskedIndex);
        List<Question> addToTask = new ArrayList<>();
        Question topQuestion = this.questionPool.getQuestionFromId(nextQuestion.getQuestionID());
        if (!nextQuestion.isIncludesFollowup()){
            topQuestion.setFollowupQuestions(new ArrayList<>());
        }
        addToTask.add(topQuestion);
        ImageTask imageTask = new ImageTask(topQuestion.getImageUrl(), addToTask, "None");
        if(this.lastQuestionAskedIndex == questionOrderedInfoList.size() - 1){
            this.lastQuestionAskedIndex = 0;
        } else {
            this.lastQuestionAskedIndex = (this.lastQuestionAskedIndex + 1) % questionOrderedInfoList.size();
        }
        return imageTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderedTaskGenerator)) return false;
        OrderedTaskGenerator that = (OrderedTaskGenerator) o;
        return lastQuestionAskedIndex == that.lastQuestionAskedIndex &&
                Objects.equals(questionOrderedInfoList, that.questionOrderedInfoList) &&
                Objects.equals(questionPool, that.questionPool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionOrderedInfoList, lastQuestionAskedIndex, questionPool);
    }
}
